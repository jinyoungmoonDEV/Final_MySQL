package com.example.Base.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum roles {
    ROLE_ADMIN, ROLE_USER, ROLE_SUPER_ADMIN, ROLE_MANAGER;
}
