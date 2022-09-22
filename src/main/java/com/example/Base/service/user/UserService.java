package com.example.Base.service.user;

import com.example.Base.domain.dto.user.UserDTO;
import com.example.Base.domain.entity.UserEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {
    UserEntity saveUser(UserDTO userDTO); //return tpye이 Entity
    List<UserEntity>getUsers();//파라미터가 비어있다, 이유는 유저가 많으면 모두 불러 오는 형식이면 과부화 오기 떄문에
    UserEntity getUser(String email); //email -> userInfo
    String getName(String email); //email -> name
    String getEmail(String name); //name -> email
    UserEntity clientInfo(HttpServletRequest request); //회원 정보
    String gosuRating(UserDTO userDTO); //전문가 별점
    void editUserInfo(UserDTO userDTO); //회원 정보 수정
    void withdrawal(UserDTO userDTO); //회원탈퇴
    String getCategory(String email); //email -> category
    List getAllExpertsByCategory(String category);
}
