package hello.batch.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// @Configuration
public class HelloJobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	// private final JobExecutionListener jobRepositoryListener;

	@Bean
	public Job helloJob() {
		return jobBuilderFactory.get("helloJob")
			.incrementer(new RunIdIncrementer())
			.start(helloStep1())
			.next(helloStep2())
			.next(helloStep3())
			.next(helloStep4())
			// .validator(new MyJobValidator())
			// .validator(new DefaultJobParametersValidator(new String[]{"name"},new String[]{"count"}))
			// .preventRestart()
			.build();
	}
	@Bean
	public Job job() {
		return jobBuilderFactory.get("job")
			.start(helloStep1())
			.next(helloStep2())
			.build();
	}

	@Bean
	public Step helloStep1() {
		return stepBuilderFactory.get("helloStep1")
			.tasklet(new Tasklet() {
				@Override
				public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws
					Exception {
					// throw new RuntimeException("");
					// Thread.sleep(3000);
					return RepeatStatus.FINISHED;
				}
			})
			.allowStartIfComplete(true)
			.build();
	}

	@Bean
	public Step helloStep2() {
		return stepBuilderFactory.get("helloStep2")
			.tasklet(new Tasklet() {
				@Override
				public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws
					Exception {
					throw new RuntimeException("");
					// return RepeatStatus.FINISHED;
				}
			})
			.startLimit(3)
			.build();
	}

	@Bean
	public Step helloStep3() {
		return stepBuilderFactory.get("helloStep3")
			.partitioner(helloStep1())
			.build();
	}

	@Bean
	public Step helloStep4() {
		return stepBuilderFactory.get("helloStep4")
			.job(job())
			.build();
	}

	@Bean
	public Flow helloFlow1() {
		FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("helloFlow1");
		return flowBuilder.start(helloStep3())
			.end();
	}

}
