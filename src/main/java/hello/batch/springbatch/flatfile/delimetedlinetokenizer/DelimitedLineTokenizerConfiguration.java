package hello.batch.springbatch.flatfile.delimetedlinetokenizer;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import hello.batch.springbatch.flatfile.Client;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class DelimitedLineTokenizerConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job delimitedLineTokenizerJob() {
		return jobBuilderFactory.get("dltJob")
			.start(step1())
			.next(step2())
			.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
			.chunk(5)
			.reader(itemReader())
			.writer((ItemWriter)items -> System.out.println("items = " + items))
			.build();
	}

	@Bean
	public ItemReader itemReader() {
		return new FlatFileItemReaderBuilder<Client>()
			.name("flatFile")
			.resource(new ClassPathResource("client.csv"))
			.fieldSetMapper(new BeanWrapperFieldSetMapper<>())
			.targetType(Client.class)
			.linesToSkip(1)
			.delimited().delimiter(",")
			.names("name", "age", "year")
			.build();
	}

	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2")
			.tasklet((stepContribution, chunkContext) -> RepeatStatus.FINISHED)
			.build();
	}
}
