package com.example.alergenko.entities;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Product {
    private int id;
    private String barcode;
    private String brandName;
    private String name;
    private String shortName;
    private ArrayList<Allergens> allergens;
    private byte [] picture;
    private String ingredients;

    public Product(int id, String barcode, String brandName, String name, String shortName, ArrayList<Allergens> allergens, byte[] picture, String ingredients) {
        this.id = id;
        this.barcode = barcode;
        this.brandName = brandName;
        this.name = name;
        this.shortName = shortName;
        this.allergens = allergens;
        this.picture = picture;
        this.ingredients = ingredients;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
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

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}

