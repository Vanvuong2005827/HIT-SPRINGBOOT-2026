package com.example.base.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.base.domain.dto.response.UserResponseDto;
import com.example.base.domain.entity.User;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AuthMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "role", ignore = true)

  UserResponseDto userToUserResponseDto(User user);
}