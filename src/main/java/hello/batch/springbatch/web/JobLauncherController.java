package hello.batch.springbatch.web;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// @RestController
public class JobLauncherController {

	private final Job job;
	private final JobLauncher jobLauncher;
	private final BasicBatchConfigurer basicBatchConfigurer;

	@PostMapping("/sync-batch")
	public String syncLaunch(@RequestBody Member member) throws	JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException,
		JobRestartException {

		JobParameters jobParameters = new JobParametersBuilder()
			.addString("id", member.getId())
			.addDate("date", new Date())
			.toJobParameters();

		jobLauncher.run(job, jobParameters);
		return "batch completed";
	}

	@PostMapping("/async-batch")
	public String asyncLaunch(@RequestBody Member member) throws	JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException,
		JobRestartException {

		JobParameters jobParameters = new JobParametersBuilder()
			.addString("id", member.getId())
			.addDate("date", new Date())
			.toJobParameters();
		SimpleJobLauncher jobLauncher = (SimpleJobLauncher)basicBatchConfigurer.getJobLauncher();
		jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
		jobLauncher.run(job, jobParameters);
		return "batch completed";
	}
}
