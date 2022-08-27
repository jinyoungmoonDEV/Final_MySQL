package com.example.Base.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.Base.domain.dto.error.ResponseDTO;
import com.example.Base.domain.dto.user.UserDTO;
import com.example.Base.domain.entity.UserEntity;
import com.example.Base.service.token.TokenServiceImpl;
import com.example.Base.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Log4j2
@RestController // @Controller + @ResponseBody
@RequiredArgsConstructor //생성자 주입
@RequestMapping("/user")//아래에 있는 모든 mapping은 문자열/api를 포함해야한다.
public class UserController {
    private final UserService userService;
    private final TokenServiceImpl tokenService;

    @PostMapping(value = "/signin")
    //ResponseEntity는  httpentity를 상속받는 결과 데이터와 HTTP 상태 코드를 직접 제어할 수 있는 클래스이고, 응답으로 변환될 정보를 모두 담은 요소들을 객체로 사용 된다.
    public ResponseEntity login(@RequestBody  UserDTO userDTO, HttpServletResponse response){
        try {
             tokenService.loginMethod(userDTO, response);

            Cookie cookie = new Cookie("Cookie","forSecure");

            cookie.setMaxAge(7*24*60*60);
            cookie.setHttpOnly(true); //token 쿠키 저장 방식의 csrf 취약 문제 방지 위해  httponly true 설정
            cookie.setSecure(true); //security : true
            cookie.setPath("/");

            response.addCookie(cookie);

            return ResponseEntity.ok().body("SignIn Success");

        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity saveUser(@RequestBody UserDTO userDTO) {
        try {
            userDTO.setRole("ROLE_USER");

            return ResponseEntity.created(URI.create("/user/signup")).body(userService.saveUser(userDTO)); //201 Created => HTTP 201 Created는 요청이 성공적으로 처리되었으며, 자원이 생성되었음을 나타내는 성공 상태 응답 코드(URI 필요)

        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PostMapping("/gosu/signup")
    public ResponseEntity saveHelper(@RequestBody UserDTO userDTO) {
        try {
            userDTO.setRole("ROLE_GOSU");

            return ResponseEntity.created(URI.create("/user/gosu/signup")).body(userService.saveUser(userDTO)); //201 Created => HTTP 201 Created는 요청이 성공적으로 처리되었으며, 자원이 생성되었음을 나타내는 성공 상태 응답 코드(URI 필요)

        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @GetMapping("/client/info") //클라이언트의 모든 정보
    public ResponseEntity userinfo(HttpServletRequest request){
        return ResponseEntity.ok().body(userService.clientInfo(request));
    }

    @PutMapping("/gosu/rating") //전문가 회원
    public ResponseEntity gosuRating(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok().body(userService.gosuRating(userDTO));
    }

//----------------------------------------------------------------------------------------------------------------------------------------------------------

    @GetMapping(value = "/check")
    public ResponseEntity checkUser(HttpServletRequest request) {

        UserDTO result = tokenService.decodeJWT(request);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refresh_token = request.getHeader(AUTHORIZATION);
        if(refresh_token != null && refresh_token.startsWith("Bearer ")) {
            try {
                String past_token = refresh_token.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(past_token);

                String email = decodedJWT.getSubject();

                UserEntity user = userService.getUser(email);//유저를 찾는다
                tokenService.refreshToken(user.toDTO(), response);

                Map<String, String> message = new HashMap<>();
                message.put("message","Refresh The Token");

                new ObjectMapper().writeValue(response.getOutputStream(), message); //토큰 전송

            }catch (Exception exception) {

                if (exception.getMessage().startsWith("The Token's Signature")) { //조작된 토큰 일때
                    log.error("Error logging in: {}", exception.getMessage());
                    response.setHeader("error", "Incorrect Token Do Re-Login");
                }

                else if (exception.getMessage().startsWith("The Token has expired")) {
                    log.error("Error logging in: {}", exception.getMessage());
                    response.setHeader("error", "Token Has Expired Do Refresh");
                }

                else {
                    log.error("Error logging in: {}", exception.getMessage());
                    response.setHeader("error", "Unexpected Error...");
                }

                response.setStatus(FORBIDDEN.value()); //forbidden error code로 보낸다.

                //response.sendError(FORBIDDEN.value());

                Map<String, String> error = new HashMap<>();

                error.put("error_message", "Make User Re-Login");

                response.setContentType(APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Access token is missing");
        }
    }
}
