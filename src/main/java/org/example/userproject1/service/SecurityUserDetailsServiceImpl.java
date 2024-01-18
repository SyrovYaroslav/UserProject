package org.example.userproject1.service;

import lombok.AllArgsConstructor;
import org.example.userproject1.entity.SecurityUser;
import org.example.userproject1.entity.SecurityUserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SecurityUserDetailsServiceImpl implements UserDetailsService {
    private SecurityService securityService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUser securityUser = securityService.loadUserByUsername(username);
        if(securityUser == null){
            throw new UsernameNotFoundException(String.format("User %s not found", username));
        }
        return User
                .withUsername(securityUser.getUsername())
                .password(securityUser.getPassword())
                .roles((securityUser.getRoles().stream().findAny().get().toString()))
                .build();
    }
}
