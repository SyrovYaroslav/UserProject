package org.example.userproject1.service;

import lombok.RequiredArgsConstructor;
import org.example.userproject1.customexception.UserNotFoundException;
import org.example.userproject1.dto.UserDto;
import org.example.userproject1.entity.User;
import org.example.userproject1.repository.UserRepository;
import org.example.userproject1.validator.UserValidator;
import org.example.userproject1.validator.ValidationResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    private final UserValidator userValidator;
    public List<User> listAll() {
        return new ArrayList<>(userRepository.findAll());
    }

    public UserDto saveUser(User user) {
        UserDto userDto = UserDto.builder()
                .mail(user.getMail())
                .password(user.getPassword())
                .error(new ArrayList<>())
                .build();
        ValidationResult validationResult = userValidator.isValid(user);
        if(validationResult.isValid()){
            userDto.setError(validationResult.getErrors());
            return userDto;
        }
        userRepository.save(user);
        return userDto;
    }

    public void deleteById(Long id){
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with id=" + id + " does not exist");
        }
        userRepository.deleteById(id);
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id=" + id + " does not exist"));
    }

}
