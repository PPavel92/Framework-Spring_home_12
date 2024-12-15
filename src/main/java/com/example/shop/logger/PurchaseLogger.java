package com.example.shop.logger;

import com.example.shop.model.Product;

public class PurchaseLogger {

    private static PurchaseLogger instance;

    private PurchaseLogger() {

    }

    public static PurchaseLogger getInstance() {
        if (instance == null) {
            synchronized (PurchaseLogger.class) {
                if (instance == null) {
                    instance = new PurchaseLogger();
                }
            }
        }
        return instance;
    }

    public void logPurchase(Product product) {

        System.out.println("Purchase logged: " + product.getDescription() + " for $" + product.getPrice());
    }
}
