package hello.batch.springbatch.item_processor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class CompositeItemProcessorConfiguration {

	public static final int CHUNK_SIZE = 5;
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job compositeProcessorJob() {
		return jobBuilderFactory.get("compositeProcessorJob")
			.start(step1())
			.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
			.<String, String>chunk(CHUNK_SIZE)
			.reader(customReader())
			.processor(customProcessor())
			.writer(customWriter())
			.build();
	}

	@Bean
	public ItemProcessor<? super String, String> customProcessor() {
		List itemProcessors = new ArrayList<>();
		itemProcessors.add(new CustomItemProcessor());
		itemProcessors.add(new CustomItemProcessor2());

		return new CompositeItemProcessorBuilder<String, String>()
			.delegates(itemProcessors)
			.build();
	}

	@Bean
	public ItemWriter<? super String> customWriter() {
		return items -> {
			for (String item : items) {
				System.out.println("item = " + item);
			}
		};
	}

	@Bean
	public ItemReader<String> customReader() {
		return new ItemReader<>() {
			int cnt = 0;

			@Override
			public String read() {
				++cnt;
				return cnt > 9 ? null : "item";
			}
		};
	}
}
