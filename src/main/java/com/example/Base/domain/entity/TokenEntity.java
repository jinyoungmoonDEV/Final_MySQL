package com.example.Base.domain.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "token")
public class TokenEntity {
    @Id
    private String email;

    private String refreshtoken;

}
