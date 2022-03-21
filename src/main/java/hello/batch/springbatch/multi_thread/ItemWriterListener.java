package hello.batch.springbatch.multi_thread;

import java.util.List;

import org.springframework.batch.core.ItemWriteListener;

public class ItemWriterListener implements ItemWriteListener<Client> {

	@Override
	public void beforeWrite(List<? extends Client> list) {

	}

	@Override
	public void afterWrite(List<? extends Client> items) {
		System.out.println(Thread.currentThread().getName() + "[Writer] : " + items);
	}

	@Override
	public void onWriteError(Exception e, List<? extends Client> list) {

	}
}
