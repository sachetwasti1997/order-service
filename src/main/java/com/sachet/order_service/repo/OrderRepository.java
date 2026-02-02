package com.sachet.order_service.repo;

import com.sachet.order_service.model.Orders;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    @Query(value = """
            SELECT ordr FROM Orders ordr where ordr.userId=:userId
        """)
    List<Orders> getOrderByUserId(@Param("userId") String userId);
}
