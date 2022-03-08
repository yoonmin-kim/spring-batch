package hello.batch.springbatch.jpa.itemwriter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "client2")
public class ClientEntity {

	@Id
	private String name;
	private int age;
	private String year;
}
