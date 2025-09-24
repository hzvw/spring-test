package com.example.dao;

import com.example.dto.OrderModifyLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// OrderModifyLogRepository.java
public interface OrderModifyLogRepository extends JpaRepository<OrderModifyLog, Long> {
    List<OrderModifyLog> findByOrderIdOrderByChangeTimeDesc(Long orderId);
}