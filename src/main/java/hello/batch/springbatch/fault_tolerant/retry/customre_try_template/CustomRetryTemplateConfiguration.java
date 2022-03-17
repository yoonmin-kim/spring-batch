package hello.batch.springbatch.fault_tolerant.retry.customre_try_template;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import hello.batch.springbatch.fault_tolerant.retry.RetryAbleException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class CustomRetryTemplateConfiguration {
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
			.processor(customProcessor())
			.writer(items -> System.out.println("items = " + items))
			.faultTolerant()
			// .skip(RetryAbleException.class)
			// .skipLimit(2)
			// .retryPolicy(retryPolicy())
			// .retry(RetryAbleException.class)
			// .retryLimit(2)
			.build();
	}

	@Bean
	public ItemProcessor<? super String, String> customProcessor() {
		return new CustomItemProcessor();
	}

	@Bean
	public RetryTemplate retryTemplate() {
		Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>();
		retryableExceptions.put(RetryAbleException.class, true);

		RetryPolicy retryPolicy = new SimpleRetryPolicy(2, retryableExceptions);
		FixedBackOffPolicy backOffPolicy =  new FixedBackOffPolicy();
		backOffPolicy.setBackOffPeriod(2000L);

		RetryTemplate retryTemplate = new RetryTemplate();
		retryTemplate.setRetryPolicy(retryPolicy);
		retryTemplate.setBackOffPolicy(backOffPolicy);
		return retryTemplate;
	}
}
