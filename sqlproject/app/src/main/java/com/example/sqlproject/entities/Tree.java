package com.example.sqlproject.entities;

public class Tree {
    private final int id;
    private final String type;
    private final int stock;
    private double price;
    private final String imageUrl;

    public Tree(int id, String type, int stock, double price, String imageUrl) {
        this.id = id;
        this.type = type;
        this.stock = stock;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public int getID() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public int getStock() {
        return this.stock;
    }

    public double getPrice() {
        return this.price;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}