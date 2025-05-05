package com.example.service;

import com.example.entities.Order;
import com.example.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ClassName: OrderServiceImpl
 * Package: com.example.service
 * Description:
 *
 * @Author Harizon
 * @Create 2025/5/4 14:48
 * @Version 1.0
 */
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public int add(Order order) {
        return orderMapper.insertSelective(order);
    }
}
