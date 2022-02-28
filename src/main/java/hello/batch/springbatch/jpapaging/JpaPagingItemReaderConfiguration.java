package hello.batch.springbatch.jpapaging;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.batch.springbatch.flatfile.Client;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class JpaPagingItemReaderConfiguration {

	public static final int CHUNK_SIZE = 10;
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;

	@Bean
	public Job jpaPagingJob() {
		return jobBuilderFactory.get("jpaPagingJob")
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
		return new JpaPagingItemReaderBuilder<Client>()
			.name("jpaPagingItemReader")
			.pageSize(CHUNK_SIZE)
			.entityManagerFactory(entityManagerFactory)
			.queryString("select c from Client c")
			.build();
	}

	@Bean
	public ItemWriter<? super Client> itemWriter() {
		return items -> {
			for (Client client : items) {
				System.out.println("client = " + client);
			}
		};
	}

}
