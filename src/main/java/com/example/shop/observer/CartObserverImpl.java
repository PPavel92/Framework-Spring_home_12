package com.example.shop.observer;

public class CartObserverImpl implements CartObserver {

    @Override
    public void update(String message) {
        System.out.println(message);
    }
}
