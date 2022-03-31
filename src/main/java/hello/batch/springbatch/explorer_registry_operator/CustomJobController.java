package hello.batch.springbatch.explorer_registry_operator;import java.util.Iterator;import java.util.Set;import org.springframework.batch.core.Job;import org.springframework.batch.core.JobExecution;import org.springframework.batch.core.JobParametersInvalidException;import org.springframework.batch.core.configuration.JobRegistry;import org.springframework.batch.core.explore.JobExplorer;import org.springframework.batch.core.job.SimpleJob;import org.springframework.batch.core.launch.JobExecutionNotRunningException;import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;import org.springframework.batch.core.launch.JobOperator;import org.springframework.batch.core.launch.NoSuchJobException;import org.springframework.batch.core.launch.NoSuchJobExecutionException;import org.springframework.batch.core.launch.support.SimpleJobOperator;import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;import org.springframework.batch.core.repository.JobRestartException;import org.springframework.web.bind.annotation.PostMapping;import org.springframework.web.bind.annotation.RequestBody;import org.springframework.web.bind.annotation.RestController;import lombok.RequiredArgsConstructor;@RequiredArgsConstructor@RestControllerpublic class CustomJobController {	private final JobExplorer jobExplorer;	private final JobRegistry jobRegistry;	private final JobOperator jobOperator;	@PostMapping	public String start(@RequestBody JobInfo jobInfo) throws		NoSuchJobException,		JobInstanceAlreadyExistsException,		JobParametersInvalidException {		for (Iterator<String> iterator = jobRegistry.getJobNames().iterator(); iterator.hasNext(); ) {			SimpleJob job = (SimpleJob)jobRegistry.getJob(iterator.next());			jobOperator.start(job.getName(), jobInfo.getId());		}		return "customJob Started!";	}	@PostMapping	public String stop() throws NoSuchJobExecutionException, JobExecutionNotRunningException {		for (Iterator<String> iterator = jobRegistry.getJobNames().iterator(); iterator.hasNext(); ) {			Set<JobExecution> runningJobExecutions = jobExplorer.findRunningJobExecutions(iterator.next());			for (JobExecution runningJobExecution : runningJobExecutions) {				jobOperator.stop(runningJobExecution.getId());			}		}		return "customJob Stopped!";	}	@PostMapping	public String restart() throws		JobParametersInvalidException,		JobRestartException,		JobInstanceAlreadyCompleteException,		NoSuchJobExecutionException,		NoSuchJobException {		for (Iterator<String> iterator = jobRegistry.getJobNames().iterator(); iterator.hasNext(); ) {			Set<JobExecution> runningJobExecutions = jobExplorer.findRunningJobExecutions(iterator.next());			for (JobExecution runningJobExecution : runningJobExecutions) {				jobOperator.restart(runningJobExecution.getId());			}		}		return "customJob Restarted!";	}}