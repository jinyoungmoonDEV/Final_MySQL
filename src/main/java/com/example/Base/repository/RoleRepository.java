package com.example.Base.repository;

import com.example.Base.domain.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByName(String name); //name으로 role정보 찾기 -> JpaRepo ->DB

}

