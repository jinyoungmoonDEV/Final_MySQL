package com.example.Base.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.Base.domain.entity.UserEntity;
import com.example.Base.security.TokenProvider;
import com.example.Base.service.TokenServiceImpl;
import com.example.Base.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class TokenController {
    private final UserService userService;
    private final TokenServiceImpl tokenService;

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String past_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(past_token);
                String email = decodedJWT.getSubject();

                UserEntity user = userService.getUser(email);//유저를 찾는다
                tokenService.refreshToken(user, response);

                Map<String, String> message = new HashMap<>();
                message.put("message","Refresh The Token");

                new ObjectMapper().writeValue(response.getOutputStream(), message); //토큰 전송

            }catch (Exception exception) {
                response.setHeader("error", "Refresh_Token has expired");
                response.setStatus(FORBIDDEN.value());

                Map<String, String> error = new HashMap<>();
                error.put("error_message", "Make User Re-Login");
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Access token is missing");
        }
    }

}
