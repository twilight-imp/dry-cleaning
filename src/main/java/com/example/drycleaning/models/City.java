package com.example.drycleaning.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "city")
public class City extends BaseEntity{
    private String name;

    private Set<User> users;

    private Set<Branch> branches;


    public City(String name) {
        this.name = name;
    }

    protected City() {
    }

    @Column(length = 50, nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "city", targetEntity = User.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @OneToMany(mappedBy = "city", targetEntity = Branch.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Branch> getBranches() {
        return branches;
    }

    public void setBranches(Set<Branch> branches) {
        this.branches = branches;
    }


}
