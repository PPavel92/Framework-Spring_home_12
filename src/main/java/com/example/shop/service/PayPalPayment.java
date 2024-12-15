package com.example.shop.service;

public class PayPalPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Оплата через PayPal. Сумма: " + amount + " руб.");
    }
}
