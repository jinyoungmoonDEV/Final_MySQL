package com.example.Base.service;

import com.example.Base.domain.dto.UserDTO;
import com.example.Base.domain.entity.TokenEntity;
import com.example.Base.domain.entity.UserEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface TokenService {
    void loginMethod(UserDTO userDTO, HttpServletResponse response);
}
