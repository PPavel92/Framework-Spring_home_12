package com.example.shop.factory;

import com.example.shop.model.Product;

public class ProductFactory {

    public static Product createProduct(String description, double price) {
        return new Product(description, price);
    }
}
