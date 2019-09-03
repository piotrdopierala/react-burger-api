package pl.dopierala.reactburgerapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.dopierala.reactburgerapi.model.Customer;
import pl.dopierala.reactburgerapi.repository.CustomerRepo;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private CustomerRepo customerRepo;

    public UserDetailsServiceImpl(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customerFoundByEmail = customerRepo.findByEmail(email);
        if (customerFoundByEmail == null) {
            throw new UsernameNotFoundException(email);
        }
        return new User(
                customerFoundByEmail.getEmail(),
                customerFoundByEmail.getPassword(),
                Collections.emptyList()
        );
    }
}
