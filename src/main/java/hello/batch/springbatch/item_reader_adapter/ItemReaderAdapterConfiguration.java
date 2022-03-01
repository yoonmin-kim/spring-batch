package hello.batch.springbatch.item_reader_adapter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class ItemReaderAdapterConfiguration {

	public static final int CHUNK_SIZE = 10;
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job adapterJob() {
		return jobBuilderFactory.get("adapterJob")
			.start(step1())
			.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
			.<String, String>chunk(CHUNK_SIZE)
			.reader(itemReader())
			.writer(itemWriter())
			.build();
	}

	@Bean
	public ItemReader<String> itemReader() {
		ItemReaderAdapter<String> adapter = new ItemReaderAdapter<>();
		adapter.setTargetObject(customService());
		adapter.setTargetMethod("customRead");
		return null;
	}

	@Bean
	public CustomService<String> customService() {
		return new CustomService<String>();
	}

	@Bean
	public ItemWriter<? super String> itemWriter() {
		return items -> {
			for (String item : items) {
				System.out.println("item = " + item);
			}
		};
	}
}
