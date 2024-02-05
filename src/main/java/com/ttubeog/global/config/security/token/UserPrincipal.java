package com.ttubeog.global.config.security.token;

import com.ttubeog.domain.member.domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class UserPrincipal implements OAuth2User, UserDetails{

    private final Member member;

    private final Long id;
    private final String email;
    private final String password;
    @Getter
    private Map<String, Object> attributes;

    public UserPrincipal(Member member, Long id, String email, String password) {
        this.member = member;
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static UserPrincipal create(Member member) {
        return new UserPrincipal(
                member,
                member.getId(),
                member.getEmail(),
                member.getPassword()
        );
    }


    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }

    @Override
    public String getUsername() {
        return email;
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
