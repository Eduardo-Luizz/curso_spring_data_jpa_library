package io.github.eduardoluiz.libraryapi.controller.mappers;

import io.github.eduardoluiz.libraryapi.controller.dto.UserRequestDTO;
import io.github.eduardoluiz.libraryapi.controller.dto.UserResponseDTO;
import io.github.eduardoluiz.libraryapi.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequestDTO userRequestDTO);

    UserResponseDTO toResponseDTO(User user);
}
