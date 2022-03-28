package hello.batch.springbatch.listener;

import org.springframework.batch.core.ItemReadListener;

public class CustomReadListener implements ItemReadListener<String> {

	@Override
	public void beforeRead() {
		System.out.println(".beforeRead");
	}

	@Override
	public void afterRead(String s) {
		System.out.println(".afterRead");
	}

	@Override
	public void onReadError(Exception e) {
		System.out.println(".onReadError");
	}
}
