package hello.batch.springbatch.jpa.itemwriter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import hello.batch.springbatch.flatfile.Client;

@Mapper
public interface ClientMapper {

	ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

	ClientEntity clientToClientEntity(Client client);
}
