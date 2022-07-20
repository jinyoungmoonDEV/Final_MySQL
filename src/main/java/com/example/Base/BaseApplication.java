package com.example.Base;

import com.example.Base.domain.entity.RoleEntity;
import com.example.Base.domain.entity.UserEntity;
import com.example.Base.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class BaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaseApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() { //application실행 할 떄 마다
		return new BCryptPasswordEncoder();
	}

	@Bean //spring이 pickup하게
	CommandLineRunner run(UserService userService) {
		return args -> {
			userService.saveRole(new RoleEntity(null, "ROLE_USER"));
			userService.saveRole(new RoleEntity(null, "ROLE_MANAGER"));
			userService.saveRole(new RoleEntity(null, "ROLE_ADMIN"));
			userService.saveRole(new RoleEntity(null, "ROLE_SUPER_ADMIN"));

			userService.saveUser(new UserEntity(null , "Moon","admin@gmail.com","1234", new ArrayList<>()));
			userService.saveUser(new UserEntity(null , "a","manager@gmail.com","1234", new ArrayList<>()));
			userService.saveUser(new UserEntity(null , "b","user01@gmail.com","1234", new ArrayList<>()));
			userService.saveUser(new UserEntity(null , "c","super@gmail.com","1234", new ArrayList<>()));

			userService.addRoleToUser("admin@gmail.com", "ROLE_ADMIN");
			userService.addRoleToUser("manager@gmail.com", "ROLE_MANAGER");
			userService.addRoleToUser("super@gmail.com", "ROLE_SUPER_ADMIN");
		};
	}
}
