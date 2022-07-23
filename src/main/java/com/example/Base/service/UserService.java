package com.example.Base.service;

import com.example.Base.domain.dto.UserDTO;
import com.example.Base.domain.entity.RoleEntity;
import com.example.Base.domain.entity.UserEntity;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserEntity saveUser(UserDTO userDTO); //return tpye이 Entity
    RoleEntity saveRole(RoleEntity role); //return tpye이 Entity
    List<UserEntity>getUsers();//파라미터가 비어있다, 이유는 유저가 많으면 모두 불러 오는 형식이면 과부화 오기 떄문에
}
