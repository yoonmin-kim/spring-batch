package hello.batch.springbatch.scope;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class JobScope_StepScope_Configuration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean

	public Job scopeJob() {
		return jobBuilderFactory.get("scopeJob")
			.start(step1(null, null))
			.next(step2())
			.listener(new ScopeJobListener())
			.build();
	}

	@Bean
	@JobScope
	public Step step1(@Value("#{jobParameters['message']}") String message, @Value("#{jobExecutionContext['name']}") String name) {
		System.out.println("message = " + message);
		System.out.println("name = " + name);
		return stepBuilderFactory.get("step1()")
			.tasklet((stepContribution, chunkContext) -> RepeatStatus.FINISHED)
			.build();
	}

	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2()")
			.tasklet(tasklet(null))
			.listener(new ScopeStepListener())
			.build();
	}

	@Bean
	@StepScope
	public Tasklet tasklet(@Value("#{stepExecutionContext['name2']}") String name2) {
		System.out.println("name2 = " + name2);
		return (stepContribution, chunkContext) -> RepeatStatus.FINISHED;
	}
}
