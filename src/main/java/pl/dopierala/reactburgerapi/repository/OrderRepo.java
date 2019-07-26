package pl.dopierala.reactburgerapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dopierala.reactburgerapi.model.Order;

public interface OrderRepo extends JpaRepository<Order,Long> {
}
