package com.example.Base.domain.entity;

import com.example.Base.domain.dto.UserDTO;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private String password;
    private String role;

    public UserDTO toDTO() {
        UserDTO userDTO = UserDTO.builder()
                .name(name)
                .password(password)
                .email(email)
                .role(role)
                .build();
        return userDTO;
    }
}
