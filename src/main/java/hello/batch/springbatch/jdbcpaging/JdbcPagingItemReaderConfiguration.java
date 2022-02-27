package hello.batch.springbatch.jdbcpaging;

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
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.batch.springbatch.flatfile.Client;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class JdbcPagingItemReaderConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final DataSource dataSource;

	@Bean
	public Job jdbcPagingJob() throws Exception {
		return jobBuilderFactory.get("jdbcPagingJob")
			.start(step1())
			.build();
	}

	@Bean
	public Step step1() throws Exception {
		return stepBuilderFactory.get("step1")
			.<Client, Client>chunk(2)
			.reader(itemReader())
			.writer(itemWriter())
			.build();
	}

	@Bean
	public ItemReader<? extends Client> itemReader() throws Exception {

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("name", "user%");

		return new JdbcPagingItemReaderBuilder<Client>()
			.name("jdbcPagingReader")
			.dataSource(dataSource)
			.pageSize(2)
			.beanRowMapper(Client.class)
			.queryProvider(customQueryProvider())
			.parameterValues(parameters)
			.build();
	}

	@Bean
	public PagingQueryProvider customQueryProvider() throws Exception {
		SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
		queryProvider.setDataSource(dataSource);
		queryProvider.setSelectClause("name, age, year");
		queryProvider.setFromClause("client");
		queryProvider.setWhereClause("name like :name");

		Map<String, Order> sortKeys = new HashMap<>();
		sortKeys.put("age", Order.ASCENDING);
		queryProvider.setSortKeys(sortKeys);

		return queryProvider.getObject();
	}

	@Bean
	public ItemWriter<? super Client> itemWriter() {
		return items -> {
			for (Client item : items) {
				System.out.println("item = " + item);
			}
		};
	}

}
