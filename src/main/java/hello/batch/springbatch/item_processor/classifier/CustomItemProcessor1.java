package hello.batch.springbatch.item_processor.classifier;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor1 implements ItemProcessor<ProcessorInfo, ProcessorInfo> {

	@Override
	public ProcessorInfo process(ProcessorInfo processorInfo) throws Exception {
		System.out.println("processorInfo1 = " + processorInfo);
		return processorInfo;
	}
}
