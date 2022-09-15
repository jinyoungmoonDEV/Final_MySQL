package com.example.Base.security;

import com.example.Base.security.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration //Spring이 설정 파일 or Bean을 만들기 위함
@EnableWebSecurity //FilterChain자동 포함
@RequiredArgsConstructor //생성자 주입
public class SecurityConfig extends WebSecurityConfigurerAdapter { //WebSecurityConfigurerAdapter Override하기 위해 상속

//    private final UserDetailsService userDetailsService; //DB에서 유저 정보를 직접 가져오는 인터페이스
//    private final BCryptPasswordEncoder bCryptPasswordEncoder; //비밀 번호 암호화(BCrypt 해싱 함수), 사용자의 의해 제출된 비밀번호와 저장소에 저장되어 있는 비밀번호의 일치 여부 확인 메서드 제공
//    //main application에 PasswordEncoder를 new BCryptPasswordEncoder Bean으로 지정
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);//userDetailsService로 security에서 유저정보 가져온다
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable(); //csrf방지
        http.cors(); //서로 출처가 다른 웹 애플리케이션에서 자원을 공유하는 것, react 연동시 proxy 설정

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//session사용 안하므로 STATELESS로 끄기

        http.authorizeRequests().antMatchers( "/user/check","/user/token/refresh","/category/**", "/chat/**","/test/subscribe/**", "/user/kakao/**", "/survey/**", "/quotationSubmit/**", "/matchedList/**", "/matchedgosulist/**").permitAll();
        http.authorizeRequests().antMatchers().hasAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(GET, "/user/**").hasAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST, "/user/**").permitAll();
        http.authorizeRequests().antMatchers("/gosu/**", "/quotation/**").hasAuthority("ROLE_GOSU");
        http.authorizeRequests().anyRequest().authenticated(); //나머지 리퀘스트들은 인증이 필요하다

        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class); //지정된 필터 앞에 커스텀 필터를 추가 하여 먼저 실행
    }

//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
}
