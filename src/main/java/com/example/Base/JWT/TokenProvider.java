package com.example.Base.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.Base.domain.dto.user.UserDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
@Log4j2
public class TokenProvider {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public void createToken(UserDTO user, HttpServletResponse response) {

        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes()); //token 생성 알고리즘

        String access_token = JWT.create() //access token 생성
                .withSubject(user.getEmail())//이름을 유일한 유저 정보로 하여 토큰의 중복 방지
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 *1000)) //기간 설정 -> 지금으로 부터 + ???
                .withIssuer(user.getName())
                .withClaim("role", user.getRole())
                .sign(algorithm);

        String refresh_token = JWT.create() //refresh token 생성
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + 300 * 60 *1000))
                .withIssuer(user.getName())
                .withClaim("role", user.getRole())
                .sign(algorithm);

        response.setHeader("access_token", access_token);
        response.setHeader("refresh_token", refresh_token);
    }
}
