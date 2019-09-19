package pl.dopierala.reactburgerapi.configuration.security;

public class SecurityConstants {
    public static final String SECRET = "SecretToGenJWTs";
    public static final int EXPIRATION_TIME = 3_600_000; //1 hour
    public static final String TOKEN_PREFIX = "";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/burger/api/cust/sign-up";
    public static final String LOGIN_PROCESS_URL = "/burger/api/cust/login-process";

}
