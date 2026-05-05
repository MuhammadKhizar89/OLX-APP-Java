package com.example.myapplication;

public class Product {
    private int id;
    private String name;
    private String price;
    private String description;
    private String imageUrl;

    public Product() {}

    public Product(int id, String name, String price, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
}