package hello.batch.springbatch.json;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import hello.batch.springbatch.flatfile.Client;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class JsonFileItemWriterConfiguration {

	public static final int CHUNK_SIZE = 5;
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final DataSource dataSource;

	@Bean
	public Job jsonFileWriterJob() {
		return jobBuilderFactory.get("jsonFileWriterJob")
			.start(step1())
			.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
			.<Client, Client>chunk(CHUNK_SIZE)
			.reader(itemReader())
			.writer(itemWriter())
			.build();
	}

	@Bean
	public ItemWriter<? super Client> itemWriter() {
		return new JsonFileItemWriterBuilder<Client>()
			.name("jsonFileWriter")
			.resource(new FileSystemResource("C:\\study\\spring-batch\\src\\main\\resources\\client.json"))
			.jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
			.build();
	}

	@Bean
	public ItemReader<? extends Client> itemReader() {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("name", "user%");

		return new JdbcPagingItemReaderBuilder<Client>()
			.name("jdbcPagingReader")
			.dataSource(dataSource)
			.beanRowMapper(Client.class)
			.pageSize(CHUNK_SIZE)
			.queryProvider(customQueryProvider())
			.parameterValues(parameters)
			.build();
	}

	@Bean
	public PagingQueryProvider customQueryProvider() {
		Map<String, Order> sortKeys = new HashMap<>();
		sortKeys.put("name", Order.DESCENDING);

		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause("name, age, year");
		queryProvider.setFromClause("client");
		queryProvider.setWhereClause("name like :name");
		queryProvider.setSortKeys(sortKeys);

		return queryProvider;
	}

}
