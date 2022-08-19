package com.example.Base.domain.entity;

import com.example.Base.domain.dto.user.UserDTO;
import lombok.*;
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

    private String name;

    @Column(unique = true)
    private String email;

    private String category;
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

    public UserDTO toDTO() {
        UserDTO userDTO = UserDTO.builder()
                .name(name)
                .password(password)
                .email(email)
                .gender(gender)
                .address(address)
                .category1(category1)
                .category2(category2)
                .category3(category3)
                .count1(count1)
                .count2(count2)
                .count3(count3)
                .role(role)
                .build();
        return userDTO;
    }
}
