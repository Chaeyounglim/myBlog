package com.sparta.myblog.exception;

import io.jsonwebtoken.JwtException;

public class TokenNotValidateException extends JwtException {
    public TokenNotValidateException(String message) {
        super(message);
    }
}
