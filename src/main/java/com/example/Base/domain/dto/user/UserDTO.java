package com.example.Base.domain.dto.user;

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
    private String name; //이름
    private Integer age; //나이
    private Integer career; //경력
    private String email; //이메일
    private String category; //카테고리
    private String password; //비밀번호
    private String gender; //성별
    private String address; //주소
    private String profileImageURL; //프로필 이미지 주소
//    private String category1;
//    private String category2;
//    private String category3;
    private Double count1; //별점
    private Integer count2;
    private Integer count3;
    private String role; //권한

    public UserEntity toEntity() {
        UserEntity userEntity = UserEntity.builder()
                .id(id)
                .email(email)
                .name(name)
                .age(age)
                .career(career)
                .password(password)
                .gender(gender)
                .address(address)
                .category(category)
                .profileImageURL(profileImageURL)
//                .category1(category1)
//                .category2(category2)
//                .category3(category3)
//                .count1(count1)
//                .count2(count2)
//                .count3(count3)
                .role(role)
                .build();
        return userEntity;
    }
}
