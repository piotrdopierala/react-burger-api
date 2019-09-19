package pl.dopierala.reactburgerapi.model.deliveryData;

import pl.dopierala.reactburgerapi.model.Order;

import javax.persistence.*;

@Entity
public class DeliveryData {
    @Id
    private Long id;
    private String name;
    private String street;
    private String zipCode;
    private String country;
    private String deliveryMethod;
    @Column(unique = true)
    private String email;
    @OneToOne
    @MapsId
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
}
