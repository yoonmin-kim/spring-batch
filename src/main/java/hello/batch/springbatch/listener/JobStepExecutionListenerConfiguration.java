package hello.batch.springbatch.listener;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class JobStepExecutionListenerConfiguration {

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
}
