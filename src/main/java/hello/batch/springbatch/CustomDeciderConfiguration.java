package hello.batch.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class CustomDeciderConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job deciderJob() {
		return jobBuilderFactory.get("deciderJob")
			.start(step1())
			.next(decider())
			.from(decider()).on("ODD").to(oddStep())
			.from(decider()).on("EVEN").to(evenStep())
			.end()
			.build();
	}

	@Bean
	public JobExecutionDecider decider() {
		return new CustomJobExecutionDecider();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
			.tasklet((stepContribution, chunkContext) -> null)
			.build();
	}

	@Bean
	public Step oddStep() {
		return stepBuilderFactory.get("oddStep")
			.tasklet((stepContribution, chunkContext) -> null)
			.build();
	}

	@Bean
	public Step evenStep() {
		return stepBuilderFactory.get("evenStep")
			.tasklet((stepContribution, chunkContext) -> null)
			.build();
	}
}
