package com.raptor.ecommerceproject.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = ("orders"))
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numberOrder;
    private Date dateCreation;
    private Date dateReceived;
    private double total;

    //Atributo donde se crea el usuario
    @ManyToOne
    private User userOrder;

    //Atributo para el detalle de la orden
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> details;

    public Order() {
    }

    public Order(Long id, String numberOrder, Date dateCreation, Date dateReceived, double total, User userOrder) {
        this.id = id;
        this.numberOrder = numberOrder;
        this.dateCreation = dateCreation;
        this.dateReceived = dateReceived;
        this.total = total;
        this.userOrder = userOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumberOrder() {
        return numberOrder;
    }

    public void setNumberOrder(String numberOrder) {
        this.numberOrder = numberOrder;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public User getUserOrder() {
        return userOrder;
    }

    public void setUserOrder(User userOrder) {
        this.userOrder = userOrder;
    }

    public List<OrderDetail> getDetails() {
        return details;
    }

    public void setDetails(List<OrderDetail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", numberOrder='" + numberOrder + '\'' +
                ", dateCreation=" + dateCreation +
                ", dateReceived=" + dateReceived +
                ", total=" + total +
                '}';
    }
}
