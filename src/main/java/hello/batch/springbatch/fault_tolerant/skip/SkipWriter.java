package hello.batch.springbatch.fault_tolerant.skip;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class SkipWriter implements ItemWriter<String> {
	@Override
	public void write(List<? extends String> items) throws Exception {
		for (String item : items) {
			if ("-12".equals(item)) {
				throw new SkipAbleException("Write failed");
			}
			System.out.println("item = " + item);
		}
	}
}
