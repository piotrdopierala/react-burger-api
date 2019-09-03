package pl.dopierala.reactburgerapi.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import pl.dopierala.reactburgerapi.model.Ingredient.Ingredient;
import pl.dopierala.reactburgerapi.service.IngredientService;

import java.util.Optional;

@Configuration
public class StartupConfiguration {

    IngredientService ingredientService;

    @Autowired
    public StartupConfiguration(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void autostart() {
        ingredientsInit();
    }

    private void ingredientsInit() {

        Ingredient saladIngr;
        Ingredient baconIngr;
        Ingredient cheeseIngr;
        Ingredient meatIngr;

        Optional<Ingredient> saladInDB = ingredientService.getIngredientByName("salad");
        saladIngr = saladInDB.orElseGet(() -> new Ingredient("salad", 0.5));

        Optional<Ingredient> baconInDB = ingredientService.getIngredientByName("bacon");
        baconIngr = baconInDB.orElseGet(() -> new Ingredient("bacon", 0.7));

        Optional<Ingredient> cheeseInDB = ingredientService.getIngredientByName("cheese");
        cheeseIngr = cheeseInDB.orElseGet(() -> new Ingredient("cheese", 0.4));

        Optional<Ingredient> meatInDB = ingredientService.getIngredientByName("meat");
        meatIngr = meatInDB.orElseGet(() -> new Ingredient("meat", 1.3));

        ingredientService.addIngredient(saladIngr);
        ingredientService.addIngredient(baconIngr);
        ingredientService.addIngredient(cheeseIngr);
        ingredientService.addIngredient(meatIngr);

    }
}
