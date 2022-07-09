package com.example.Base.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data //Getter, Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    private String username;
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)//사용자를 로드 할 떄마다 모든역할을 로드하기원하면 EAGER
    private Collection<RoleEntity> roles = new ArrayList<>();
}
