package com.example.Base.service.token;

import com.example.Base.domain.dto.user.UserDTO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public interface TokenService {
    void loginMethod(UserDTO userDTO, HttpServletResponse response);
    void refreshToken(UserDTO user, HttpServletResponse response);
    UserDTO decodeJWT(HttpServletRequest request);
}
