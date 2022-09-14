package com.example.Base;

import com.example.Base.domain.dto.user.UserDTO;
import com.example.Base.service.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
//
//			userService.saveUser(new UserDTO(null , "user", 20, 3, "user@gmail.com","편의점 알바","1234","a","b","c","d","e",1.1,2,3,"ROLE_USER"));
//			userService.saveUser(new UserDTO(null , "user2",25, 3,"user2@gmail.com","편의점 알바","1234","a","b","c","d","e",1.1,2,3,"ROLE_ADMIN"));
//			userService.saveUser(new UserDTO(null , "user3",30, 3, "user3@gmail.com","편의점 알바","1234","a","b","c","d","e",1.1,2,3,"ROLE_ADMIN"));
//			userService.saveUser(new UserDTO(null , "user4",30, 3,"user4@gmail.com","편의점 알바","1234","a","b","c","d","e",1.1,2,3,"ROLE_ADMIN"));
//			userService.saveUser(new UserDTO(null , "user5",30, 3,"user5@gmail.com","편의점 알바","1234","a","b","c","d","e",1.1,2,3,"ROLE_ADMIN"));
//
//			userService.saveUser(new UserDTO(null , "gosu",35, 3,"gosu@gmail.com","편의점 알바","1234","a","강남구","c","d","e",1.1,2,3,"ROLE_GOSU"));
//			userService.saveUser(new UserDTO(null , "gosu2",35, 3,"gosu2@gmail.com","편의점 알바","1234","a","b","c","d","e",1.1,2,3,"ROLE_GOSU"));
//			userService.saveUser(new UserDTO(null , "gosu3",35, 3,"gosu3@gmail.com","편의점 알바","1234","a","b","c","d","e",1.1,2,3,"ROLE_GOSU"));
//			userService.saveUser(new UserDTO(null , "gosu4",35, 5,"gosu4@gmail.com","편의점 알바","1234","a","b","c","d","e",1.1,2,3,"ROLE_GOSU"));
//			userService.saveUser(new UserDTO(null , "admin",40, 3,"admin@gmail.com","편의점 알바","1234","a","a","c","d","e",1.1,2,3,"ROLE_ADMIN"));
//
//		};
//	}
}
