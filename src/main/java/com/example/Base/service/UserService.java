package com.example.Base.service;

import com.example.Base.domain.RoleEntity;
import com.example.Base.domain.UserEntity;

import java.util.List;

public interface UserService {
    UserEntity saveUser(UserEntity user);
    RoleEntity saveRole(RoleEntity role);
    void addRoleToUser(String email, String roleName);
    UserEntity getUser(String email);

    List<UserEntity>getUsers();//파라미터가 없다 이유는 유저가 많으면 모두 불러 오는 형식이면 과부화 오기 떄문에
}
