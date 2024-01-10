package org.example.userproject1.validator;

import lombok.RequiredArgsConstructor;
import org.example.userproject1.entity.Phone;
import org.example.userproject1.entity.User;
import org.example.userproject1.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class UserValidator implements Validator<User>{
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);
    private final UserRepository userRepository;
    @Override
    public ValidationResult isValid(User user) {
        ValidationResult validationResult = new ValidationResult();
        Matcher matcher = pattern.matcher(user.getMail());
        if(user.getMail().isEmpty()){
            validationResult.add(Error.of("invalid.mail", "Mail is empty!"));
        } else if (!matcher.matches()) {
            validationResult.add(Error.of("invalid.mail", "Mail is not matches!"));
        } else if (user.getMail().length() > 100) {
            validationResult.add(Error.of("invalid.mail", "Mail is too long!"));
        } else if (!userUniqueCheck(user)) {
            validationResult.add(Error.of("invalid.mail", "Mail is exist!"));
        }
        if(user.getPassword().isEmpty()){
            validationResult.add(Error.of("invalid.password", "Password is empty!"));
        } else if (user.getPassword().length() > 50) {
            validationResult.add(Error.of("invalid.password", "Password is too long!"));
        }
        return validationResult;
    }

    private boolean userUniqueCheck(User user) {
        return userRepository.findAll().stream().noneMatch(o -> o.getMail().equals(user.getMail())
                && !o.getUser_id().equals(user.getUser_id()));
    }
}
