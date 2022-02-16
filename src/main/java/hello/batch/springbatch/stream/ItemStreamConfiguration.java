package hello.batch.springbatch.stream;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.batch.springbatch.reader_processor_writer.CustomItemReader;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class ItemStreamConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job streamJob() {
		return jobBuilderFactory.get("streamJob")
			.start(step1())
			.next(step2())
			.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
			.<String, String>chunk(5)
			.reader(itemReader())
			.writer(itemWriter())
			.build();
	}

	@Bean
	public ItemStreamWriter<? super String> itemWriter() {
		return new CustomItemStreamWriter();
	}

	public CustomItemStreamReader itemReader() {
		List<String> items = new ArrayList<>(10);
		for (int i = 0; i < 10; i++) {
			items.add(String.valueOf(i));
		}
		return new CustomItemStreamReader(items);
	}

	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2")
			.tasklet((stepContribution, chunkContext) -> null)
			.build();
	}
}
