package hello.batch.springbatch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;
import org.springframework.batch.core.step.job.JobParametersExtractor;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class JobStepConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job parentJob() {
		return jobBuilderFactory.get("parentJob")
			.start(jobStep(null))
			.next(step2())
			.build();
	}

	@Bean
	public Step jobStep(JobLauncher jobLauncher) {
		return stepBuilderFactory.get("jobStep")
			.job(childJob())
			.launcher(jobLauncher)
			.parametersExtractor(jobParametersExtractor())
			.listener(new StepExecutionListener() {
				@Override
				public void beforeStep(StepExecution stepExecution) {
					stepExecution.getExecutionContext().put("name", "user1");
				}
				@Override
				public ExitStatus afterStep(StepExecution stepExecution) {
					return null;
				}
			})
			.build();
	}

	private JobParametersExtractor jobParametersExtractor() {
		DefaultJobParametersExtractor extractor = new DefaultJobParametersExtractor();
		extractor.setKeys(new String[]{"name"});
		return extractor;
	}

	@Bean
	public Job childJob() {
		return jobBuilderFactory.get("childJob")
			.start(step1())
			.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
			.tasklet(new Tasklet() {
				@Override
				public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
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
				public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
					return RepeatStatus.FINISHED;
				}
			})
			.build();
	}

}
