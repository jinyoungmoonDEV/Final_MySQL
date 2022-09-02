package com.example.Base.repository;

import com.example.Base.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByEmail(String email); //email로 user정보 찾기 -> JpaRepo ->DB

    UserEntity findByName(String name);

    List<UserEntity> findAllByCategory(String category);
}
