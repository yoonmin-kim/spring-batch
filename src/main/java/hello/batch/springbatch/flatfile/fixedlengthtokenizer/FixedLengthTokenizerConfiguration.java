package hello.batch.springbatch.flatfile.fixedlengthtokenizer;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import hello.batch.springbatch.flatfile.Client;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class FixedLengthTokenizerConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job fixedLengthTokenizerJob() {
		return jobBuilderFactory.get("fltJob")
			.start(step1())
			.next(step2())
			.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
			.chunk(5)
			.reader(itemReader())
			.writer(new ItemWriter() {
				@Override
				public void write(List items) throws Exception {
					System.out.println("items = " + items);
				}
			})
			.build();
	}

	@Bean
				public ItemReader<?> itemReader() {
		return new FlatFileItemReaderBuilder<Client>()
			.name("flatfile")
			.targetType(Client.class)
			.resource(new FileSystemResource("C:\\study\\spring-batch\\src\\main\\resources\\client.txt"))
			.linesToSkip(1)
			.fixedLength()
			.addColumns(new Range(1,5))
			.addColumns(new Range(6,9))
			.addColumns(new Range(10,11))
			.names("name", "year", "age")
			.build()
			;
	}

	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2")
			.tasklet((stepContribution, chunkContext) -> RepeatStatus.FINISHED)
			.build();
	}
}
