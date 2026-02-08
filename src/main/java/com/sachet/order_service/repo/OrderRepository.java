package com.sachet.order_service.repo;

import com.sachet.order_service.model.Orders;
import com.sachet.order_service.model.Status;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    @Query(value = """
            SELECT ordr FROM Orders ordr where ordr.userId=:userId
        """)
    List<Orders> getOrderByUserId(@Param("userId") String userId);

    @Transactional
    @Modifying
    @Query(value = """
        UPDATE Orders ordr SET ordr.status = :status, ordr.expiresAt = null WHERE ordr.id = :id
    """)
    void updateOrdersById(@Param("status")Status status, @Param("id")Long id);
}
