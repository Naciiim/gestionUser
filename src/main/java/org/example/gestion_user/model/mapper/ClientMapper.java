package org.example.gestion_user.model.mapper;

import org.example.gestion_user.model.dto.ClientDto;
import org.example.gestion_user.model.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    Client toEntity(ClientDto clientDTO);

    ClientDto toDto(Client client);
}