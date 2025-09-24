package com.example.dao;

import com.example.dto.Order;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);
    
    @Lock(LockModeType.OPTIMISTIC)
    Optional<Order> findWithLockingById(Long id);
    
    @Query("SELECT o FROM Order o WHERE o.id = :id AND o.version = :version")
    Optional<Order> findByIdAndVersion(Long id, Integer version);

    @Modifying
    @Query("UPDATE Order o SET o.shippingAddress = :newAddress, o.version = o.version + 1 WHERE o.id = :orderId AND o.version = :currentVersion")
    int updateAddressWithVersion(@Param("orderId") Long orderId, @Param("newAddress") String newAddress, @Param("currentVersion") Integer currentVersion);
}