package pl.dopierala.reactburgerapi.model;

import javax.persistence.*;

@Entity
@Table(name="burgers")
public class Burger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;
    private int Salad;
    private int Bacon;
    private int Cheese;
    private int Meat;

    public Burger() {
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

    public int getSalad() {
        return Salad;
    }

    public void setSalad(int salad) {
        Salad = salad;
    }

    public int getBacon() {
        return Bacon;
    }

    public void setBacon(int bacon) {
        Bacon = bacon;
    }

    public int getCheese() {
        return Cheese;
    }

    public void setCheese(int cheese) {
        Cheese = cheese;
    }

    public int getMeat() {
        return Meat;
    }

    public void setMeat(int meat) {
        Meat = meat;
    }

    public static BurgerBuilder builder() {
        return new BurgerBuilder();
    }

    public static class BurgerBuilder {
        private Burger newBurger = new Burger();

        public BurgerBuilder withSalad(int salad) {
            newBurger.setSalad(salad);
            return this;
        }

        public BurgerBuilder withBacon(int bacon) {
            newBurger.setBacon(bacon);
            return this;
        }

        public BurgerBuilder withCheese(int cheese) {
            newBurger.setCheese(cheese);
            return this;
        }

        public BurgerBuilder withMeat(int meat) {
            newBurger.setMeat(meat);
            return this;
        }

        public Burger build() {
            return newBurger;
        }
    }

}
