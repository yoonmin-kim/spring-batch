package hello.batch.springbatch.fault_tolerant.skip;

import org.springframework.batch.item.ItemProcessor;

public class SkipProcessor implements ItemProcessor<String, String>{

	@Override
	public String process(String item) throws Exception {

		if ("6".equals(item) || "7".equals(item)) {
			throw new SkipAbleException("Process failed");
		}

		System.out.println("item = " + item);
		return String.valueOf(Integer.valueOf(item)-1);
	}
}
