package pl.dopierala.reactburgerapi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.dopierala.reactburgerapi.errorHandling.exceptionDefinitions.InsufficientDataException;
import pl.dopierala.reactburgerapi.model.Burger;
import pl.dopierala.reactburgerapi.model.Customer;
import pl.dopierala.reactburgerapi.model.Order;
import pl.dopierala.reactburgerapi.service.BurgerOrderService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/burger/api")
@CrossOrigin
public class BurgerOrderController {

    @Autowired
    BurgerOrderService burgerOrderService;

    ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/getburgers")
    public List<Burger> getBurgers(){
        return burgerOrderService.getBurgers();
    }

    @GetMapping("/getorders")
    public List<Order> getOrders(){
        return burgerOrderService.gerOrders();
    }

    @PostMapping("/saveorder")
    public void saveOrder(@RequestBody String receivedJSON) throws IOException, InterruptedException {

        ObjectNode responseNode = mapper.readValue(receivedJSON,ObjectNode.class);
        JsonNode ingredientsNode = responseNode.get("ingredients");
        JsonNode customerNode = responseNode.get("customer");

        Customer customerThatOrdered = new Customer();
        Burger burgerToSave;
        try {
            customerThatOrdered.setName(customerNode.get("name").asText());
            customerThatOrdered.setEmail(customerNode.get("email").asText());
            customerThatOrdered.setAddress(customerNode.get("address").asText());

            burgerToSave = Burger.builder()
                    .withBacon(ingredientsNode.get("bacon").asInt())
                    .withCheese(ingredientsNode.get("cheese").asInt())
                    .withMeat(ingredientsNode.get("meat").asInt())
                    .withSalad(ingredientsNode.get("salad").asInt())
                    .build();
        }
        catch (NullPointerException e){
            throw new InsufficientDataException("Insufficient order data received from front-end.");
        }

        Order orderToSave = new Order();
        orderToSave.setCustomer(customerThatOrdered);
        orderToSave.setOrderedBurgers(Collections.singletonList(burgerToSave));
        orderToSave.setDeliveryMethod(responseNode.get("deliveryMethod").asText());
        double price = responseNode.get("price").asDouble();
        BigDecimal priceDecimal = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP);
        orderToSave.setPrice(priceDecimal);

        burgerToSave.setOrder(orderToSave);

        burgerOrderService.saveOrder(orderToSave);
        Thread.sleep(1500);//only to check spinner functionality on front-end.
    }

}
