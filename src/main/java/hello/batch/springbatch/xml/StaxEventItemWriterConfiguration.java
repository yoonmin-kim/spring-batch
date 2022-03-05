package hello.batch.springbatch.xml;

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
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;

import hello.batch.springbatch.flatfile.Client;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class StaxEventItemWriterConfiguration {

	public static final int CHUNK_SIZE = 5;
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final DataSource dataSource;

	@Bean
	public Job staxWriterJob() {
		return jobBuilderFactory.get("staxWriterJob")
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

		return new StaxEventItemWriterBuilder<Client>()
			.name("staxItemWriter")
			.resource(new FileSystemResource("C:\\study\\spring-batch\\src\\main\\resources\\client-stax.xml"))
			.rootTagName("client")
			.marshaller(customMarshaller())
			.build();
	}

	@Bean
	public Marshaller customMarshaller() {

		Map<String, Class<?>> aliases = new HashMap<>();
		aliases.put("name", String.class);
		aliases.put("age", Integer.class);
		aliases.put("year", String.class);

		XStreamMarshaller marshaller = new XStreamMarshaller();
		marshaller.setAliases(aliases);

		return marshaller;
	}

	@Bean
	public ItemReader<? extends Client> itemReader() {

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("name", "user%");

		return new JdbcPagingItemReaderBuilder<Client>()
			.name("jdbcPagingReader")
			.dataSource(dataSource)
			.pageSize(CHUNK_SIZE)
			.beanRowMapper(Client.class)
			.parameterValues(parameters)
			.queryProvider(queryProvider())
			.build();
	}

	@Bean
	public PagingQueryProvider queryProvider() {

		Map<String, Order> sortKeys = new HashMap<>();

		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause("name, age, year");
		queryProvider.setFromClause("client");
		queryProvider.setWhereClause("name like :name");

		sortKeys.put("name", Order.DESCENDING);
		queryProvider.setSortKeys(sortKeys);
		return queryProvider;
	}
}
