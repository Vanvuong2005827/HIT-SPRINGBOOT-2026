package com.example.base.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.base.constant.ErrorMessage;
import com.example.base.domain.dto.request.CreateUserRequestDto;
import com.example.base.domain.dto.response.UserResponseDto;
import com.example.base.domain.entity.User;
import com.example.base.domain.mapper.UserMapper;
import com.example.base.exception.VsException;
import com.example.base.repository.UserRepository;
import com.example.base.service.UserService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

  UserRepository userRepository;

  UserMapper userMapper;

  PasswordEncoder passwordEncoder;

  @Override
  public UserResponseDto getUserById(String userId) {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new VsException(HttpStatus.BAD_REQUEST, ErrorMessage.User.ERR_USER_NOT_EXISTED));

    return userMapper.userToUserResponseDto(user);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public UserResponseDto createUser(CreateUserRequestDto request) {

    if (userRepository.existsUserByEmail(request.getEmail())) {
      throw new VsException(HttpStatus.BAD_REQUEST, ErrorMessage.User.ERR_EMAIL_EXISTED);
    }
    if (userRepository.existsUserByUsername(request.getUsername())) {
      throw new VsException(HttpStatus.BAD_REQUEST, ErrorMessage.User.ERR_USERNAME_EXISTED);
    }

    User user = userMapper.createUserRequestDtoToUser(request);

    user.setPassword(passwordEncoder.encode(request.getPassword()));

    return userMapper.userToUserResponseDto(userRepository.save(user));
  }

  @Override
  public UserResponseDto getMyProfile(String userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new VsException(
            HttpStatus.UNAUTHORIZED,
            ErrorMessage.User.ERR_USER_NOT_EXISTED));

    UserResponseDto userResponseDto = userMapper.userToUserResponseDto(user);

    return userResponseDto;
  }

}