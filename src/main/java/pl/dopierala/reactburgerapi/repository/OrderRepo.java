package pl.dopierala.reactburgerapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dopierala.reactburgerapi.model.Order;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order,Long> {
    public List<Order> findAllByCustomerEmail(String email);
}
