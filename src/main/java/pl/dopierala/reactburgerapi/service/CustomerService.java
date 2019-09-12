package pl.dopierala.reactburgerapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.dopierala.reactburgerapi.errorHandling.exceptionDefinitions.CustomerEmailAlreadyRegisteredException;
import pl.dopierala.reactburgerapi.model.customer.Customer;
import pl.dopierala.reactburgerapi.model.customer.CustomerDTO;
import pl.dopierala.reactburgerapi.repository.CustomerRepo;

@Service
public class CustomerService {
    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Customer getByEmail(String email) {
        return customerRepo.findByEmail(email);
    }

    public void saveNewCustomer(CustomerDTO customerDTO) throws CustomerEmailAlreadyRegisteredException {

            if (customerRepo.existsByEmail(customerDTO.email)) {
                throw new CustomerEmailAlreadyRegisteredException("Email already registered, please log in");
            }

            Customer newCustomer = new Customer();

            newCustomer.setPassword(bCryptPasswordEncoder.encode(customerDTO.password));
            newCustomer.setEmail(customerDTO.email);
            newCustomer.setName(customerDTO.name);
            newCustomer.setStreet(customerDTO.street);
            newCustomer.setZipCode(customerDTO.zipCode);
            newCustomer.setCountry(customerDTO.country);

            customerRepo.save(newCustomer);
    }

}
