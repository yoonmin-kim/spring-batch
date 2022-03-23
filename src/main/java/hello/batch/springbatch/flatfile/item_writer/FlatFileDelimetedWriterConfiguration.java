package hello.batch.springbatch.flatfile.item_writer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import hello.batch.springbatch.flatfile.Client;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// @Configuration
public class FlatFileDelimetedWriterConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job flatFileDelimetedJob() {
		return jobBuilderFactory.get("flatFileDelimetedJob")
			.start(step1())
			.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
			.<Client, Client>chunk(1)
			.reader(itemReader())
			.writer(itemWriter())
			.build();

	}

	@Bean
	public ItemReader<? extends Client> itemReader() {

		List<Client> items = Arrays.asList(new Client("user1", 29, "1994"),
			new Client("user2", 28, "1995"),
			new Client("user3", 27, "1996"));

		ListItemReader<Client> itemsList = new ListItemReader<>(items);

		return itemsList;
	}

	@Bean
	public ItemWriter<? super Client> itemWriter() {
		return new FlatFileItemWriterBuilder<Client>()
			.name("flatfileWriter")
			.resource(new FileSystemResource("C:\\study\\spring-batch\\src\\main\\resources\\client-flat.txt"))
			.delimited()
			.delimiter("|")
			.names(new String[] {"name", "age", "year"})
			.build();
	}
}
