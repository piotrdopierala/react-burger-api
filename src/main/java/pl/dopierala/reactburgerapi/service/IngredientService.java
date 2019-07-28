package pl.dopierala.reactburgerapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.dopierala.reactburgerapi.model.Ingredient;
import pl.dopierala.reactburgerapi.repository.IngredientRepo;

import java.util.List;

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


}
