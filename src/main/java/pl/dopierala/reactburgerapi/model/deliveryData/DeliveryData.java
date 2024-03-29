package pl.dopierala.reactburgerapi.model.deliveryData;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pl.dopierala.reactburgerapi.model.Order;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class DeliveryData {
    @Id
    private Long id;
    private String name;
    private String street;
    private String zipCode;
    private String country;
    private String deliveryMethod;
    @Column
    private String email;
    private BigDecimal price;
    @OneToOne
    @MapsId
    @JsonIgnore
    private Order order;

    public DeliveryData() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
