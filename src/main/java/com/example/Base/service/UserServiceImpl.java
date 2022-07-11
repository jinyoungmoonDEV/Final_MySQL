package com.example.Base.service;

import com.example.Base.domain.RoleEntity;
import com.example.Base.domain.UserEntity;
import com.example.Base.repository.RoleRepository;
import com.example.Base.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor//종속성 주입
@Transactional//모든 클래스가 트랜잭션 되야함 -> 모든 작업들이 성공적이여만 묶음 결과를 적용 -> 에러가 나면 모든 작업들이 다시 원 상태로 되돌릴수 있다.
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    
    private final UserRepository userRepository;
    
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email);
        if(user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}",email);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>(); //Collection은 데이터 집합,그룹
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    @Override
    public UserEntity saveUser(UserEntity user) { //유저 정보 DB에 저장
        log.info("Saving new user {} to the database", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword())); //password encode
        return userRepository.save(user);
    }

    @Override
    public RoleEntity saveRole(RoleEntity role) { //role 정보 DB에 저장
        log.info("Saving new user {} to the database", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String email, String roleName) {//user에 role추가
        log.info("Adding role {} to user {}", roleName, email);
        UserEntity user = userRepository.findByEmail(email);//email로 user찾기
        RoleEntity role = roleRepository.findByName(roleName);//rolename으로 role찾기
        user.getRoles().add(role);//user의 role들을 가져오고 -> user에 role 추가, @Transactional -> 모두 성공 or 모두 실패
    }

    @Override
    public UserEntity getUser(String email) {//email로 유저 정보 가져오기
        log.info("Fetching user {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserEntity> getUsers() { //모든 user 불러온다
        log.info("Fetching all users");
        return userRepository.findAll();
    }
}
