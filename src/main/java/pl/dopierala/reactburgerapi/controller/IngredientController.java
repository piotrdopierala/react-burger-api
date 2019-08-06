package pl.dopierala.reactburgerapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dopierala.reactburgerapi.model.Ingredient.Ingredient;
import pl.dopierala.reactburgerapi.service.IngredientService;

import java.util.List;

@RestController
@RequestMapping("/burger/api/ingredient")
public class IngredientController {

    private IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/getAll")
    public List<Ingredient> getAllIngredients() throws InterruptedException {
        Thread.sleep(1500);//only to check spinner functionality on front-end.s
        return  ingredientService.getAllIngredientsByPrice();
    }
}
