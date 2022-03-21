package hello.batch.springbatch.multi_thread;

import org.springframework.batch.core.ItemProcessListener;

public class ItemProcessorListener implements ItemProcessListener<Client, Client> {

	@Override
	public void beforeProcess(Client client) {

	}

	@Override
	public void afterProcess(Client client, Client client2) {
		System.out.println(Thread.currentThread().getName() + "[Process] : " + client);
	}

	@Override
	public void onProcessError(Client client, Exception e) {

	}
}
