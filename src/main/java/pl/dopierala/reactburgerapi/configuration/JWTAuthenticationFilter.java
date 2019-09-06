package pl.dopierala.reactburgerapi.configuration;

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
import pl.dopierala.reactburgerapi.model.Customer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static pl.dopierala.reactburgerapi.configuration.SecurityConstants.*;


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

        if (!reqBodyNode.hasNonNull("email") || !reqBodyNode.hasNonNull("pass")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing email or pass field in passed JSON");
        }

        String email = reqBodyNode.get("email").asText();
        String password = reqBodyNode.get("pass").asText();

        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                email,
                password
        ));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        Date expiresAt = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC512(SECRET.getBytes()));

        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(res.getWriter());
        jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectFieldStart("auth");
                jsonGenerator.writeStringField("email", ((User) auth.getPrincipal()).getUsername());
                jsonGenerator.writeStringField("token", token);
                jsonGenerator.writeStringField("expiresGMT", dateFormat.format(expiresAt));jsonGenerator.writeEndObject();
            jsonGenerator.writeEndObject();
        jsonGenerator.close();
    }
}
