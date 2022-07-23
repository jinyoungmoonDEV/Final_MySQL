package com.example.Base.service;

import com.example.Base.domain.dto.UserDTO;
import com.example.Base.domain.entity.UserEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public interface TokenService {
    void loginMethod(UserDTO userDTO, HttpServletResponse response);
    void refreshToken(UserEntity user, HttpServletResponse response);
}
