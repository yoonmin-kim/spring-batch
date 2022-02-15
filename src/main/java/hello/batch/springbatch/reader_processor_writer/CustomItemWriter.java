package hello.batch.springbatch.reader_processor_writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class CustomItemWriter implements ItemWriter<Customer> {

	@Override
	public void write(List<? extends Customer> customers) throws Exception {
		System.out.println("customers = " + customers);
	}
}
