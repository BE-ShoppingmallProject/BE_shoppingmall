package com.github.shoppingmall.shopping_mall.service.exceptions;

public class AccessDenied extends RuntimeException {

    public AccessDenied(String message) {
        super(message);
    }
}
