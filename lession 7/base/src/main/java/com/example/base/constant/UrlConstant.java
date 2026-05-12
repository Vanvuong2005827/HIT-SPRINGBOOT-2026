package com.example.base.constant;

public class UrlConstant {
  public static class Auth {
    private static final String PRE_FIX = "/auth";

    public static final String LOGIN = PRE_FIX + "/login";
    public static final String REFRESH_TOKEN = PRE_FIX + "/refresh";
    public static final String LOGOUT = PRE_FIX + "/logout";

    private Auth() {
    }
  }

  public static class User {
    private static final String PRE_FIX = "/user";

    public static final String GET_PROFILE = PRE_FIX + "/profile";

    private User() {
    }
  }

  public static class Admin {
    private static final String PRE_FIX = "/admin";

    public static final String GET_USER = PRE_FIX + "/users/{userId}";
    public static final String CREATE_USER = PRE_FIX + "/create-user";

    private Admin() {
    }
  }
}