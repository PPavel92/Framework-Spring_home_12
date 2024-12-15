package com.example.shop.decorator;

import com.example.shop.model.Product;

public class GiftWrapDecorator extends Product {
    private Product product;

    public GiftWrapDecorator(Product product) {
        super(product.getDescription(), product.getPrice());
        this.product = product;
    }

    @Override
    public String getDescription() {
        return product.getDescription() + " with Gift Wrap";
    }

    @Override
    public double getPrice() {
        return product.getPrice() + 5;
    }
}
