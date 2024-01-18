package org.example.userproject1.validator;

import lombok.RequiredArgsConstructor;
import org.example.userproject1.entity.SecurityUser;
import org.example.userproject1.repository.SecurityUserRepository;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class SecurityUserValidator implements Validator<SecurityUser>{
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);
    private final SecurityUserRepository securityUserRepository;
    @Override
    public ValidationResult isValid(SecurityUser securityUser) {
        ValidationResult validationResult = new ValidationResult();
        Matcher matcher = pattern.matcher(securityUser.getEmail());
        if(securityUser.getEmail().isEmpty()){
            validationResult.add(Error.of("invalid.mail", "Mail is empty!"));
        } else if (!matcher.matches()) {
            validationResult.add(Error.of("invalid.mail", "Mail is not matches!"));
        } else if (securityUser.getEmail().length() > 100) {
            validationResult.add(Error.of("invalid.mail", "Mail is too long!"));
        }
        if (!userUniqueCheck(securityUser)) {
            validationResult.add(Error.of("invalid.username", "Username is exist!"));
        }
        if(securityUser.getPassword().isEmpty()){
            validationResult.add(Error.of("invalid.password", "Password is empty!"));
        } else if (securityUser.getPassword().length() > 50) {
            validationResult.add(Error.of("invalid.password", "Password is too long!"));
        }
        return validationResult;
    }

    private boolean userUniqueCheck(SecurityUser securityUser) {
        return securityUserRepository.findAll().stream().noneMatch(o -> o.getUsername().equals(securityUser.getUsername())
                && !o.getUserId().equals(securityUser.getUserId()));
    }
}
