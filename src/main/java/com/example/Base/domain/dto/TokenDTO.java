package com.example.Base.domain.dto;

import com.example.Base.domain.entity.TokenEntity;
import lombok.*;

@Builder
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    private String email;
    private String accesstoken;
    private String refreshtoken;

    public TokenEntity toEntity(){
        TokenEntity tokenEntity = TokenEntity.builder()
                .email(email)
                .accesstoken(accesstoken)
                .refreshtoken(refreshtoken)
                .build();
        return tokenEntity;
    }
}
