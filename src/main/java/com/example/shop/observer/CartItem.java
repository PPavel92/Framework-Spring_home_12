package com.example.shop.observer;

public class CartItem {
    private String description;
    private double price;

    public CartItem(String description, double price) {
        this.description = description;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CartItem cartItem = (CartItem) obj;
        return Double.compare(cartItem.price, price) == 0 &&
                description.equals(cartItem.description);
    }

    @Override
    public int hashCode() {
        return 31 * description.hashCode() + (int) price;
    }
}
