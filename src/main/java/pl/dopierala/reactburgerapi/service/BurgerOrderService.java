package pl.dopierala.reactburgerapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.dopierala.reactburgerapi.model.Burger;
import pl.dopierala.reactburgerapi.model.Order;
import pl.dopierala.reactburgerapi.repository.BurgerRepo;
import pl.dopierala.reactburgerapi.repository.CustomerRepo;
import pl.dopierala.reactburgerapi.repository.OrderRepo;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BurgerOrderService {

    @Autowired
    private BurgerRepo burgerRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private OrderRepo orderRepo;

    public List<Burger> getBurgers() {
        return burgerRepo.findAll();
    }

    public List<Order> gerOrders(){
        return orderRepo.findAll();
    }

    public void saveOrder(Order burger){
        orderRepo.save(burger);
    }

}
