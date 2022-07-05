package hmanjarres.projectBackend.security;

import hmanjarres.projectBackend.SpringApplicationContext;

public class SecurityConstants {
    public static final long EXPIRATION_DATE = 864000000;// token expira en 10 dias
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";
//    public static final String TOKEN_SECRET = "X5nJpRYxp1aUSPb5TtLn3Gs6THfL2n8h";
    public static String getTokenSecret(){
        AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
        return appProperties.getTokenSecret();
    }
}
