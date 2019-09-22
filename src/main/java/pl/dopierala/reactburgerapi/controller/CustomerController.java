package pl.dopierala.reactburgerapi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.dopierala.reactburgerapi.errorHandling.exceptionDefinitions.CustomerEmailAlreadyRegisteredException;
import pl.dopierala.reactburgerapi.model.customer.Customer;
import pl.dopierala.reactburgerapi.model.customer.CustomerDTO;
import pl.dopierala.reactburgerapi.repository.CustomerRepo;
import pl.dopierala.reactburgerapi.service.CustomerService;

import java.io.IOException;

@Controller
@RequestMapping("/burger/api/cust")
public class CustomerController {

    private CustomerService customerService;
    private ObjectMapper objectMapper;

    public CustomerController(ObjectMapper objectMapper, CustomerService customerService) {
        this.objectMapper = objectMapper;
        this.customerService = customerService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody String receivedData) throws InterruptedException {
        Thread.sleep(1000);
        CustomerDTO newCustomerDTO = new CustomerDTO();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(receivedData);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (!jsonNode.has("email") || !jsonNode.has("password")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No email and/or password data passed in request");
        }

        newCustomerDTO.email = jsonNode.get("email").asText();
        newCustomerDTO.password = jsonNode.get("password").asText();
        newCustomerDTO.name = jsonNode.get("name").asText();
        newCustomerDTO.street = jsonNode.get("street").asText();
        newCustomerDTO.zipCode = jsonNode.get("zipCode").asText();
        newCustomerDTO.country = jsonNode.get("country").asText();

        try {
            customerService.saveNewCustomer(newCustomerDTO);
        }catch (CustomerEmailAlreadyRegisteredException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
