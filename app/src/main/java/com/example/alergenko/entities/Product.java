package com.example.alergenko.entities;

import java.util.ArrayList;
import java.util.UUID;

public class Product {
    private int id;
    private String brandName;
    private String name;

    public Product(int id, String brandName, String name) {
        this.id = id;
        this.brandName = brandName;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
