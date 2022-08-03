package com.example.Base.domain.dto;

import com.example.Base.domain.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String name;
    private String email;
    private String password;
    private String role;


    public UserEntity toEntity() {
        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .name(name)
                .password(password)
                .role(role)
                .build();
        return userEntity;
    }
}
