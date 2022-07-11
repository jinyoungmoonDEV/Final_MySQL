package com.example.Base.repository;

import com.example.Base.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByEmail(String email); //email로 user정보 찾기 -> JpaRepo ->DB
}
