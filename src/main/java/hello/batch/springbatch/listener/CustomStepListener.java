package hello.batch.springbatch.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class CustomStepListener implements StepExecutionListener {

	@Override
	public void beforeStep(StepExecution stepExecution) {
		System.out.println("stepExecution.getStepName() = " + stepExecution.getStepName());
		stepExecution.getExecutionContext().put("name", "user1");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		ExitStatus exitStatus = stepExecution.getExitStatus();
		System.out.println("exitStatus = " + exitStatus);
		BatchStatus batchStatus = stepExecution.getStatus();
		System.out.println("batchStatus = " + batchStatus);
		System.out.println(
			"stepExecution.getExecutionContext().get(\"name\") = " + stepExecution.getExecutionContext().get("name"));

		return ExitStatus.COMPLETED;
	}
}
