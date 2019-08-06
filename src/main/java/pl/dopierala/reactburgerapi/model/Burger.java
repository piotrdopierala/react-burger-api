package pl.dopierala.reactburgerapi.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name="burgers")
public class Burger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="order_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Order order;
    //private int Salad;
    //private int Bacon;
    //private int Cheese;
    //private int Meat;
    @ElementCollection
    @CollectionTable(
            joinColumns = @JoinColumn(name="burger_id")
    )
    @Column(name="count")
    @MapKeyJoinColumn(name="ingredient_id")
    private Map<Ingredient,Integer> ingredients = new HashMap<>();

    public Burger() {
    }

    public void addIngredient(Ingredient ingredient, int amount){
        ingredients.merge(ingredient, amount, Integer::sum);
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

    public Map<Ingredient, Integer> getIngredients() { //nie mozna persystowac takiej MAP gdzie key jest Entity, trzeba tworzyc dodatkowa encje
        return ingredients;
    }

    public void setIngredients(Map<Ingredient, Integer> ingredients) {
        this.ingredients = ingredients;
    }

}
