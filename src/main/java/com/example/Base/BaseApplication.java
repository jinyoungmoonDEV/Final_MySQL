package com.example.Base;

import com.example.Base.domain.dto.user.UserDTO;
import com.example.Base.domain.entity.RoleEntity;
import com.example.Base.service.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaseApplication.class, args);
	}
	@Bean
	PasswordEncoder passwordEncoder() { //application실행 할 떄 마다
		return new BCryptPasswordEncoder();
	}

//	@Bean //spring이 pickup하게
//	CommandLineRunner run(UserService userService) {
//		return args -> {
//			userService.saveRole(new RoleEntity(null, "ROLE_USER"));
//			userService.saveRole(new RoleEntity(null, "ROLE_GOSU"));
//			userService.saveRole(new RoleEntity(null, "ROLE_ADMIN"));
//
//			userService.saveUser(new UserDTO(null , "user","user@gmail.com","a","1234","a","b","c","d","e",1.1,2,3,"ROLE_USER"));
//			userService.saveUser(new UserDTO(null , "user2","user2@gmail.com","a","1234","a","b","c","d","e",1.1,2,3,"ROLE_USER"));
//			userService.saveUser(new UserDTO(null , "user3","user3@gmail.com","a","1234","a","b","c","d","e",1.1,2,3,"ROLE_USER"));
//			userService.saveUser(new UserDTO(null , "gosu","gosu@gmail.com","a","1234","a","b","c","d","e",1.1,2,3,"ROLE_GOSU"));
//			userService.saveUser(new UserDTO(null , "admin","admin@gmail.com","a","1234","a","b","c","d","e",1.1,2,3,"ROLE_ADMIN"));
//
//		};
//	}
}
