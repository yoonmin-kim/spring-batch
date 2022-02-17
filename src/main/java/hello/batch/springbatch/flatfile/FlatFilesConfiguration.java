package hello.batch.springbatch.flatfile;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class FlatFilesConfiguration {
	
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job flatFileJob() {
		return jobBuilderFactory.get("flatFileJob")
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
	public ItemReader itemReader() {
		DefaultLineMapper<Client> lineMapper = new DefaultLineMapper<>();
		lineMapper.setFieldSetMapper(new ClientFieldSetMapper());
		lineMapper.setLineTokenizer(new DelimitedLineTokenizer());

		FlatFileItemReader itemReader = new FlatFileItemReader();
		itemReader.setResource(new ClassPathResource("/client.csv"));
		itemReader.setLineMapper(lineMapper);
		itemReader.setLinesToSkip(1);

		return itemReader;
	}

	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2")
			.tasklet((stepContribution, chunkContext) -> RepeatStatus.FINISHED)
			.build();
	}
}
