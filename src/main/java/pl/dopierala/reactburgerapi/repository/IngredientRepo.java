package pl.dopierala.reactburgerapi.repository;

import org.springframework.data.repository.CrudRepository;
import pl.dopierala.reactburgerapi.model.Ingredient;

import java.util.List;

public interface IngredientRepo extends CrudRepository<Ingredient,Long> {
    List<Ingredient> findByOrderByPriceAsc();
}
