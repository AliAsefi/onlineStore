package com.example.onlineStore.repository;

import com.example.onlineStore.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    CartItemEntity findByCart_IdAndProduct_Id(Long id, Long id1);

    @Modifying
    @Query("DELETE FROM CartItemEntity c WHERE c.product.id IN :productIds")
    void deleteByProductIdIn(@Param("productIds") List<Long> productIds);
}
