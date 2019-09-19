package pl.dopierala.reactburgerapi.configuration.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pl.dopierala.reactburgerapi.model.customer.Customer;
import pl.dopierala.reactburgerapi.service.CustomerService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import static pl.dopierala.reactburgerapi.configuration.security.SecurityConstants.HEADER_STRING;
import static pl.dopierala.reactburgerapi.configuration.security.SecurityConstants.SECRET;
import static pl.dopierala.reactburgerapi.configuration.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthorisationFilter extends BasicAuthenticationFilter {

    private CustomerService customerService;

    public JWTAuthorisationFilter(AuthenticationManager authenticationManager, CustomerService customerService) {
        super(authenticationManager);
        this.customerService = customerService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        Enumeration<String> headers = req.getHeaderNames();
        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req,res);

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
        String token = req.getHeader(HEADER_STRING);
        if (token != null && token.length()>40) {
            String customerEmail = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""))
                    .getSubject();
            if (customerEmail != null) {
                Customer CustomerFoundByEmail = customerService.getByEmail(customerEmail);
                UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(CustomerFoundByEmail,null,new ArrayList<>());
                userPassAuthToken.setDetails(new WebAuthenticationDetails(req));
                return userPassAuthToken;
            }
            return null;
        }
        return null;
    }
}
