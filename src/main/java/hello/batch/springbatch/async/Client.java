package hello.batch.springbatch.async;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Client {
	private String username;
	private int age;
	private String year;
}
