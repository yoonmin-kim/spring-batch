package hello.batch.springbatch.fault_tolerant.retry;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class RetryConfiguration {

	public static final int CHUNK_SIZE = 5;
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job retryJob() {
		return jobBuilderFactory.get("retryJob")
			.start(step())
			.build();
	}

	@Bean
	public Step step() {
		return stepBuilderFactory.get("retryStep")
			.<String, String>chunk(CHUNK_SIZE)
			.reader(new ItemReader<>() {
				int cnt = 0;
				@Override
				public String read() {
					++cnt;
					return cnt > 20 ? null : String.valueOf(cnt);
				}
			})
			.processor(new ItemProcessor<String, String>() {
				@Override
				public String process(String item) {
					if ("6".equals(item) || "7".equals(item)) {
						throw new RetryAbleException("재시도 가능 Exception 발생!");
					}
					return item;
				}
			})
			.writer(items -> System.out.println("items = " + items))
			.faultTolerant()
			.retry(RetryAbleException.class)
			.retryLimit(2)
			.build();
	}
}
