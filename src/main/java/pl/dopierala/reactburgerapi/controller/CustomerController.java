package pl.dopierala.reactburgerapi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.dopierala.reactburgerapi.model.Customer;
import pl.dopierala.reactburgerapi.repository.CustomerRepo;

import java.io.IOException;

@Controller
@RequestMapping("/burger/api/cust")
public class CustomerController {

    private CustomerRepo customerRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ObjectMapper objectMapper;

    public CustomerController(CustomerRepo customerRepo, BCryptPasswordEncoder bCryptPasswordEncoder, ObjectMapper objectMapper) {
        this.customerRepo = customerRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.objectMapper = objectMapper;
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
                return new ResponseEntity<>("Email already registered, please log in",HttpStatus.CONFLICT);
            }

            Customer newCustomer = new Customer();
            newCustomer.setPassword(bCryptPasswordEncoder.encode(password));
            newCustomer.setEmail(email);
            customerRepo.save(newCustomer);

            return new ResponseEntity<>(HttpStatus.ACCEPTED);

        } else {
            return new ResponseEntity<>("No email and/or password data passed in request", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestParam("email") String email, @RequestParam("pass") String password) {
        return null;
    }

    @GetMapping("/secured")
    public String securedTest(@RequestParam("token") String token) {
        return "SECURE INFORMATION";
    }
}
