package org.example.userproject1.service;

import lombok.RequiredArgsConstructor;
import org.example.userproject1.customexception.PhoneNotFoundException;
import org.example.userproject1.customexception.UserNotFoundException;
import org.example.userproject1.dto.PhoneDto;
import org.example.userproject1.entity.Phone;
import org.example.userproject1.entity.User;
import org.example.userproject1.repository.PhoneRepository;
import org.example.userproject1.repository.UserRepository;
import org.example.userproject1.validator.PhoneValidator;
import org.example.userproject1.validator.ValidationResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PhoneService {
    private final PhoneRepository phoneRepository;
    private final UserRepository userRepository;
    private final PhoneValidator phoneValidator;
    public List<Phone> userContacts(long id) {
        User user;
        user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id=" + id + " does not exist"));
        return phoneRepository.
                findAll()
                .stream()
                .filter(o -> Objects.equals(o.getUser().getId(), user.getId()))
                .collect(Collectors.toList());
    }

    public PhoneDto saveContact(Phone phone) {
        PhoneDto phoneDto = PhoneDto.builder()
                .phoneNumber(phone.getPhoneNumber())
                .user(phone.getUser())
                .error(new ArrayList<>())
                .build();
        ValidationResult validationResult = phoneValidator.isValid(phone);
        if(validationResult.isValid()){
            phoneDto.setError(validationResult.getErrors());
            return phoneDto;
        }
        phoneRepository.save(phone);
        return phoneDto;
    }

    public void deleteById(long id) {
        if (!phoneRepository.existsById(id)) {
            throw new PhoneNotFoundException("Phone with id=" + id + " does not exist");
        }
        phoneRepository.deleteById(id);
    }

    public Phone getPhone(long id){
        return phoneRepository.findById(id)
                .orElseThrow(() -> new PhoneNotFoundException("Phone with id=" + id + " does not exist"));
    }

}
