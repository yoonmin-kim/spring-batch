package hello.batch.springbatch.listener;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class JobStepExecutionListenerConfiguration {

	public static final int CHUNK_SIZE = 5;
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final CustomStepListener customStepListener;

	@Bean
	public Job listenerJob() {
		return jobBuilderFactory.get("listenerJob")
			.start(step1())
			.next(step2())
			.listener(new CustomJobListener())
			// .listener(new CustomJobAnnotationListener())
			.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
			.tasklet((stepContribution, chunkContext) -> RepeatStatus.FINISHED)
			.listener(customStepListener)
			.build();
	}

	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2")
			.tasklet((stepContribution, chunkContext) -> RepeatStatus.FINISHED)
			.listener(customStepListener)
			.build();
	}

	@Bean
	public Step step3() {
		return stepBuilderFactory.get("step3")
			.<String, String>chunk(CHUNK_SIZE)
			.listener(new CustomChunkListener())
			.listener(new CustomReadListener())
			.listener(new CustomProcessListener())
			.listener(new CustomWriteListener())
			.reader(new ItemReader<>() {
				int cnt = 0;

				@Override
				public String read() {
					cnt++;
					return cnt > 20 ? null : "item" + cnt;
				}
			})
			.processor((ItemProcessor<String, String>)item -> item.toUpperCase())
			.writer(items -> {
				for (String item : items) {
					System.out.println("item = " + item);
				}
			})
			.build();
	}

}
