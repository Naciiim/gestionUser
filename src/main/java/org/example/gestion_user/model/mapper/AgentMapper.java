package org.example.gestion_user.model.mapper;

import org.example.gestion_user.model.dto.AgentDto;
import org.example.gestion_user.model.entity.Agent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AgentMapper {

    AgentMapper INSTANCE = Mappers.getMapper(AgentMapper.class);

    AgentDto toDto(Agent agent);

    Agent toEntity(AgentDto agentDTO);
}