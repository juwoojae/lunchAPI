package com.example.springjwt.dto;

import com.example.springjwt.entity.UserEntity;
import com.example.springjwt.jwt.RoleUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    /**
     * -- GETTER --
     *   UserDetails 에서 Role 꺼내오기
     */
    private final UserEntity userEntity;

    public String getRole() {
        return userEntity.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities  = new ArrayList<>();
        String role = userEntity.getRole();
        authorities.add(() -> RoleUtil.toRole(role));
        return authorities;
    }

    @Override
    public String getPassword() {
       return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
       return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
