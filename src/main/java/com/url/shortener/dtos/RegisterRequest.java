package com.url.shortener.dtos;

import lombok.Data;

import java.util.Set;

//Data transfer object - dto --> It defines the structure of the request
@Data
public class RegisterRequest {
    private String username;
    private String email;
    private Set<String> role;
    private String password;
}
