package hello.batch.springbatch.async;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class AsyncItemConfiguration {

	public static final int CHUNK_SIZE = 5;
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final DataSource dataSource;

	@Bean
	public Job asyncJob() {
		return jobBuilderFactory.get("asyncJob")
			.start(step1())
			.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("asyncStep")
			.<Client, Client>chunk(CHUNK_SIZE)
			.reader(itemReader())
			.processor(asyncItemProcessor())
			.writer(asyncItemWriter())
			.build();
	}

	@Bean
	public AsyncItemWriter<Client> asyncItemWriter() {
		AsyncItemWriter<Client> asyncItemWriter = new AsyncItemWriter<>();
		asyncItemWriter.setDelegate(itemWriter());
		return asyncItemWriter;
	}

	@Bean
	public ItemWriter<Client> itemWriter() {
		return items -> {
			for (Client item : items) {
				System.out.println("item = " + item);
			}
		};
	}

	@Bean
	public AsyncItemProcessor asyncItemProcessor() {
		AsyncItemProcessor<Client, Client> asyncItemProcessor = new AsyncItemProcessor<>();
		asyncItemProcessor.setDelegate(itemProcessor());
		asyncItemProcessor.setTaskExecutor(new SimpleAsyncTaskExecutor());
		return asyncItemProcessor;
	}

	@Bean
	public ItemProcessor<Client, Client> itemProcessor() {
		return client -> {
			Client newClient = Client.builder()
				.username(client.getUsername().toUpperCase())
				.age(client.getAge())
				.year(client.getYear())
				.build();
			return newClient;
		};
	}

	@Bean
	public ItemReader<? extends Client> itemReader() {
		Map<String, Order> sortKeys = new HashMap<>();
		sortKeys.put("age", Order.DESCENDING);

		return new JdbcPagingItemReaderBuilder<Client>()
			.name("clientPaging")
			.dataSource(dataSource)
			.pageSize(CHUNK_SIZE).fetchSize(CHUNK_SIZE)
			.beanRowMapper(Client.class)
			.selectClause("username, age, yaer")
			.fromClause("from client")
			.sortKeys(sortKeys)
			.build();
	}
}
