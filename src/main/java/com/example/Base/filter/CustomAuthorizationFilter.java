package com.example.Base.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter { //OncePerRequestFilter가 application에 오는 모든 request를 다 인터셉트함(토큰확인 후 서비스 하기위함)

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/token/refresh")) { //로그인이나 토큰 재발급은 필터를 거칠 필요없어서 바로 보내버린다.
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);//token의 key를 확인하기위해 header를 불러오는 변수 생성
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) { //header에 값이 있고, Bearer로 시작할떄(client에서 Bearer에 한칸 띄우고 토큰을 보냄, 문자열을 넣는 이유는 우리의 토큰 이라는 것을 인증 하기 위해서)
                try {
                    String token = authorizationHeader.substring("Bearer ".length()); //token만 가져오기 위해서 앞에 적어두었던 문자열 뺴고 변수에 담는다.

                    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());// 토큰 생성한거와 같은 알고리즘 사용 해야한다(인증을 위해서)

                    JWTVerifier verifier = JWT.require(algorithm).build(); //기존 알고리즘과 같은 JWTVerifier의 verifier 변수에 빌드

                    DecodedJWT decodedJWT = verifier.verify(token); //verifier에 있는 알고리즘으로 token decode한다

                    String email = decodedJWT.getSubject(); //decode해서 유저정보를 얻어오는 변수

                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class); //roles라는 key값의 value는 유저의 권한이 있으므로 권한을 가져오는 변수, 권한이 여러개 일 수 있으므로 배열 형식

                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>(); //authorities 변수를 Collection형식으로 생성, Parameter를 SimpleGrantedAuthority로 하는 이유는
                                                                                        // security에서의 권한은 GrantedAuthority 인터페이스를 구현한 객체로 만들면되는데 총 4가지중 문자열만 저장하는 SimpleGrandAuthority사용 하였다

                    stream(roles).forEach(role -> { //stream을 이용해서 함수형으로 authorities 변수에 권한 정보 add한다.
                        authorities.add(new SimpleGrantedAuthority(role));
                    });

                    UsernamePasswordAuthenticationToken authenticationToken = //UsernamePasswordAuthenticationToken를 쓰는 이유는 security에 유저정보 전달을 위해서
                            new UsernamePasswordAuthenticationToken(email,null, authorities); //유저정보(email, 권한) 정보를 담은 authenticationToken 변수 생성, password는 필요없어서 null

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken); //갖고있는 정보를 토대로 security가 권한에따라서 접근 할수 있는지 없는지 판단하기 위해 authention으로 설정, SecurityContextHolder는 security가 인증한 내용들을 갖고 있는것

                    filterChain.doFilter(request, response);//요청이 계속 지속 되어야 하므로 filterchain 호출한다.

                }catch (Exception exception) {
                    log.error("Error logging in: {}", exception.getMessage());

                    response.setHeader("error", exception.getMessage()); //header에  문자열"error" 보낸다.

                    response.setStatus(FORBIDDEN.value()); //forbidden error code로 보낸다.

                    //response.sendError(FORBIDDEN.value());

                    Map<String, String> error = new HashMap<>();

                    error.put("error_message", exception.getMessage()); //문자열과 함께 error메세지 저장

                    response.setContentType(APPLICATION_JSON_VALUE);

                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
