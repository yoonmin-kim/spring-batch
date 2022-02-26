package hello.batch.springbatch.flatfile;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Client {

	@Id
	@GeneratedValue
	private String name;
	private int age;
	private String year;
}
