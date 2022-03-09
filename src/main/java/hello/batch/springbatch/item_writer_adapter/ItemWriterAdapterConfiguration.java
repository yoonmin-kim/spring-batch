package hello.batch.springbatch.item_writer_adapter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class ItemWriterAdapterConfiguration {

	public static final int CHUNK_SIZE = 5;
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job writerAdapterJob() {
		return jobBuilderFactory.get("writerAdapterJob")
			.start(step1())
			.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
			.<String, String>chunk(CHUNK_SIZE)
			.reader(customReader())
			.writer(customWriter())
			.build();
	}

	@Bean
	public ItemWriter<? super String> customWriter() {

		ItemWriterAdapter<String> writerAdapter = new ItemWriterAdapter<>();
		writerAdapter.setTargetObject(legacyWriter());
		writerAdapter.setTargetMethod("customWrite");
		return writerAdapter;
	}

	@Bean
	public LegacyWriter legacyWriter() {
		return new LegacyWriter();
	}

	@Bean
	public ItemReader<String> customReader() {
		return new ItemReader<>() {
			int cnt = 0;

			@Override
			public String read() {
				return cnt > 10 ? null : "item" + cnt++;
			}
		};
	}
}
