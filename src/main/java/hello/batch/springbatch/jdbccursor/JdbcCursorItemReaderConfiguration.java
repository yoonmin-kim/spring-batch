package hello.batch.springbatch.jdbccursor;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.batch.springbatch.flatfile.Client;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class JdbcCursorItemReaderConfiguration {

	public static final int CHUNK_SIZE = 10;
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final DataSource dataSource;

	@Bean
	public Job jdbcCursorJob() {
		return jobBuilderFactory.get("jdbcCursorJob")
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
	public ItemReader<? extends Client> itemReader() {
		return new JdbcCursorItemReaderBuilder<Client>()
			.name("jdbcCursorItemReader")
			.fetchSize(CHUNK_SIZE)
			.dataSource(dataSource)
			.beanRowMapper(Client.class)
			.sql("SELECT name, age, year FROM CLIENT WHERE name LIKE ? ORDER BY year")
			.queryArguments("user%")
			.build();
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
