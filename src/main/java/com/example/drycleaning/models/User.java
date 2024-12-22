package com.example.drycleaning.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "\"user\"")
public class User extends BaseEntity {
    private String lastName;
    private String name;
    private String phone;
    private String email;

    private String password;

    private City city;

    private Loyalty loyalty;

    private List<Role> roles;

    private Set<Order> order;

    public User(String lastName, String name, String phone, String email, String password, City city) {
        this.lastName = lastName;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.city = city;
    }


    protected User() {
        this.roles = new ArrayList<>();
    }

    @Column(name = "last_name",  nullable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Column(length = 11, nullable = false, unique = true)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Column(nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @OneToMany(mappedBy = "user", targetEntity = Order.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Order> getOrder() {
        return order;
    }

    public void setOrder(Set<Order> order) {
        this.order = order;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @OneToOne(mappedBy = "user", targetEntity = Loyalty.class)
    public Loyalty getLoyalty() {
        return loyalty;
    }

    public void setLoyalty(Loyalty loyalty) {
        this.loyalty = loyalty;
    }

}
