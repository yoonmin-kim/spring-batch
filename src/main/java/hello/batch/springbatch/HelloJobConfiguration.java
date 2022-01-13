package hello.batch.springbatch;

import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class HelloJobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job helloJob() {
		return jobBuilderFactory.get("helloJob")
			.start(helloStep1())
			.next(helloStep2())
			.build();
	}

	@Bean
	public Step helloStep1() {
		return stepBuilderFactory.get("helloStep1")
			.tasklet((stepContribution, chunkContext) -> {
				System.out.println(" ====================");
				System.out.println("Hello Spring Batch!!");
				System.out.println(" ====================");
				JobParameters jobParameters = stepContribution.getStepExecution().getJobExecution().getJobParameters();
				jobParameters.getString("name");
				jobParameters.getLong("seq");
				jobParameters.getDate("date");
				jobParameters.getDouble("age");

				Map<String, Object> jobParameters1 = chunkContext.getStepContext().getJobParameters();
				return RepeatStatus.FINISHED;
			})
			.build();
	}

	@Bean
	public Step helloStep2() {
		return stepBuilderFactory.get("helloStep2")
			.tasklet(new CustomTasklet())
			.build();
	}
}
