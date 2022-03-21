package hello.batch.springbatch.multi_thread;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Client {
	private String username;
	private int age;
	private String year;
}
