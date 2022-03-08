package hello.batch.springbatch.jpa.itemwriter;

import org.springframework.batch.item.ItemProcessor;

import hello.batch.springbatch.flatfile.Client;

public class CustomProcessor implements ItemProcessor<Client, ClientEntity> {

	@Override
	public ClientEntity process(Client client) throws Exception {
		ClientEntity clientEntity = ClientMapper.INSTANCE.clientToClientEntity(client);
		return clientEntity;
	}
}
