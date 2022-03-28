package hello.batch.springbatch.listener;

import org.springframework.batch.core.annotation.AfterProcess;
import org.springframework.batch.core.annotation.BeforeProcess;
import org.springframework.batch.core.annotation.OnProcessError;

public class CustomProcessListener {

	@BeforeProcess
	public void beforeProcess(String s) {
		System.out.println(".beforeProcess");
	}

	@AfterProcess
	public void afterProcess(String s, String s2) {
		System.out.println(".afterProcess");
	}

	@OnProcessError
	public void onProcessError(String s, Exception e) {
		System.out.println(".onProcessError");
	}
}
