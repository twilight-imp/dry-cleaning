package com.example.drycleaning.models;
import jakarta.persistence.*;
import org.w3c.dom.Text;

import java.util.Set;

@Entity
@Table(name = "service")
public class Service extends BaseEntity {
    private String name;

    private String description;

    private Integer cost;


    private Set<OrderPosition> orderPositions;

    public Service(String name, String description, Integer cost) {
        this.name = name;
        this.description = description;
        this.cost = cost;
    }

    protected Service() {
    }

    @Column(length = 50, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(mappedBy = "service", targetEntity = OrderPosition.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<OrderPosition> getOrderPositions() {
        return orderPositions;
    }

    public void setOrderPositions(Set<OrderPosition> orderPositions) {
        this.orderPositions = orderPositions;
    }

    @Column(nullable = false)
    public Integer getCost() {
        return cost;
    }
    public void setCost(Integer cost) {
        this.cost = cost;
    }
}

