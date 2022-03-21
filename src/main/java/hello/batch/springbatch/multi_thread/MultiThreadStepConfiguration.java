package hello.batch.springbatch.multi_thread;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class MultiThreadStepConfiguration {

	public static final int CHUNK_SIZE = 10;
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final DataSource dataSource;

	@Bean
	public Job multiThreadJob() {
		return jobBuilderFactory.get("multiThreadJob")
			.start(step1())
			.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("multiThreadStep")
			.<Client, Client>chunk(CHUNK_SIZE)
			.reader(itemReader())
			.listener(new ItemReaderListener())
			.processor((ItemProcessor<Client, Client>)item -> item)
			.listener(new ItemProcessorListener())
			.writer(itemWriter())
			.listener(new ItemWriterListener())
			.build();
	}

	@Bean
	public ItemWriter<? super Client> itemWriter() {
		return new JdbcBatchItemWriterBuilder<Client>()
			.dataSource(dataSource)
			.sql("insert into client2 values(:username, :age, :year)")
			.beanMapped()
			.build();
	}

	@Bean
	public ItemReader<? extends Client> itemReader() {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("username", "user");

		return new JdbcPagingItemReaderBuilder<Client>()
			.name("jdbcPagingReader")
			.dataSource(dataSource)
			.pageSize(CHUNK_SIZE)
			.rowMapper(new BeanPropertyRowMapper<>())
			.selectClause("username, age, year")
			.fromClause("from client")
			.whereClause("where username like :username")
			.parameterValues(parameters)
			.build();
	}
}
