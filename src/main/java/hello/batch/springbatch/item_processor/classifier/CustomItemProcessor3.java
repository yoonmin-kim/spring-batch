package hello.batch.springbatch.item_processor.classifier;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor3 implements ItemProcessor<ProcessorInfo, ProcessorInfo> {

	@Override
	public ProcessorInfo process(ProcessorInfo processorInfo) throws Exception {
		System.out.println("processorInfo3 = " + processorInfo);
		return processorInfo;
	}
}
