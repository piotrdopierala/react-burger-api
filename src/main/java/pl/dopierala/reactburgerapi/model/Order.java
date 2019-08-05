package pl.dopierala.reactburgerapi.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Entity
@Table(name="burger_orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER, mappedBy = "order")
    private List<Burger> orderedBurgers;
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn
    private Customer customer;
    private String deliveryMethod;
    private BigDecimal price;

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Burger> getOrderedBurgers() {
        return orderedBurgers;
    }

    public void setOrderedBurgers(List<Burger> orderedBurgers) {
        this.orderedBurgers = orderedBurgers;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
