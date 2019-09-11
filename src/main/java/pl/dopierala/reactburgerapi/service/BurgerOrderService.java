package pl.dopierala.reactburgerapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.dopierala.reactburgerapi.model.Burger;
import pl.dopierala.reactburgerapi.model.customer.Customer;
import pl.dopierala.reactburgerapi.model.Order;
import pl.dopierala.reactburgerapi.repository.BurgerRepo;
import pl.dopierala.reactburgerapi.repository.OrderRepo;

import java.util.List;

@Service
public class BurgerOrderService {

    @Autowired
    private BurgerRepo burgerRepo;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderRepo orderRepo;

    public List<Burger> getBurgers() {
        return burgerRepo.findAll();
    }

    public List<Order> getOrders(){
        return orderRepo.findAll();
    }

    public List<Order> getOrdersOfCustomerEmail(String email){
        return orderRepo.findAllByCustomerEmail(email);
    }

    public void saveOrder(Order orderToSave){

        Customer customerFoundByEmail = customerService.getByEmail(orderToSave.getCustomer().getEmail());
        if(customerFoundByEmail != null){
            orderToSave.setCustomer(customerFoundByEmail);
        }
        orderRepo.save(orderToSave);
    }

}
