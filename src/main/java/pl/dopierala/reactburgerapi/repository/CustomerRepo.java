package pl.dopierala.reactburgerapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dopierala.reactburgerapi.model.Customer;

public interface CustomerRepo extends JpaRepository<Customer,Long> {
    Customer findByEmail(String email);
    boolean existsByEmail(String email);
}
