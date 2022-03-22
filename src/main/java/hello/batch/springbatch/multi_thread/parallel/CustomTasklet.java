package hello.batch.springbatch.multi_thread.parallel;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class CustomTasklet implements Tasklet {

	private int count = 0;
	private Object object = new Object();

	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
		synchronized (object) {
			for (int i = 0; i < 1000000000; i++) {
				count++;
			}

			System.out.printf("%s cnt: %d\n",Thread.currentThread().getName(), count);
		}

		return RepeatStatus.FINISHED;
	}
}
