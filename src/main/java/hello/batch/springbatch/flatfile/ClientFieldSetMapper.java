package hello.batch.springbatch.flatfile;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class ClientFieldSetMapper implements FieldSetMapper<Client> {

	@Override
	public Client mapFieldSet(FieldSet fieldSet) throws BindException {

		if (fieldSet == null) {
			return null;
		}

		Client client = new Client();
		client.setName(fieldSet.readString(0));
		client.setAge(fieldSet.readInt(1));
		client.setYear(fieldSet.readString(2));
		return client;
	}
}
