package com.example.shop.service;

public class CreditCardPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Оплата картой. Сумма: " + amount + " руб.");
    }
}
