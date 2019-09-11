package pl.dopierala.reactburgerapi.model.deliveryData;

import pl.dopierala.reactburgerapi.model.Order;

import javax.persistence.*;

@Entity
public class DeliveryData {
    @Id
    private Long id;
    private String name;
    private String address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
