package hello.batch.springbatch.fault_tolerant.retry.customre_try_template;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.classify.Classifier;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.DefaultRetryState;
import org.springframework.retry.support.RetryTemplate;

import hello.batch.springbatch.fault_tolerant.retry.RetryAbleException;

public class CustomItemProcessor implements ItemProcessor<String, String> {

	@Autowired
	private RetryTemplate retryTemplate;
	private int cnt;

	@Override
	public String process(String item) throws Exception {
		Classifier<? super Throwable, Boolean> rollbackClassifier = new BinaryExceptionClassifier(true);

		return retryTemplate.execute(
			new RetryCallback<String, RuntimeException>() {
				@Override
				public String doWithRetry(RetryContext retryContext) throws RuntimeException {
					cnt++;
					if ("6".equals(item) || "7".equals(item)) {
						throw new RetryAbleException("failed cnt : " + cnt);
					}
					return item;
				}
			},
			new RecoveryCallback<String>() {
				@Override
				public String recover(RetryContext retryContext) throws Exception {
					return item;
				}
			},
			new DefaultRetryState(item, rollbackClassifier)
		);
	}
}
