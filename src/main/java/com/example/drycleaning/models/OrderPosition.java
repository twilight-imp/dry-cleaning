package com.example.drycleaning.models;

import jakarta.persistence.*;


@Entity
@Table(name = "order_positions")

public class OrderPosition extends BaseEntity {
    private Product product;
    private Service service;
    private Order order;

    private Integer number;

    private Integer amount;

    public OrderPosition(Product product, Service service, Order order, Integer number, Integer amount) {
        this.product = product;
        this.service = service;
        this.order = order;
        this.number = number;
        this.amount = amount;
    }

    protected OrderPosition() {
    }



    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id", referencedColumnName = "id")
    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }


    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Column(nullable = false)
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Column()
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "OrderPosition{" +
                "product=" + product +
                ", service=" + service +
                ", order=" + order +
                ", number=" + number +
                ", amount=" + amount +
                '}';
    }
}
