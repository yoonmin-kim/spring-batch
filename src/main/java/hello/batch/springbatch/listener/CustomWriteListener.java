package hello.batch.springbatch.listener;

import java.util.List;

import org.springframework.batch.core.ItemWriteListener;

public class CustomWriteListener implements ItemWriteListener<String> {
	@Override
	public void beforeWrite(List<? extends String> list) {
		System.out.println(".beforeWrite");
	}

	@Override
	public void afterWrite(List<? extends String> list) {
		System.out.println(".afterWrite");
	}

	@Override
	public void onWriteError(Exception e, List<? extends String> list) {
		System.out.println(".onWriteError");
	}
}
