package hello.batch.springbatch.repeat;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatCallback;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.repeat.exception.SimpleLimitExceptionHandler;
import org.springframework.batch.repeat.policy.CompositeCompletionPolicy;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.batch.repeat.policy.TimeoutTerminationPolicy;
import org.springframework.batch.repeat.support.RepeatTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class RepeatConfiguration {

	public static final int CHUNK_SIZE = 5;
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job repeatJob() {
		return jobBuilderFactory.get("repeatJob")
			.start(repeatStep())
			.build();
	}

	@Bean
	public Step repeatStep() {
		return stepBuilderFactory.get("repeatStep")
			.<String, String>chunk(CHUNK_SIZE)
			.reader(itemReader())
			.processor(itemProcessor())
			.writer(itemWriter())
			.build();
	}

	@Bean
	public ItemProcessor<? super String, String> itemProcessor() {
		return new ItemProcessor<>() {

			RepeatTemplate repeatTemplate = new RepeatTemplate();

			@Override
			public String process(String item) {
				// repeatTemplate.setCompletionPolicy(new SimpleCompletionPolicy(CHUNK_SIZE));
				repeatTemplate.setExceptionHandler(new SimpleLimitExceptionHandler(2));

				CompositeCompletionPolicy completionPolicy = new CompositeCompletionPolicy();
				completionPolicy.setPolicies(new CompletionPolicy[] {
					new SimpleCompletionPolicy(CHUNK_SIZE), new TimeoutTerminationPolicy(3000L)
				});
				repeatTemplate.setCompletionPolicy(completionPolicy);

				repeatTemplate.iterate(new RepeatCallback() {
					@Override
					public RepeatStatus doInIteration(RepeatContext repeatContext) throws Exception {
						System.out.println(">>> repeat");
						return RepeatStatus.CONTINUABLE;
					}
				});

				return item;
			}
		};
	}

	@Bean
	public ItemWriter<? super String> itemWriter() {
		return items -> {
			for (String item : items) {
				System.out.println("item = " + item);
			}
		};
	}

	@Bean
	public ItemReader<String> itemReader() {
		return new ItemReader<>() {
			int cnt = 0;

			@Override
			public String read() {
				++cnt;
				return cnt > 3 ? null : ("item" + cnt);
			}
		};
	}
}
