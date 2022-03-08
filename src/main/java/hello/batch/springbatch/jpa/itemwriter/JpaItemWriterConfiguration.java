package hello.batch.springbatch.jpa.itemwriter;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.batch.springbatch.flatfile.Client;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class JpaItemWriterConfiguration {

	public static final int CHUNK_SIZE = 5;
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;

	@Bean
	public Job jpaWriterJob() {
		return jobBuilderFactory.get("jpaWriterJob")
			.incrementer(new RunIdIncrementer())
			.start(step1())
			.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
			.<Client, ClientEntity>chunk(CHUNK_SIZE)
			.reader(customReader())
			.processor(customProcessor())
			.writer(customWriter())
			.build();
	}

	@Bean
	public ItemWriter<? super ClientEntity> customWriter() {
		return new JpaItemWriterBuilder<ClientEntity>()
			.usePersist(true)
			.entityManagerFactory(entityManagerFactory)
			.build();
	}

	@Bean
	public ItemProcessor<Client, ClientEntity> customProcessor() {
		return new CustomProcessor();
	}

	@Bean
	public ItemReader<? extends Client> customReader() {

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("name", "user%");

		return new JpaCursorItemReaderBuilder<Client>()
			.name("japReader")
			.entityManagerFactory(entityManagerFactory)
			.queryProvider(myQueryProvider())
			.parameterValues(parameters)
			.build();
	}

	@Bean
	public JpaQueryProvider myQueryProvider() {

		JpaNativeQueryProvider<Client> queryProvider = new JpaNativeQueryProvider<>();
		queryProvider.setSqlQuery("select name, age, year from client where name like :name");
		queryProvider.setEntityClass(Client.class);
		return queryProvider;
	}

}
