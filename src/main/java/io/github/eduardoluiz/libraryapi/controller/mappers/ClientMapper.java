package io.github.eduardoluiz.libraryapi.controller.mappers;


import io.github.eduardoluiz.libraryapi.controller.dto.ClientDTO;
import io.github.eduardoluiz.libraryapi.model.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toEntity(ClientDTO dto);

    ClientDTO toDto(Client client);
}
