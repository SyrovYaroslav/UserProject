package org.example.userproject1.service;

import org.example.userproject1.customexception.UserNotFoundException;
import org.example.userproject1.dto.UserDto;
import org.example.userproject1.entity.User;
import org.example.userproject1.repository.UserRepository;
import org.example.userproject1.validator.Error;
import org.example.userproject1.validator.UserValidator;
import org.example.userproject1.validator.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserValidator userValidator;
    @Mock
    private UserRepository userRepository;

    private User user1;

    @BeforeEach
    void setUp(){
        user1 = new User(1L, "ascasca@gmail.com", "1233");
    }

    @Test
    void listAllUserTest(){

        User user2 = new User(2L, "ascasc3@gmail.com", "1233");

        List<User> userList = List.of(user1, user2);

        Mockito.when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.listAll();

        assertEquals(userList, result);
    }

    @Test
    void saveUserTest(){
        User user = User.builder()
                .mail("ascasca@gmail.com")
                .password("1233")
                .build();
        ValidationResult validationResult = new ValidationResult();
        Mockito.when(userValidator.isValid(user)).thenReturn(validationResult);

        UserDto userDto = UserDto.builder()
                .mail(user.getMail())
                .password(user.getPassword())
                .error(validationResult.getErrors())
                .build();

        Mockito.when(userRepository.save(user)).thenReturn(user);

        UserDto createdUserDto = userService.saveUser("ascasca@gmail.com", "1233");

        assertEquals(userDto, createdUserDto);
    }

    @Test
    void saveNonValidUserTest(){
        User user = User.builder()
                .mail("ascascagmail.com")
                .password("1233")
                .build();
        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of("invalid.mail", "Mail is empty!"));
        Mockito.when(userValidator.isValid(user)).thenReturn(validationResult);

        UserDto userDto = UserDto.builder()
                .mail(user.getMail())
                .password(user.getPassword())
                .error(validationResult.getErrors())
                .build();

        UserDto createdUserDto = userService.saveUser("ascascagmail.com", "1233");

        assertEquals(userDto, createdUserDto);
    }

    @Test
    void deleteByIdUserTest(){
        userRepository.deleteById(user1.getId());
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(user1.getId());
    }
    @Test
    void deleteByIdUserExceptionTest(){
        userRepository.deleteById(user1.getId());
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(user1.getId());
        assertThrows(UserNotFoundException.class, () -> userService.deleteById(user1.getId()));
    }

    @Test
    void getUserTest(){
        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        User result = userService.getUser(user1.getId());
        assertEquals(user1, result);
    }

    @Test
    void getUserExceptionTest(){
        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUser(user1.getId()));
    }

    @Test
    void updateUserTest(){
        ValidationResult validationResult = new ValidationResult();
        Mockito.when(userValidator.isValid(user1)).thenReturn(validationResult);

        UserDto userDto = UserDto.builder()
                .id(user1.getId())
                .mail(user1.getMail())
                .password(user1.getPassword())
                .error(validationResult.getErrors())
                .build();

        Mockito.when(userRepository.save(user1)).thenReturn(user1);

        UserDto createdUserDto = userService.saveUser("ascasca@gmail.com", "1233", 1L);

        assertEquals(userDto, createdUserDto);
    }

    @Test
    void updateNotExistUserTest(){
        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of("invalid.mail", "Mail is empty!"));
        Mockito.when(userValidator.isValid(user1)).thenReturn(validationResult);

        UserDto userDto = UserDto.builder()
                .id(user1.getId())
                .mail(user1.getMail())
                .password(user1.getPassword())
                .error(validationResult.getErrors())
                .build();

        UserDto createdUserDto = userService.saveUser("ascasca@gmail.com", "1233", 1L);

        assertEquals(userDto, createdUserDto);
    }
}
