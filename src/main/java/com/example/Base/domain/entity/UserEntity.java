package com.example.Base.domain.entity;

import com.example.Base.domain.dto.user.ExpertOnlyDto;
import com.example.Base.domain.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    @Column(unique = true)
    private String name; //이름

    private Integer age; //나이

    @Column(unique = true)
    private String email; //이메일

    private String category; //카테고리
    private String password; //비밀번호
    private Integer career; //경력
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

    public UserDTO toDTO() {
        UserDTO userDTO = UserDTO.builder()
                .name(name)
                .age(age)
                .career(career)
                .password(password)
                .email(email)
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
        return userDTO;
    }

    public ExpertOnlyDto toExpertOnlyDto() {
        ExpertOnlyDto info = ExpertOnlyDto.builder()
                .gosuName(name)
                .gosuAge(age)
                .gosuGender(gender)
                .gosuCategory(category)
                .gosuAddress(address)
                .gosuCareer(career)
                .build();
        return info;
    }
}















