package pl.dopierala.reactburgerapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.dopierala.reactburgerapi.model.ingredient.Ingredient;
import pl.dopierala.reactburgerapi.repository.IngredientRepo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    private IngredientRepo ingredientRepo;

    @Autowired
    public IngredientService(IngredientRepo ingredientRepo) {
        this.ingredientRepo = ingredientRepo;
    }

    public List<Ingredient> getAllIngredientsByPrice() {
        return ingredientRepo.findByOrderByPriceAsc();
    }

    public Map<String, BigDecimal> getAllIngredientNamesMapPrice(){
        List<Ingredient> IngredientsList = getAllIngredientsByPrice();
        Map<String, BigDecimal> ingredientsPriceMap = IngredientsList.stream().collect(Collectors.toMap(Ingredient::getName, Ingredient::getPrice));
        return ingredientsPriceMap;
    }

    public void addIngredient(Ingredient ingredientToAdd){
        ingredientRepo.save(ingredientToAdd);
    }

    public Optional<Ingredient> getIngredientByName(String name) {
        return ingredientRepo.findByName(name);
    }
}
