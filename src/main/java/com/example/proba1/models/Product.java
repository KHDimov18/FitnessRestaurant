package com.example.servingwebcontent.models;

public class Product {
    private int ProductId;
    private String Title;
    private int Calories;

    public Product(int productId, String title, int calories) {
        ProductId = productId;
        Title = title;
        Calories = calories;
    }

    public Product() {
    }

    @java.lang.Override
    public java.lang.String toString() {
            return "Product{" +
                "ProductId=" + ProductId +
                ", Title='" + Title + '\'' +
                ", Calories=" + Calories +
                '}';
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getCalories() {
        return Calories;
    }

    public void setCalories(int calories) {
        Calories = calories;
    }
}