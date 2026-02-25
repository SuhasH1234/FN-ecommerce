package com.zosh.response;

import com.zosh.dto.OrderHistory;
import com.zosh.model.Cart;
import com.zosh.model.Order;
import com.zosh.model.Product;

public class FunctionResponse {
    private String functionName;
    private Cart userCart;
    private OrderHistory orderHistory;
    private Product product;

    public FunctionResponse() {
    }

    public FunctionResponse(String functionName, Cart userCart, OrderHistory orderHistory, Product product) {
        this.functionName = functionName;
        this.userCart = userCart;
        this.orderHistory = orderHistory;
        this.product = product;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public Cart getUserCart() {
        return userCart;
    }

    public void setUserCart(Cart userCart) {
        this.userCart = userCart;
    }

    public OrderHistory getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(OrderHistory orderHistory) {
        this.orderHistory = orderHistory;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
