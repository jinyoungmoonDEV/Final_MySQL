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

    private String gender;

    private String address;

    private String category1;

    private String category2;

    private String category3;

    private Double count1;

    private Integer count2;

    private Integer count3;

    private String role;


    public UserEntity toEntity() {
        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .name(name)
                .password(password)
                .gender(gender)
                .category1(category1)
                .category2(category2)
                .category3(category3)
                .count1(count1)
                .count2(count2)
                .count3(count3)
                .role(role)
                .build();
        return userEntity;
    }
}
