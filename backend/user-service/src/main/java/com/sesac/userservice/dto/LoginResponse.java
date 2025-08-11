package com.sesac.userservice.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String name;

    public LoginResponse(String token, Long id, String email, String name) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.name = name;
    }
}
