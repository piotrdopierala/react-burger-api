package pl.dopierala.reactburgerapi.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pl.dopierala.reactburgerapi.model.ingredient.Ingredient;
import pl.dopierala.reactburgerapi.model.ingredient.IngredientSerializerIngrNameOnly;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "burgers")
public class Burger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Order order;
    @ManyToMany
    @OrderColumn(name="ingr_order")
    @JsonSerialize(keyUsing = IngredientSerializerIngrNameOnly.class)
    private List<Ingredient> ingredients = new ArrayList<>();

    public Burger() {
    }

    public void addIngredient(Ingredient ingredient, int position) {
        ingredients.add(position, ingredient);
        //ingredients.merge(ingredient, amount, Integer::sum);
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(0, ingredient);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
