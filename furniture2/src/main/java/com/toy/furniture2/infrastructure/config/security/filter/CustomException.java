package com.toy.furniture2.infrastructure.config.security.filter;

import org.springframework.security.core.AuthenticationException;

public class CustomException extends AuthenticationException  {
    public CustomException(String msg) {
        super(msg);
    }
}