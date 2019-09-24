package pl.dopierala.reactburgerapi.controller;

import org.junit.Test;
import org.junit.experimental.results.ResultMatchers;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import pl.dopierala.reactburgerapi.model.ingredient.Ingredient;
import pl.dopierala.reactburgerapi.service.IngredientService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = IngredientController.class)
public class IngredientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IngredientService ingredientServiceMock;

    @Test
    public void Should_getAllIngredients_return_two_ingredients() throws Exception {
        //given
        Ingredient ing1 = new Ingredient();
        Ingredient ing2 = new Ingredient();
        ing1.setName("ing1");
        ing2.setName("ing2");
        List<Ingredient> ingredientsList = new ArrayList<Ingredient>(Arrays.asList(ing1,ing2));
        //when
        when(ingredientServiceMock.getAllIngredientsByPrice()).thenReturn(ingredientsList);
        //then
        mockMvc.perform(get("/burger/api/ingredient/getAll"))
                .andExpect(jsonPath("$..name").value(hasItem("ing1")))
                .andExpect(jsonPath("$..name").value(hasItem("ing2")))
                .andExpect(jsonPath("$").value(hasSize(2)));
        verify(ingredientServiceMock,times(1)).getAllIngredientsByPrice();
    }

}