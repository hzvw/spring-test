package com.example.controller;

import com.example.entities.Order;
import com.example.resp.ResultData;
import com.example.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther zzyy
 * @create 2023-12-01 17:55
 */

@RestController
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 创建订单
     */
    @PostMapping("/order/create")
    public ResultData create(@RequestBody Order order)
    {
        orderService.create(order);
        return ResultData.success(order);
    }
}