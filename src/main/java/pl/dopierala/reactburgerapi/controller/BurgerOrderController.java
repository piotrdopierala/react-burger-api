package pl.dopierala.reactburgerapi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.dopierala.reactburgerapi.model.Burger;
import pl.dopierala.reactburgerapi.model.Customer;
import pl.dopierala.reactburgerapi.model.Order;
import pl.dopierala.reactburgerapi.service.BurgerOrderService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/burger/api")
@CrossOrigin
public class BurgerOrderController {

    @Autowired
    BurgerOrderService burgerOrderService;

    ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/getorders")
    public List<Burger> getOrders(){
        return burgerOrderService.getOrders();
    }

    @PostMapping("/saveorder")
    public void saveOrder(@RequestBody String receivedJSON) throws IOException {

        ObjectNode responseNode = mapper.readValue(receivedJSON,ObjectNode.class);
        JsonNode ingredientsNode = responseNode.get("ingredients");
        JsonNode customerNode = responseNode.get("customer");

        Customer customerThatOrdered = new Customer();
        customerThatOrdered.setName(customerNode.get("name").asText());
        customerThatOrdered.setEmail(customerNode.get("email").asText());
        customerThatOrdered.setAddress(customerNode.get("address").asText());

        Burger burgerToSave = Burger.builder()
                .withBacon(ingredientsNode.get("bacon").asInt())
                .withCheese(ingredientsNode.get("cheese").asInt())
                .withMeat(ingredientsNode.get("meat").asInt())
                .withSalad(ingredientsNode.get("salad").asInt())
                .build();


        Order orderToSave = new Order();
        orderToSave.setCustomer(customerThatOrdered);
        orderToSave.setOrderedBurgers(Collections.singletonList(burgerToSave));
        orderToSave.setDeliveryMethod(responseNode.get("deliveryMethod").asText());
        double price = responseNode.get("price").asDouble();
        BigDecimal priceDecimal = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP);
        orderToSave.setPrice(priceDecimal);

        burgerToSave.setOrder(orderToSave);

        burgerOrderService.saveOrder(orderToSave);
    }

}
