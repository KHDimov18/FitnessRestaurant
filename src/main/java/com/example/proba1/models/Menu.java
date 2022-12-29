package com.example.servingwebcontent.models;
import java.util.*;


public class Menu {
    private int MenuId;
    private String Title;

    public List<Integer> getProducts() {
        return Products;
    }

    public void setProducts(List<Integer> products) {
        Products = products;
    }

    private List<Integer> Products;

    private List<Product> ProductsObj;

    @java.lang.Override
    public java.lang.String toString() {
        return "Menu{" +
                "MenuId=" + MenuId +
                ", Title='" + Title + '\'' +
                '}';
    }

    public Menu(int menuId, String title, ArrayList<Integer> products) {
        MenuId = menuId;
        Title = title;
        Products = products;
    }

    public Menu(int menuId, String title, List<Product> productsObj) {
        MenuId = menuId;
        Title = title;
        ProductsObj = productsObj;
    }

    public Menu() {
    }

    public int getMenuId() {
        return MenuId;
    }

    public void setMenuId(int menuId) {
        MenuId = menuId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Integer getTotalCalories()
    {
        Integer total = 0;
        for (Product product : ProductsObj) {
            total += product.getCalories();
        }
        return total;
    }

    public void setProductsObj(List<Product> productsObj) {
        ProductsObj = productsObj;
    }
}