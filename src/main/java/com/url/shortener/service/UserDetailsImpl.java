package com.url.shortener.service;

import com.url.shortener.models.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Data
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {


    private static final long serialVersionID=1L;

    private Long id;
    private String username;
    private String email;
    private String password;

    private Collection<? extends GrantedAuthority > authorities;

    public UserDetailsImpl(Long id, String username, String email, String password,Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    //It will convert our database user object into USerDetailsImpl object for spring security
    //Spring security make use of USerDetailsImpl
    public static UserDetailsImpl build(User user){
        GrantedAuthority authority=new SimpleGrantedAuthority(user.getRole()); // Spring security doesn't understand role of our user that's why we are using GrantedAuthority
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(authority)
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
