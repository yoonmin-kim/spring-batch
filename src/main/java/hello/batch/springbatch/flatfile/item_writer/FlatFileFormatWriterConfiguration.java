package hello.batch.springbatch.flatfile.item_writer;

import java.util.Arrays;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import hello.batch.springbatch.flatfile.Client;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class FlatFileFormatWriterConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job flatFileFormatJob() {
		return jobBuilderFactory.get("flatFileFormatJob")
			.incrementer(new RunIdIncrementer())
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

		ListItemReader<Client> itemReader = new ListItemReader<>(
			Arrays.asList(new Client("user1", 29, "1994"),
				new Client("user2", 28, "1995"),
				new Client("user3", 27, "1996"))
		);

		return itemReader;
	}

	@Bean
	public ItemWriter<? super Client> itemWriter() {
		return new FlatFileItemWriterBuilder<Client>()
			.name("flatFileWriter")
			.resource(new FileSystemResource("C:\\study\\spring-batch\\src\\main\\resources\\client-format.txt"))
			.append(true)
			.formatted()
			.format("%-6s%-3d%-4s")
			.names(new String[]{"name", "age", "year"})
			.build();
	}
}
