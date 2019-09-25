package pl.dopierala.reactburgerapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.dopierala.reactburgerapi.model.Burger;
import pl.dopierala.reactburgerapi.model.Order;
import pl.dopierala.reactburgerapi.model.customer.Customer;
import pl.dopierala.reactburgerapi.model.ingredient.Ingredient;
import pl.dopierala.reactburgerapi.service.BurgerOrderService;
import pl.dopierala.reactburgerapi.service.CustomerService;
import pl.dopierala.reactburgerapi.service.IngredientService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.longThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = BurgerOrderController.class)
public class BurgerOrderControllerTest {

    private static final String TEST_USER_EMAIL = "test@test.pl";
    private static final String TEST_USER_PASSWORD = "testPassword";
    private static final String TEST_USER_NAME = "test customer name";

    @MockBean
    BurgerOrderService burgerOrderServiceMock;

    @MockBean
    IngredientService ingredientService;

    @MockBean
    CustomerService customerService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private Ingredient ingrSalad = new Ingredient("salad",5.0);
    private Ingredient ingrBacon = new Ingredient("bacon",8.0);
    private List<Burger> burgers = generateTestBurgers();
    private List<Order> orders = generateTestOrders();
    private Customer customer = new Customer(TEST_USER_NAME,TEST_USER_EMAIL);

    private List<Order> generateTestOrders() {
        Order order1 = new Order();
        order1.setOrderedBurgers(Collections.singletonList(burgers.get(0)));
        order1.setId(101L);

        Order order2 = new Order();
        order2.setOrderedBurgers(Collections.singletonList(burgers.get(1)));
        order2.setId(202L);

        return new ArrayList<>(Arrays.asList(order1,order2));
    }

    private List<Burger> generateTestBurgers(){

        Burger b1 = new Burger();
        b1.addIngredient(ingrBacon,4);

        Burger b2 = new Burger();
        b2.addIngredient(ingrBacon,1);
        b2.addIngredient(ingrSalad,1);

        return new ArrayList<>(Arrays.asList(b1,b2));
    }

    @Test
    @WithMockUser
    public void Should_getBurgers_return_burgers() throws Exception {
        //given
        when(burgerOrderServiceMock.getBurgers()).thenReturn(burgers);
        //when
        ResultActions resultActions = mockMvc.perform(get("/burger/api/getburgers"));
        //then
        resultActions.andExpect(status().isOk());
        verify(burgerOrderServiceMock, times(1)).getBurgers();
        resultActions.andExpect(jsonPath("$").value(hasSize(2)));
        resultActions.andExpect(jsonPath("$..id").exists());
        resultActions.andExpect(jsonPath("$..order").exists());
        resultActions.andExpect(jsonPath("$..ingredients").exists());
    }

    @Test
    public void Should_getBurgers_access_denied() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/burger/api/getburgers"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    public void Should_getOrders_return_test_orders() throws Exception {
        //given
        //when
        when(burgerOrderServiceMock.getOrders()).thenReturn(orders);
        //then
        ResultActions resultActions = mockMvc.perform(get("/burger/api/getorders/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(hasSize(2)))
                .andExpect(jsonPath("$..id").exists())
                .andExpect(jsonPath("$..orderedBurgers").exists())
                .andExpect(jsonPath("$..customer").exists())
                .andExpect(jsonPath("$..deliveryData").exists())
                .andExpect(jsonPath("$..price").exists());
    }

    @Test
    public void Should_getOrders_access_denied() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/burger/api/getorders/all"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = TEST_USER_EMAIL, password = TEST_USER_PASSWORD)
    public void Should_getOrdersOfUser_return_orders_of_user() throws Exception {
        //given
        final Order order = orders.get(1);
        final List<Order> userOrdersList = Collections.singletonList(order);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(customer,null,new ArrayList<>()));
        //when
        when(burgerOrderServiceMock.getOrdersOfCustomerEmail(TEST_USER_EMAIL)).thenReturn(userOrdersList);
        //then
        ResultActions resultActions = mockMvc.perform(get("/burger/api/getorders/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(202L));

        verify(burgerOrderServiceMock,times(1)).getOrdersOfCustomerEmail(TEST_USER_EMAIL);
    }

    @Test
    public void Should_getOrdersOfUser_access_denied() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/burger/api/getorders/user"))
                .andExpect(status().isForbidden());
    }


}