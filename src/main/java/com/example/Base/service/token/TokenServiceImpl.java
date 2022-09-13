package com.example.Base.service.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.Base.domain.dto.user.UserDTO;
import com.example.Base.domain.entity.UserEntity;
import com.example.Base.repository.UserRepository;
import com.example.Base.JWT.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.BufferOverflowException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
@Log4j2
public class TokenServiceImpl implements TokenService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    @Override
    public void loginMethod(UserDTO userDTO, HttpServletResponse response) {

        String email = userDTO.getEmail();

        UserEntity info = userRepository.findByEmail(email);

        if(info == null){
            throw new UsernameNotFoundException("User not found in the database");
        }

        else {
            String password = info.getPassword();
            boolean verify = passwordEncoder.matches(userDTO.getPassword(), password);

            if(verify){
                tokenProvider.createToken(info.toDTO(), response);
            }

            else {
                throw new RuntimeException("WrongPassword");
            }
        }
    }

    @Override
    public void refreshToken(UserDTO user, HttpServletResponse response) {
        tokenProvider.createToken(user, response);
    }

    @Override
    public UserDTO decodeJWT(HttpServletRequest request) {

        String access_token = request.getHeader(AUTHORIZATION);

        if(access_token != null && access_token.startsWith("Bearer ")) {
            try {
                String token = access_token.substring("Bearer ".length());

                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

                JWTVerifier verifier = JWT.require(algorithm).build();

                DecodedJWT decodedJWT = verifier.verify(token);

                String email = decodedJWT.getSubject();
                String name = decodedJWT.getIssuer();
                String role = decodedJWT.getClaim("role").toString().replace("\"", "");

                log.info(name);
                UserDTO userDTO = UserDTO.builder()
                        .email(email)
                        .name(name)
                        .role(role)
                        .build();

                return userDTO;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else {
            throw new RuntimeException("OMG");
        }
    }
}
