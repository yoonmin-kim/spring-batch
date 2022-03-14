package hello.batch.springbatch.fault_tolerant.skip;

public class SkipAbleException extends Exception {
	public SkipAbleException(String failed) {
		super(failed);
	}
}
