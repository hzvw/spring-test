package com.example.service;

import com.example.entities.Order;

/**
 * @auther zzyy
 * @create 2023-12-01 17:52
 */
public interface OrderService {

    /**
     * 创建订单
     */
    void create(Order order);

}