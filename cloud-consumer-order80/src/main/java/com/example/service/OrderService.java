package com.example.service;

import com.example.entities.Order;
import org.springframework.stereotype.Service;

/**
 * ClassName: OrderService
 * Package: com.example.service
 * Description:
 *
 * @Author Harizon
 * @Create 2025/5/4 14:47
 * @Version 1.0
 */
@Service
public interface OrderService {
    int add(Order order);
}
