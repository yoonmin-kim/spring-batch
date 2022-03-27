package hello.batch.springbatch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

public class CustomJobAnnotationListener{

	@BeforeJob
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("Job is Started");
		System.out.println("jobExecution.getJobInstance().getJobName() = " + jobExecution.getJobInstance().getJobName());

	}

	@AfterJob
	public void afterJob(JobExecution jobExecution) {
		long startTime = jobExecution.getStartTime().getTime();
		long endTime = jobExecution.getEndTime().getTime();
		System.out.println("Total Job time : " + (endTime - startTime));
	}
}
