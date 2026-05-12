// This class implements the UserDetailsService interface to load user-specific data during authentication.

package com.example.base.security;

import com.example.base.constant.ErrorMessage;
import com.example.base.domain.entity.User;
import com.example.base.repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDetailsServiceImpl implements UserDetailsService {

  UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository
        .findUserDetailByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.User.ERR_USER_NOT_EXISTED + username));
    return new CustomUserDetails(user);
  }

}
