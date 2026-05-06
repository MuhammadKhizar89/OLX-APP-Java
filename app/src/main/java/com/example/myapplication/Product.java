package com.example.myapplication;

public class Product {
    private int id;
    private String title;
    private int price;
    private String imageUrl;

    public Product() {}

    public Product(int id, String title, int price, String imageUrl) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public int getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
}