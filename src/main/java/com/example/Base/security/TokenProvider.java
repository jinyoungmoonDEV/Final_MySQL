package com.example.Base.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.Base.domain.dto.TokenDTO;
import com.example.Base.domain.dto.UserDTO;
import com.example.Base.domain.entity.TokenEntity;
import com.example.Base.domain.entity.UserEntity;
import com.example.Base.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Log4j2
public class TokenProvider {

    private final TokenRepository tokenRepository;

    public void createToken(UserDTO user, HttpServletResponse response) {

        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes()); //token 생성 알고리즘

        String accesstoken = JWT.create() //access token 생성
                .withSubject(user.getEmail())//이름을 유일한 유저 정보로 하여 토큰의 중복 방지
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 *1000)) //기간 설정 -> 지금으로 부터 + ???
//                .withIssuer()
//                .withClaim("roles", )
                .sign(algorithm);

        String refreshtoken = JWT.create() //refresh token 생성
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 *1000))
//                .withIssuer(request.getRequestURI().toString())
                .sign(algorithm);

        response.setHeader("access_token", accesstoken);
        response.setHeader("refresh_token", refreshtoken);

        TokenDTO tokenDTO = TokenDTO.builder()
                .email(user.getEmail())
                .accesstoken(accesstoken)
                .refreshtoken(refreshtoken)
                .build();
        TokenEntity test = tokenDTO.toEntity();
        String aaa = test.getEmail();
        log.info(test);
        log.info(aaa);

        tokenRepository.save(test);
    }
/*    User user = (User)authentication.getPrincipal(); //로그인 정보로 받아서
    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes()); //token 생성 알고리즘
    String access_token = JWT.create() //access token 생성
            .withSubject(user.getUsername())//이름을 유일한 유저 정보로 하여 토큰의 중복 방지
            .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 *1000)) //기간 설정 -> 지금으로 부터 + ???
            .withIssuer(request.getRequestURL().toString())
            .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
            .sign(algorithm);

    String refresh_token = JWT.create() //refresh token 생성
            .withSubject(user.getUsername())
            .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 *1000))
            .withIssuer(request.getRequestURI().toString())
            .sign(algorithm);

        response.setHeader("access_token", access_token);
        response.setHeader("refresh_token", refresh_token);
    Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", "토큰인줄 알았지?");
        tokens.put("refresh_token", "이것도 아니지롱");
        log.info(refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        chain.doFilter(request, response);*/
}
