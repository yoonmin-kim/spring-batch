package hello.batch.springbatch.multi_thread;

import org.springframework.batch.core.ItemReadListener;

public class ItemReaderListener implements ItemReadListener<Client> {

	@Override
	public void beforeRead() {

	}

	@Override
	public void afterRead(Client client) {
		System.out.println(Thread.currentThread().getName() + "[Reader] : " + client);
	}

	@Override
	public void onReadError(Exception e) {

	}
}
