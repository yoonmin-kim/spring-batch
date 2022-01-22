package hello.batch.springbatch;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

public class MyJobValidator	implements JobParametersValidator {

	@Override
	public void validate(JobParameters jobParameters) throws JobParametersInvalidException {
		if (jobParameters.getString("name") == null) {
			throw new JobParametersInvalidException("name parameter is not found");
		}
	}
}
