package com.example.Base.service.token;

import com.example.Base.domain.dto.user.UserDTO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public interface TokenService {
    void loginMethod(UserDTO userDTO, HttpServletResponse response); //유저 정보 확인, 토큰 발급
    void refreshToken(UserDTO user, HttpServletResponse response); //Refresh Token 유효성 검사, 토큰 재발급
    UserDTO decodeJWT(HttpServletRequest request); //JWT Decode
}
