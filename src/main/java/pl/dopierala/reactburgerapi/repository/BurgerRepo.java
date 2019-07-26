package pl.dopierala.reactburgerapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dopierala.reactburgerapi.model.Burger;

public interface BurgerRepo extends JpaRepository<Burger,Long> {

}
