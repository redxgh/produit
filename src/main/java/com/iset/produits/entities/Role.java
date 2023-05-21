package com.iset.produits.entities;

import javax.persistence.*;

@Entity
public class Role {
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public Role() {

    }

    public Integer getId() {
        return id;
    }
    public Role(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
