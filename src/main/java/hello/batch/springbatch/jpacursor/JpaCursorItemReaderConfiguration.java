package hello.batch.springbatch.jpacursor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.batch.springbatch.flatfile.Client;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class JpaCursorItemReaderConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;

	@Bean
	public Job jpaItemJob() {
		return jobBuilderFactory.get("jpaItemJob")
			.start(step1())
			.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
			.<Client, Client>chunk(5)
			.reader(itemReader())
			.writer(itemWriter())
			.build();
	}

	@Bean
	public ItemReader<? extends Client> itemReader() {

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("name", "user");

		return new JpaCursorItemReaderBuilder<Client>()
			.name("jpaCursorItemReader")
			.entityManagerFactory(entityManagerFactory)
			.queryString("select c from Client c from c.name like :name")
			.parameterValues(parameters)
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
