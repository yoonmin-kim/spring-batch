package hello.batch.springbatch.multi_thread.parallel;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SpiltTaskConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job splitJob() {
		return jobBuilderFactory.get("splitJob")
			.start(flow1())
			.split(taskExecutor()).add(flow2())
			.end()
			.build();
	}

	private TaskExecutor taskExecutor() {

		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(2);
		taskExecutor.setMaxPoolSize(8);
		taskExecutor.setThreadNamePrefix("split-thread");
		return taskExecutor;
	}

	@Bean
	public Flow flow1() {

		TaskletStep step1 = stepBuilderFactory.get("step1")
			.tasklet(new CustomTasklet())
			.build();

		return new FlowBuilder<Flow>("flow1")
			.start(step1)
			.build();
	}

	@Bean
	public Flow flow2() {
		TaskletStep step2 = stepBuilderFactory.get("step2")
			.tasklet(new CustomTasklet())
			.build();
		TaskletStep step3 = stepBuilderFactory.get("step3")
			.tasklet(new CustomTasklet())
			.build();
		return new FlowBuilder<Flow>("flow2")
			.start(step2)
			.next(step3)
			.build();
	}

}
