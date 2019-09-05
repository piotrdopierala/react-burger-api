package pl.dopierala.reactburgerapi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;
import pl.dopierala.reactburgerapi.model.Customer;
import pl.dopierala.reactburgerapi.repository.CustomerRepo;

import java.io.IOException;

@Controller
@RequestMapping("/burger/api/cust")
public class CustomerController {

    private CustomerRepo customerRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ObjectMapper objectMapper;
    private AuthenticationManager authenticationManager;

    public CustomerController(CustomerRepo customerRepo, BCryptPasswordEncoder bCryptPasswordEncoder, ObjectMapper objectMapper, AuthenticationManager authenticationManager) {
        this.customerRepo = customerRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.objectMapper = objectMapper;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody String receivedData) {
        String email, password;
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(receivedData);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        if (jsonNode.has("email") && jsonNode.has("password")) {
            email = jsonNode.get("email").asText();
            password = jsonNode.get("password").asText();

            if (customerRepo.existsByEmail(email)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already registered, please log in");
            }

            Customer newCustomer = new Customer();
            newCustomer.setPassword(bCryptPasswordEncoder.encode(password));
            newCustomer.setEmail(email);
            customerRepo.save(newCustomer);

            return new ResponseEntity<>(HttpStatus.ACCEPTED);

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No email and/or password data passed in request");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticate() {
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @GetMapping("/secured")
    public String securedTest(@RequestParam("token") String token) {
        return "SECURE INFORMATION";
    }
}
