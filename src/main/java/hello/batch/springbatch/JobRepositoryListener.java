package hello.batch.springbatch;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JobRepositoryListener implements JobExecutionListener {

	private final JobRepository jobRepository;

	@Override
	public void beforeJob(JobExecution jobExecution) {

	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		String jobName = jobExecution.getJobInstance().getJobName();

		JobParameters jobParameters = new JobParametersBuilder()
										.addString("requestDate", "20220117")
										.toJobParameters();

		JobExecution lastJobExecution = jobRepository.getLastJobExecution(jobName, jobParameters);
		if (lastJobExecution != null) {
			for (StepExecution execution : lastJobExecution.getStepExecutions()) {
				BatchStatus batchStatus = execution.getStatus();
				System.out.println("batchStatus = " + batchStatus);
				ExitStatus exitStatus = execution.getExitStatus();
				System.out.println("exitStatus = " + exitStatus);
				String stepName = execution.getStepName();
				System.out.println("stepName = " + stepName);
			}
		}
	}
}
