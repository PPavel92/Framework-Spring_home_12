package com.example.shop.observer;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void addItem(String description, double price) {
        this.items.add(new CartItem(description, price));
    }

    public void removeItem(CartItem item) {
        this.items.remove(item);
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getPrice();
        }
        return total;
    }

    public String getCartItemsString() {
        StringBuilder cartContent = new StringBuilder();
        for (CartItem item : items) {
            cartContent.append(item.getDescription())
                    .append(" - ")
                    .append(item.getPrice())
                    .append(" руб.\n");
        }
        return cartContent.toString();
    }
}
