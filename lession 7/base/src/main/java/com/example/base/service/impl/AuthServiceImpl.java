package com.example.base.service.impl;

import com.example.base.constant.CommonConstant;
import com.example.base.constant.ErrorMessage;
import com.example.base.constant.SuccessMessage;
import com.example.base.domain.dto.request.LoginRequestDto;
import com.example.base.domain.dto.request.LogoutRequestDto;
import com.example.base.domain.dto.response.CommonResponseDto;
import com.example.base.domain.dto.response.LoginResponseDto;
import com.example.base.domain.entity.InvalidatedToken;
import com.example.base.domain.entity.User;
import com.example.base.exception.VsException;
import com.example.base.repository.InvalidatedTokenRepository;
import com.example.base.repository.UserRepository;
import com.example.base.security.JwtProvider;
import com.example.base.security.UserDetailsServiceImpl;
import com.example.base.service.AuthService;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

  UserRepository userRepository;
  InvalidatedTokenRepository invalidatedTokenRepository;
  JwtProvider jwtProvider;
  UserDetailsServiceImpl userDetailsService;

  PasswordEncoder passwordEncoder;

  @NonFinal
  @Value("${jwt.access.expiration_time}")
  long ACCESS_TOKEN_EXPIRATION;

  @NonFinal
  @Value("${jwt.refresh.expiration_time}")
  long REFRESH_TOKEN_EXPIRATION;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public LoginResponseDto authentication(LoginRequestDto request) {
    User user = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new VsException(
            HttpStatus.UNAUTHORIZED,
            ErrorMessage.Auth.ERR_INVALID_CREDENTIALS));

    // [1]. Validate password
    boolean auth = passwordEncoder.matches(request.getPassword(), user.getPassword());
    if (!auth) {
      throw new VsException(HttpStatus.UNAUTHORIZED, ErrorMessage.Auth.ERR_INVALID_CREDENTIALS);
    }

    // [2]. Generate tokens
    String accessToken = jwtProvider.generateToken(user, ACCESS_TOKEN_EXPIRATION);
    String refreshToken = jwtProvider.generateToken(user, REFRESH_TOKEN_EXPIRATION);

    // [3]. Build response object
    return LoginResponseDto.builder()
        .status(HttpStatus.OK)
        .message("")
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .id(user.getId())
        .tokenType(CommonConstant.BEARER_TOKEN)
        .build();
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public CommonResponseDto logout(LogoutRequestDto request) {
    try {
      // [1]. Parse the token to extract claims
      SignedJWT signedJWT = SignedJWT.parse(request.getToken());

      // [2]. Get username and get user details to validate token
      String username = jwtProvider.extractUsername(request.getToken());
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      // [3]. Validate token
      if (!jwtProvider.isTokenValid(request.getToken(), userDetails)) {
        throw new VsException(HttpStatus.UNAUTHORIZED, ErrorMessage.Auth.ERR_TOKEN_INVALIDATED);
      }

      // [4]. Get JWT ID and expiration time to invalidate token
      String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
      Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

      if (invalidatedTokenRepository.existsById(jwtId)) {
        throw new VsException(HttpStatus.BAD_REQUEST, ErrorMessage.Auth.ERR_TOKEN_ALREADY_INVALIDATED);
      }

      // [5]. Invalidate access token
      // This step will save the JWT ID and expiration time of the token to the
      // database, so that when the user tries to use the token again, we can check if
      // it has been invalidated or not.
      invalidatedTokenRepository.save(new InvalidatedToken(jwtId, expirationTime));

      return new CommonResponseDto(HttpStatus.OK, SuccessMessage.Auth.LOGOUT_SUCCESS);
    } catch (ParseException e) {
      throw new VsException(HttpStatus.BAD_REQUEST, ErrorMessage.Auth.ERR_GET_TOKEN_CLAIM_SET_FAIL);
    }
  }

}