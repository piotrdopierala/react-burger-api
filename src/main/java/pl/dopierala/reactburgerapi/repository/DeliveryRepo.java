package pl.dopierala.reactburgerapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dopierala.reactburgerapi.model.deliveryData.DeliveryData;

public interface DeliveryRepo extends JpaRepository<DeliveryData, Long> {

}
