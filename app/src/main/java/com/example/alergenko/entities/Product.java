package com.example.alergenko.entities;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Product {
    private int id;
    private String brandName;
    private String name;
    private ArrayList<Allergens> allergens;
    private byte [] picture;

    public Product(int id, String brandName, String name, ArrayList<Allergens> allergens, byte[] picture) {
        this.id = id;
        this.brandName = brandName;
        this.name = name;
        this.allergens = allergens;
        this.picture = picture;
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

    public ArrayList<Allergens> getAllergens() {
        return allergens;
    }

    public void setAllergens(ArrayList<Allergens> allergens) {
        this.allergens = allergens;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}
