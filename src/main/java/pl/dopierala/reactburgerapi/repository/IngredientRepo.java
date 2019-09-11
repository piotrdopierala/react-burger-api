package pl.dopierala.reactburgerapi.repository;

import org.springframework.data.repository.CrudRepository;
import pl.dopierala.reactburgerapi.model.ingredient.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientRepo extends CrudRepository<Ingredient,Long> {
    List<Ingredient> findByOrderByPriceAsc();
    Optional<Ingredient> findByName(String name);
}
