package pl.dopierala.reactburgerapi.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.dopierala.reactburgerapi.model.Customer;
import pl.dopierala.reactburgerapi.repository.CustomerRepo;

@Controller
@RequestMapping("/burger/api/cust")
public class CustomerController {

    private CustomerRepo customerRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public CustomerController(CustomerRepo customerRepo, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.customerRepo = customerRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Customer customer){
        customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
        customerRepo.save(customer);
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
