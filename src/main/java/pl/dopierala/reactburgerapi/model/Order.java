package pl.dopierala.reactburgerapi.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pl.dopierala.reactburgerapi.model.customer.Customer;
import pl.dopierala.reactburgerapi.model.customer.CustomerSerializerIdAndEmailOnly;
import pl.dopierala.reactburgerapi.model.deliveryData.DeliveryData;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="burger_orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.EAGER, mappedBy = "order")
    private List<Burger> orderedBurgers;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinColumn
    @JsonSerialize(using = CustomerSerializerIdAndEmailOnly.class)
    private Customer customer;
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private DeliveryData deliveryData;


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

    public DeliveryData getDeliveryData() {
        return deliveryData;
    }

    public void setDeliveryData(DeliveryData deliveryData) {
        this.deliveryData = deliveryData;
        deliveryData.setOrder(this);
    }
}
