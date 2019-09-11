package pl.dopierala.reactburgerapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.dopierala.reactburgerapi.model.ingredient.Ingredient;
import pl.dopierala.reactburgerapi.repository.IngredientRepo;

import java.util.List;
import java.util.Optional;

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

    public void addIngredient(Ingredient ingredientToAdd){
        ingredientRepo.save(ingredientToAdd);
    }

    public Optional<Ingredient> getIngredientByName(String name) {
        return ingredientRepo.findByName(name);
    }
}
