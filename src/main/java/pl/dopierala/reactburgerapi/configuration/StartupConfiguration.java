package pl.dopierala.reactburgerapi.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import pl.dopierala.reactburgerapi.model.Ingredient;
import pl.dopierala.reactburgerapi.service.IngredientService;

@Configuration
public class StartupConfiguration {

    IngredientService ingredientService;

    @Autowired
    public StartupConfiguration(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void autostart(){
        ingredientsInit();
    }

    private void ingredientsInit(){
        Ingredient saladIngr = new Ingredient("salad",0.5);
        Ingredient baconIngr = new Ingredient("bacon",0.7);
        Ingredient cheeseIngr = new Ingredient("cheese",0.4);
        Ingredient meatIngr = new Ingredient("meat",1.3);

        ingredientService.addIngredient(saladIngr);
        ingredientService.addIngredient(baconIngr);
        ingredientService.addIngredient(cheeseIngr);
        ingredientService.addIngredient(meatIngr);


    }
}
