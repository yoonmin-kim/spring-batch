package hello.batch.springbatch.chunk;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// @Configuration
public class ChunkConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job chunkJob() {
		return jobBuilderFactory.get("chunkJob")
			.start(step1())
			.next(step2())
			.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
			.<String, String>chunk(3)
			.reader(new ListItemReader<>(Arrays.asList("item1", "item2", "item3")))
			.processor(new ItemProcessor<String, String>() {
				@Override
				public String process(String s) throws Exception {
					System.out.println("s = " + s);
					return "my"+s;
				}
			})
			.writer(new ItemWriter<String>() {
				@Override
				public void write(List<? extends String> list) throws Exception {
					System.out.println("list = " + list);
				}
			})
			.build();
	}

	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2")
			.tasklet((stepContribution, chunkContext) -> RepeatStatus.FINISHED)
			.build();

	}
}
