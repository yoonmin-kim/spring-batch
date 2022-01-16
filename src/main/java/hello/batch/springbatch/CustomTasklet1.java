package hello.batch.springbatch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class CustomTasklet1 implements Tasklet {

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		System.out.println("customTasklet1 was executed");

		ExecutionContext jobExecutionContext = contribution.getStepExecution().getJobExecution().getExecutionContext();
		ExecutionContext stepExecutionContext = contribution.getStepExecution().getExecutionContext();

		String stepName1 = contribution.getStepExecution().getStepName();
		String jobName1 = contribution.getStepExecution().getJobExecution().getJobInstance().getJobName();
		String stepName2 = chunkContext.getStepContext().getStepName();
		String stepName3 = chunkContext.getStepContext().getStepExecution().getStepName();
		String jobName2 = chunkContext.getStepContext().getJobName();

		if (jobExecutionContext.get("jobName") == null) {
			jobExecutionContext.put("jobName", jobName1);
		}

		if (stepExecutionContext.get("stepName") == null) {
			stepExecutionContext.put("stepName", stepName1);
		}

		return RepeatStatus.FINISHED;
	}
}
