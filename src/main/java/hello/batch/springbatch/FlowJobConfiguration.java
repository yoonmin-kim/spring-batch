package hello.batch.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class FlowJobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job flowJob() {
		return jobBuilderFactory.get("flowJob")
			.start(step1())
			.on("COMPLETED").stop()
			.from(step1())
			.on("FAILED").to(step2())
			.next(step3())
			.end()
			.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
			.tasklet(new Tasklet() {
				@Override
				public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws
					Exception {
					// throw new RuntimeException("");
					return RepeatStatus.FINISHED;
				}
			})
			.build();
	}

	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2")
			.tasklet(new Tasklet() {
				@Override
				public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws
					Exception {
					return RepeatStatus.FINISHED;
				}
			})
			.build();
	}

	@Bean
	public Step step3() {
		return stepBuilderFactory.get("step3")
			.tasklet(new Tasklet() {
				@Override
				public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws
					Exception {
					return RepeatStatus.FINISHED;
				}
			})
			.build();
	}
}
