// This class provides utility methods for security-related operations, 
// such as retrieving the current user's ID and username from the security context.

package com.example.base.security;

import com.example.base.constant.ErrorMessage;
import com.example.base.exception.VsException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
  public static String getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new VsException(HttpStatus.UNAUTHORIZED, ErrorMessage.Auth.ERR_TOKEN_INVALIDATED);
    }

    Object principal = authentication.getPrincipal();

    if (principal instanceof CustomUserDetails) {
      return ((CustomUserDetails) principal).getUser().getId();
    }

    if (principal instanceof String && principal.equals("anonymousUser")) {
      throw new VsException(HttpStatus.UNAUTHORIZED, ErrorMessage.Auth.ERR_TOKEN_INVALIDATED);
    }

    throw new VsException(HttpStatus.UNAUTHORIZED, ErrorMessage.Auth.ERR_TOKEN_INVALIDATED);
  }

  public static String getCurrentUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null)
      return null;
    return authentication.getName();
  }
}