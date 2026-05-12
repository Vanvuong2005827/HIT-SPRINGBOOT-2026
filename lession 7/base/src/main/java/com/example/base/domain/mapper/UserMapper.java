package com.example.base.domain.mapper;

import org.mapstruct.*;

import com.example.base.domain.dto.request.CreateUserRequestDto;
import com.example.base.domain.dto.response.UserResponseDto;
import com.example.base.domain.entity.User;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {

  UserResponseDto userToUserResponseDto(User user);

  User createUserRequestDtoToUser(CreateUserRequestDto request);

}