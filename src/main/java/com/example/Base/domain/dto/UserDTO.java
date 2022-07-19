package com.example.Base.domain.dto;

import com.example.Base.domain.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String username;
    private String email;
    private String password;


    public UserEntity toEntity() {
        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .username(username)
                .password(password)
                .build();
        return userEntity;
    }
}
