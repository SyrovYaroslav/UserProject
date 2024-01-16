package org.example.userproject1.service;

import lombok.RequiredArgsConstructor;
import org.example.userproject1.customexception.UserNotFoundException;
import org.example.userproject1.dto.UserDto;
import org.example.userproject1.entity.User;
import org.example.userproject1.repository.UserRepository;
import org.example.userproject1.validator.UserValidator;
import org.example.userproject1.validator.ValidationResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    private final UserValidator userValidator;
    public Page<User> listAll(int page, int size, String query) {
        return query.isEmpty() ?userRepository.findAll(PageRequest.of(page, size))
                :userRepository.findUsersByQuery(query,PageRequest.of(page, size));
    }

    public List<Integer> pageSlicer(int page, int totalPages) {
        return IntStream.rangeClosed(Math.max(0, page - 2), Math.min(totalPages - 1, page + 2))
                .boxed()
                .collect(Collectors.toList());
    }

    public UserDto saveUser(String email, String password) {
        User user = User.builder()
                .mail(email)
                .password(password)
                .build();
        UserDto userDto = UserDto.builder()
                .mail(email)
                .password(password)
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

    public UserDto saveUser(String email, String password, long id) {
        User user = User.builder()
                .id(id)
                .mail(email)
                .password(password)
                .build();
        UserDto userDto = UserDto.builder()
                .id(id)
                .mail(email)
                .password(password)
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
