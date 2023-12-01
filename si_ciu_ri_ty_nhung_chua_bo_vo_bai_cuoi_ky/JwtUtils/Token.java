package org.example.Lab9_10.JwtUtils;

import java.io.Serial;
import java.io.Serializable;

public class Token implements Serializable {
    private final String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}