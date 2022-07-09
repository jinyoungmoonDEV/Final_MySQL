package com.example.Base;

import com.example.Base.domain.RoleEntity;
import com.example.Base.domain.UserEntity;
import com.example.Base.service.UserService;
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

			userService.saveUser(new UserEntity(null , "Moon","mgy1017@gmail.com","1234", new ArrayList<>()));
			userService.saveUser(new UserEntity(null , "a","a","1234", new ArrayList<>()));
			userService.saveUser(new UserEntity(null , "b","b","1234", new ArrayList<>()));
			userService.saveUser(new UserEntity(null , "c","c","1234", new ArrayList<>()));

			userService.addRoleToUser("mgy1017@gmail.com", "ROLE_USER");
			userService.addRoleToUser("mgy1017@gmail.com", "ROLE_MANAGER");
			userService.addRoleToUser("a", "ROLE_MANAGER");
			userService.addRoleToUser("b", "ROLE_ADMIN");
			userService.addRoleToUser("c", "ROLE_SUPER_ADMIN");
			userService.addRoleToUser("c", "ROLE_ADMIN");
			userService.addRoleToUser("c", "ROLE_ADMIN");
		};
	}
}
