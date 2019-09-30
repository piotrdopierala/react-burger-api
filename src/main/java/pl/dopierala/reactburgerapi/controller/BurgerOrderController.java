package pl.dopierala.reactburgerapi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.dopierala.reactburgerapi.errorHandling.exceptionDefinitions.IngredientNotFoundException;
import pl.dopierala.reactburgerapi.errorHandling.exceptionDefinitions.InsufficientDataException;
import pl.dopierala.reactburgerapi.model.Burger;
import pl.dopierala.reactburgerapi.model.customer.Customer;
import pl.dopierala.reactburgerapi.model.deliveryData.DeliveryData;
import pl.dopierala.reactburgerapi.model.ingredient.Ingredient;
import pl.dopierala.reactburgerapi.model.Order;
import pl.dopierala.reactburgerapi.service.BurgerOrderService;
import pl.dopierala.reactburgerapi.service.CustomerService;
import pl.dopierala.reactburgerapi.service.IngredientService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@RestController
@RequestMapping("/burger/api")
@CrossOrigin
public class BurgerOrderController {

    @Autowired
    BurgerOrderService burgerOrderService;

    @Autowired
    IngredientService ingredientService;

    @Autowired
    CustomerService customerService;

    ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/getburgers")
    public List<Burger> getBurgers() {
        return burgerOrderService.getBurgers();
    }

    @PostMapping("/saveorder")
    public String saveOrder(@RequestBody String receivedJSON, Authentication authentication) throws IOException, InterruptedException {

        //todo cleanup this controller (move business functionality to service)

        ObjectNode responseNode = mapper.readValue(receivedJSON, ObjectNode.class);
        JsonNode ingredientsNode = responseNode.get("ingredients");
        JsonNode orderData = responseNode.get("orderData");
        DeliveryData deliveryData = new DeliveryData();
        Customer customerLoggedInThatOrdered = (Customer) authentication.getPrincipal();
        Burger burgerToSave = new Burger();
        try {
            deliveryData.setName(orderData.get("name").asText());
            deliveryData.setEmail(orderData.get("email").asText());
            deliveryData.setCountry(orderData.get("country").asText());
            deliveryData.setStreet(orderData.get("street").asText());
            deliveryData.setZipCode(orderData.get("zipCode").asText());
            deliveryData.setDeliveryMethod(orderData.get("deliveryMethod").asText());


            Iterator<JsonNode> ingredientsIterator = ingredientsNode.elements();
            while (ingredientsIterator.hasNext()) {
                String ingredientName = ingredientsIterator.next().asText();

                Optional<Ingredient> ingredientByName = ingredientService.getIngredientByName(ingredientName);
                if (ingredientByName.isPresent()) {
                    burgerToSave.addIngredient(ingredientByName.get());
                } else {
                    throw new IngredientNotFoundException("ingredient named '" + ingredientName + "' not found.");
                }
            }
        } catch (NullPointerException e) {
            throw new InsufficientDataException("Insufficient order data received from front-end.");
        }

        Order orderToSave = new Order();

        orderToSave.setDeliveryData(deliveryData);
        ArrayList<Burger> orderedBurgers = new ArrayList<>();
        orderedBurgers.add(burgerToSave);
        orderToSave.setOrderedBurgers(orderedBurgers);
        BigDecimal priceFromReq = new BigDecimal(responseNode.get("price").asDouble()).setScale(2,RoundingMode.HALF_UP);
        BigDecimal priceCounted = countPrice(burgerToSave);

        if (!priceFromReq.equals(priceCounted)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error checking Burger price, place order again.");
        }

        deliveryData.setPrice(priceCounted);

        customerLoggedInThatOrdered.addOrder(orderToSave);
        burgerToSave.setOrder(orderToSave);

        customerLoggedInThatOrdered = customerService.save(customerLoggedInThatOrdered);
        orderToSave.setCustomer(customerLoggedInThatOrdered);
        burgerOrderService.saveOrder(orderToSave);
        Thread.sleep(1000);//only to check spinner functionality on front-end.

        ObjectNode objectNodeResponse = mapper.createObjectNode();
        objectNodeResponse.put("OrderId", orderToSave.getId());

        return objectNodeResponse.toString();
    }

    private BigDecimal countPrice(Burger burger) {
        final Map<String, BigDecimal> allIngredientMapPrice = ingredientService.getAllIngredientNamesMapPrice();
        BigDecimal test = new BigDecimal(1.0);
        test = test.add(new BigDecimal(2.0));

        return burger.getIngredients()
                .stream()
                .map((ingr) -> allIngredientMapPrice.get(ingr.getName()))
                .reduce(allIngredientMapPrice.get("start-price"), BigDecimal::add);
    }

    @GetMapping("/getorders/all")
    public List<Order> getOrders() throws InterruptedException {
        Thread.sleep(1500); //simulate loading on front-end
        return burgerOrderService.getOrders();
    }

    @GetMapping("/getorders/user")
    public List<Order> getOrdersOfUser(Authentication authentication) throws InterruptedException {
        Thread.sleep(1000);
        Customer principal = (Customer) authentication.getPrincipal();
        return burgerOrderService.getOrdersOfCustomerEmail(principal.getEmail());
    }
}
