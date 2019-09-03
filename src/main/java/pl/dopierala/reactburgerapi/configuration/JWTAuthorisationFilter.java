package pl.dopierala.reactburgerapi.configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorisationFilter extends BasicAuthenticationFilter {

    public JWTAuthorisationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

//TODO Finish Authorisation filter, configure filters in security conf. adapter
}
