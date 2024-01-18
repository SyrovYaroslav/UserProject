package org.example.userproject1.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.userproject1.entity.SecurityUser;
import org.example.userproject1.entity.SecurityUserRole;
import org.example.userproject1.repository.SecurityUserRepository;

import org.example.userproject1.validator.Error;
import org.example.userproject1.validator.SecurityUserValidator;
import org.example.userproject1.validator.ValidationResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class SecurityService {

    private SecurityUserRepository securityUserRepository;
    private PasswordEncoder passwordEncoder;
    private SecurityUserValidator securityUserValidator;
    public List<Error> addNewSecurityUser(String username, String email,
                                           String password, String confirmPassword) {
        SecurityUser securityUser = securityUserRepository.findByUsername(username);

        if (securityUser != null) {
            throw new RuntimeException("SecurityUser exist");
        }

        SecurityUserRole userRole = new SecurityUserRole();
        userRole.setRole("USER");
        SecurityUser user = SecurityUser.builder()
                .userId(UUID.randomUUID().toString())
                .username(username)
                .password(password)
                .email(email)
                .roles(List.of(userRole))
                .build();
        ValidationResult validationResult = securityUserValidator.isValid(user);
        if (!password.equals(confirmPassword)) {
            validationResult.add(Error.of("invalid.password", "Password is not match!"));
        }
        if(validationResult.isValid()) {
            return validationResult.getErrors();
        }
        user.setPassword(passwordEncoder.encode(password));
        securityUserRepository.save(user);
        return validationResult.getErrors();
    }

    public Page<SecurityUser> listAllSecurityUsers(int page, int size) {
        return securityUserRepository.findAll(PageRequest.of(page, size));
    }

    public SecurityUser loadUserByUsername(String username){
        return securityUserRepository.findByUsername(username);
    }
}
