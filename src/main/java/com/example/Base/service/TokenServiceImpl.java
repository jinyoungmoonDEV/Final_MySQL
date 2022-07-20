package com.example.Base.service;

import com.example.Base.domain.entity.TokenEntity;

public class TokenServiceImpl implements TokenService{
    @Override
    public TokenEntity checkExpiration(String accesstoken) {
        try{
            //유효 한지 검사
            //유효하면 do filter
            return null;
        } catch (Exception e){
            //유효 하지 않으면
            //re 유효한지 검사

            //유효 하면 ac와 re 정보 비교
            //같으면 토큰 재발급
            //다르면 error return 다시 로그인! -> token 삭제
            
            //유효하지 않으면 다시 로그인 요청 으로 리턴
            return null;
        }
    }
}
