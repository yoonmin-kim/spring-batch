package hello.batch.springbatch.fault_tolerant.retry;

public class RetryAbleException extends RuntimeException{

	public RetryAbleException() {
	}

	public RetryAbleException(String message) {
		super(message);
	}
}
