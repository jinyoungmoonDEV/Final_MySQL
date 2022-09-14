package com.example.Base.service.user;

import com.example.Base.domain.dto.user.UserDTO;
import com.example.Base.domain.entity.UserEntity;
import com.example.Base.repository.UserRepository;
import com.example.Base.service.token.TokenServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor//종속성 주입
@Transactional//모든 클래스가 트랜잭션 되야함 -> 모든 작업들이 성공적이여만 묶음 결과를 적용 -> 에러가 나면 모든 작업들이 다시 원 상태로 되돌릴수 있다.
@Log4j2
public class UserServiceImpl implements UserService/*, UserDetailsService*/ { //UserDetailsService에서 loadUserByUsername메소드 Override

    private final UserRepository userRepository;

    private final TokenServiceImpl tokenService;

    private final PasswordEncoder passwordEncoder;


//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { //DB에서 유저 정보를 불러오는 중요한 메소드
//        UserEntity user = userRepository.findByEmail(email);
//        if(user == null) {
//            log.error("User not found in the database");
//            throw new UsernameNotFoundException("User not found in the database");
//        } else {
//            log.info("User found in the database: {}",email);
//        }
//        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>(); //Collection은 데이터 집합,그룹
//        authorities.add(new SimpleGrantedAuthority(user.getRole()));
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
//    }

    @Override
    public List<UserEntity> getUsers() { //모든 user 불러온다
        return userRepository.findAll();
    }

    @Override
    public UserEntity getUser(String email) {//email로 유저 정보 가져오기
        return userRepository.findByEmail(email);
    }

    @Override
    public String getName(String email) {

        UserEntity info = userRepository.findByEmail(email);
        try {
            return info.getName();

        } catch (Exception e) {
            Map<String,String> map = new HashMap<>();
            return map.put("회원 여부", "없는 회원입니다.");
        }
    }

    @Override
    public UserEntity saveUser(final UserDTO user) { //유저 정보 DB에 저장

        if (userRepository.findByEmail(user.getEmail()) == null && userRepository.findByName(user.getName()) == null)
        {
            log.info("Saving new user {} to the database", user.getName());

            user.setPassword(passwordEncoder.encode(user.getPassword())); //password encode
            UserEntity userEntity = user.toEntity();

            return userRepository.save(userEntity);
        }
        else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User Info Already Exist");
        }
    }

   // token, Cookie , Yap

    @Override
    public UserEntity clientInfo(HttpServletRequest request) {

        UserDTO info = tokenService.decodeJWT(request);

        String email = info.getEmail();

        UserEntity user = userRepository.findByEmail(email);

        return user;
    }

    @Override
    public String gosuRating(UserDTO userDTO) {

        String email = userDTO.getEmail();

        UserEntity info = userRepository.findByEmail(email);

        UserDTO update = info.toDTO();

        update.setCount1(userDTO.getCount1());//별점계산이랑 다른 정수 카운트 메서드 실행 후 update 수정 필요
        update.setCount2(userDTO.getCount2());
        update.setCount3(userDTO.getCount3());

        userRepository.save(update.toEntity());

        return "Rating Success";
    }

    @Override
    public String getCategory(String email) {

        UserEntity info = userRepository.findByEmail(email);
        String category = info.getCategory();

        return category;
    }

    @Override
    public List getAllExpertsByCategory(String category) {

        List ls = new ArrayList();
        List<UserEntity> userEntities = userRepository.findAllByCategory(category);

        for (int i=0; i<userEntities.size(); i++) {
            ls.add(userEntities.get(i).getEmail());
        }

        return ls;
    }

    // 주소를 리스트 배열에 담아서 리턴 -> 인덱스로 찾아서 String 으로 뽑으면 됨
    @Override
    public List splitString(String expertAddress) {

        String excepts = "[^\uAC00-\uD7A30-9a-zA-Z,]";
        String cleanAddr = expertAddress.replaceAll(excepts, "");
        String[] eachAddressList = cleanAddr.split(",");

        List ls = new ArrayList();

        for (int i=0; i<eachAddressList.length; i++) {
            String addr = eachAddressList[i];
            ls.add(addr);
        }
        return ls;
    }

}
