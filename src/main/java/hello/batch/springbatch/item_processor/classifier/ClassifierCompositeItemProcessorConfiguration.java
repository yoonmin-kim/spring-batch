package hello.batch.springbatch.item_processor.classifier;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class ClassifierCompositeItemProcessorConfiguration {

	public static final int CHUNK_SIZE = 5;
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job compositeProcessorJob() {
		return jobBuilderFactory.get("classifierProcessorJob")
			.start(step1())
			.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
			.<ProcessorInfo, ProcessorInfo>chunk(CHUNK_SIZE)
			.reader(customReader())
			.processor(customProcessor())
			.writer(customWriter())
			.build();
	}

	@Bean
	public ItemProcessor<ProcessorInfo, ProcessorInfo> customProcessor() {
		ClassifierCompositeItemProcessor<ProcessorInfo, ProcessorInfo> itemProcessor = new ClassifierCompositeItemProcessor<>();
		ProcessorClassifier<ProcessorInfo, ItemProcessor<?, ? extends ProcessorInfo>> classifier = new ProcessorClassifier<>();
		Map<Integer, ItemProcessor<ProcessorInfo, ProcessorInfo>> processorMap = new HashMap<>();
		processorMap.put(1, new CustomItemProcessor1());
		processorMap.put(2, new CustomItemProcessor3());
		processorMap.put(3, new CustomItemProcessor1());
		classifier.setProcessorMap(processorMap);
		itemProcessor.setClassifier(classifier);
		return itemProcessor;
	}

	@Bean
	public ItemWriter<? super ProcessorInfo> customWriter() {
		return items -> {
			for (ProcessorInfo item : items) {
				System.out.println("item = " + item);
			}
		};
	}

	@Bean
	public ItemReader<ProcessorInfo> customReader() {
		return new ItemReader<>() {
			int cnt = 0;

			@Override
			public ProcessorInfo read() {
				++cnt;
				ProcessorInfo processorInfo = ProcessorInfo.builder().id(cnt).build();
				return cnt > 3 ? null : processorInfo;
			}
		};
	}
}
