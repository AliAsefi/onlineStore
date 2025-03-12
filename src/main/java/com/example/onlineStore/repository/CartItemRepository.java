package com.example.onlineStore.repository;

import com.example.onlineStore.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    CartItemEntity findByCart_IdAndProduct_Id(Long id, Long id1);
}
