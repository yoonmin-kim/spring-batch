package hello.batch.springbatch.item_processor.classifier;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor2 implements ItemProcessor<ProcessorInfo, ProcessorInfo> {

	@Override
	public ProcessorInfo process(ProcessorInfo processorInfo) throws Exception {
		System.out.println("processorInfo2 = " + processorInfo);
		return processorInfo;
	}
}
