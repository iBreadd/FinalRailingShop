package com.example.RailingShop.Exceptions;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(Long id){
        super("Поръчка с id:"+id+" не е намерено!");
    }
}
