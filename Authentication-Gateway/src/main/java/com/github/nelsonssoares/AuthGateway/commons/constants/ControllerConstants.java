package com.github.nelsonssoares.AuthGateway.commons.constants;

public class ControllerConstants {

    public static final String API_BASE_URL = "starbank/auth";
    public static final String API_VERSION = "v1";
    public static final String API_TAG = "STAR BANK - Registration and Authentication API";
    public static final String API_DESCRIPTION = "API para Registro e Autenticação de usuários da Star Bank";

    // ENDPOINTS
    public static final String REGISTER_USER = "/signup";
    public static final String LOGIN_USER = "/signin";
    public static final String REFRESH_TOKEN = "/refresh-token/{username}";
}
