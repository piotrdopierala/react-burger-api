package pl.dopierala.reactburgerapi.configuration.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.server.ResponseStatusException;
import pl.dopierala.reactburgerapi.model.customer.Customer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static pl.dopierala.reactburgerapi.configuration.security.SecurityConstants.*;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {

        JsonNode reqBodyNode;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            reqBodyNode = objectMapper.readTree(req.getReader());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON");
        }

        if (!reqBodyNode.hasNonNull("email") || !reqBodyNode.hasNonNull("password")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing email or pass field in passed JSON");
        }

        String email = reqBodyNode.get("email").asText();
        String password = reqBodyNode.get("password").asText();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Authentication authResult = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                email,
                password
        ));

        return authResult;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        Date expiresAt = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        String token = JWT.create()
                .withSubject(((Customer) auth.getPrincipal()).getEmail())
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC512(SECRET.getBytes()));

        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(res.getWriter());
        jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectFieldStart("auth");
                jsonGenerator.writeStringField("email", ((Customer) auth.getPrincipal()).getEmail());
                jsonGenerator.writeStringField("token", token);
                jsonGenerator.writeStringField("expiresGMT", dateFormat.format(expiresAt));
                jsonGenerator.writeStringField("expiresIn", String.valueOf(EXPIRATION_TIME));
            jsonGenerator.writeEndObject();
        jsonGenerator.close();
    }
}
