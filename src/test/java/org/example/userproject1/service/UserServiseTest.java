package org.example.userproject1.service;

import org.example.userproject1.entity.User;
import org.example.userproject1.repository.UserRepository;
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
public class UserServiseTest {

    @InjectMocks
    private UserServise userServise;

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

        List<User> result = userServise.listAll();

        assertEquals(userList, result);
    }

    @Test
    void createUserTest(){
        Mockito.when(userRepository.save(user1)).thenReturn(user1);

        User createdUser = userServise.create(user1);

        assertEquals(user1, createdUser);
    }

    @Test
    void deleteByIdUserTest(){
        userRepository.deleteById(user1.getUser_id());
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(user1.getUser_id());
    }

    @Test
    void getUserTest(){
        Mockito.when(userRepository.findById(user1.getUser_id())).thenReturn(Optional.of(user1));
        User result = userServise.getUser(user1.getUser_id());
        assertEquals(user1, result);
    }

    @Test
    void getUserExceptionTest(){
        Mockito.when(userRepository.findById(user1.getUser_id())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userServise.getUser(user1.getUser_id()));
    }

    @Test
    void updateUserTest(){
        Mockito.when(userRepository.existsById(user1.getUser_id())).thenReturn(true);
        userServise.update(user1);
        Mockito.verify(userRepository, Mockito.times(1)).save(user1);
    }

    @Test
    void updateNotExistUserTest(){
        Mockito.when(userRepository.existsById(user1.getUser_id())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> userServise.update(user1));
    }
}
