package hello.batch.springbatch.fault_tolerant;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class FaultTolerantConfiguration {

	public static final int CHUNK_SIZE = 5;
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job faultTolerantJob() {
		return jobBuilderFactory.get("faultTolerantJob")
			.start(step())
			.build();
	}

	@Bean
	public Step step() {
		return stepBuilderFactory.get("faultTolerantStep")
			.<String, String>chunk(CHUNK_SIZE)
			.reader(new ItemReader<>() {
				int cnt = 0;

				@Override
				public String read() {
					++cnt;
					if (cnt == 1) {
						throw new IllegalArgumentException("This exception is skipped");
					}
					return cnt > 3 ? null : "item" + cnt;
				}
			})
			.writer(items -> {
				for (String item : items) {
					if (item.contains("1"))
						throw new IllegalStateException("This exception is retried");
					System.out.println("item = " + item);
				}
			})
			.faultTolerant()
			.skip(IllegalArgumentException.class)
			.skipLimit(2)
			.retry(IllegalStateException.class)
			.retryLimit(2)
			.build();
	}
}
