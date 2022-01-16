package hello.batch.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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
			.next(helloStep3())
			.next(helloStep4())
			.build();
	}

	@Bean
	public Step helloStep1() {
		return stepBuilderFactory.get("helloStep1")
			.tasklet(new CustomTasklet1())
			.build();
	}

	@Bean
	public Step helloStep2() {
		return stepBuilderFactory.get("helloStep2")
			.tasklet(new CustomTasklet2())
			.build();
	}

	@Bean
	public Step helloStep3() {
		return stepBuilderFactory.get("helloStep3")
			.tasklet(new CustomTasklet3())
			.build();

	}

	@Bean
	public Step helloStep4() {
		return stepBuilderFactory.get("helloStep4")
			.tasklet(new CustomTasklet4())
			.build();
	}

}
