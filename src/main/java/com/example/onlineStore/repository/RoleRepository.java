package com.example.onlineStore.repository;

import com.example.onlineStore.entity.RoleEntity;
import com.example.onlineStore.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByRoleName(RoleName roleName);
}
