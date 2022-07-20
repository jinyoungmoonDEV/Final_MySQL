package com.example.Base.service;

import com.example.Base.domain.entity.TokenEntity;
import org.springframework.stereotype.Service;

public interface TokenService {
    TokenEntity checkExpiration(String accesstoken);
}
