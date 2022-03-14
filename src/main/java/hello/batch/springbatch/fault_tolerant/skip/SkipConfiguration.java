package hello.batch.springbatch.fault_tolerant.skip;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.skip.LimitCheckingItemSkipPolicy;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SkipConfiguration {

	public static final int CHUNK_SIZE = 5;
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job skipJob() {
		return jobBuilderFactory.get("skipJob")
			.start(step())
			.build();
	}

	@Bean
	public Step step() {
		return stepBuilderFactory.get("skipStep")
			.<String, String>chunk(CHUNK_SIZE)
			.reader(new ItemReader<String>() {
				int cnt = 0;
				@Override
				public String read() {
					++cnt;
					return cnt > 20 ? null : String.valueOf(cnt);
				}
			})
			.processor(skipProcessor())
			.writer(skipWriter())
			.faultTolerant()
			.skipPolicy(customSkipPolicy())
			.build();
	}

	@Bean
	public SkipPolicy customSkipPolicy() {

		Map<Class<? extends Throwable>, Boolean> skippableExceptionMap = new HashMap<>();
		skippableExceptionMap.put(SkipAbleException.class, true);

		return new LimitCheckingItemSkipPolicy(4, skippableExceptionMap);
	}

	@Bean
	public ItemWriter<String> skipWriter() {
		return new SkipWriter();
	}

	@Bean
	public ItemProcessor<String, String> skipProcessor() {
		return new SkipProcessor();
	}
}
