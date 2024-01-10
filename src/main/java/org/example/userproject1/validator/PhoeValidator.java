package org.example.userproject1.validator;

import lombok.RequiredArgsConstructor;
import org.example.userproject1.entity.Phone;
import org.example.userproject1.repository.PhoneRepository;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class PhoeValidator implements Validator<Phone>{
    private static final String PHONE_REGEX =
            "^\\+?[0-9]+$";
    private static final Pattern pattern = Pattern.compile(PHONE_REGEX);
    private final PhoneRepository phoneRepository;
    @Override
    public ValidationResult isValid(Phone phone) {
        ValidationResult validationResult = new ValidationResult();
        Matcher matcher = pattern.matcher(phone.getPhone());
        if(phone.getPhone().isEmpty()){
            validationResult.add(Error.of("invalid.phone", "Phone is empty!"));
        } else if (!matcher.matches()) {
            validationResult.add(Error.of("invalid.phone", "Phone is not matches!"));
        } else if (phone.getPhone().length() > 15) {
            validationResult.add(Error.of("invalid.phone", "Phone is too long!"));
        } else if (!phoneUniqueCheck(phone)) {
            validationResult.add(Error.of("invalid.phone", "Phone is exist!"));
        }
        return validationResult;
    }

    private boolean phoneUniqueCheck(Phone phone) {
        return phoneRepository.findAll().stream().noneMatch(o -> o.getPhone().equals(phone.getPhone())
                && !o.getId().equals(phone.getId()));
    }
}
