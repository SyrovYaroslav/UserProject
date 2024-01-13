package org.example.userproject1.endpoint;

import org.example.userproject1.entity.User;
import org.example.userproject1.repository.UserRepository;
import org.example.userproject1.service.UserService;
import org.example.userproject1.validator.Error;
import org.example.userproject1.validator.UserValidator;
import org.example.userproject1.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private UserValidator userValidator;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void listUserTest() throws Exception {
        User user1 = new User(1L, "asdasd@gmail.com", "1234");
        User user2 = new User(2L, "asdas4@gmail.com", "12534");

        List<User> userList = List.of(user1, user2);

        Mockito.when(userService.listAll()).thenReturn(userList);

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(view().name("allUserPage"))
                .andExpect(model().attribute("UserList", userList));

        verify(userService, times(1)).listAll();
    }

    @Test
    void getCreateUserTest() throws Exception {
        mockMvc.perform(get("/user/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createUserPage"));
    }

    @Test
    void postCreateUserTest() throws Exception {
        User user1 = new User();
        user1.setMail("asdasd@gmail.com");
        user1.setPassword("1234");
        ValidationResult validationResult = new ValidationResult();
        when(userValidator.isValid(user1)).thenReturn(validationResult);
        when(userService.create(user1)).thenReturn(user1);


        mockMvc.perform(post("/user/create")
                        .param("email", user1.getMail())
                        .param("password", user1.getPassword()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/user"))
                .andExpect(model().attributeDoesNotExist(("errors")));

        verify(userService, times(1)).create(user1);
        verify(userValidator).isValid(user1);
    }

    @Test
    void postCreateNonValidUserTest() throws Exception {
        User user1 = new User();
        user1.setMail("asdas7d@gmail.com");
        user1.setPassword("1234");
        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of("invalid.mail", "Mail is empty!"));
        when(userValidator.isValid(any(User.class))).thenReturn(validationResult);
        when(userService.create(user1)).thenReturn(user1);

        mockMvc.perform(post("/user/create")
                        .param("email", "test@gmail.com")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("createUserPage"))
                .andExpect(model().attribute("errors", validationResult.getErrors()));


        verify(userService, never()).create(user1);
    }

    @Test
    void deleteUserTest() throws Exception {
        User user1 = new User(1L, "asdasd@gmail.com", "1234");

        mockMvc.perform(post("/user/delete")
                        .param("id", String.valueOf(user1.getId())))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/user"));

        verify(userService, times(1)).deleteById(user1.getId());
    }

    @Test
    void getUpdateUserTest() throws Exception {
        User user1 = new User(1L, "asdasd@gmail.com", "1234");

        when(userService.getUser(user1.getId())).thenReturn(user1);

        mockMvc.perform(get("/user/edit")
                        .param("id", String.valueOf(user1.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name("editUserPage"))
                .andExpect(model().attribute("User", user1));
        verify(userService, times(1)).getUser(user1.getId());
    }

    @Test
    void postUpdateUserTest() throws Exception {
        User user1 = new User(1L, "asdasd@gmail.com", "1234");

        ValidationResult validationResult = new ValidationResult();

        when(userValidator.isValid(user1)).thenReturn(validationResult);
        doNothing().when(userService).update(user1);

        mockMvc.perform(post("/user/edit")
                        .param("id", String.valueOf(user1.getId()))
                        .param("email", String.valueOf(user1.getMail()))
                        .param("password", String.valueOf(user1.getPassword())))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/user"))
                .andExpect(model().attributeDoesNotExist(("errors")));

        verify(userService, times(1)).update(user1);
        verify(userValidator).isValid(user1);
    }

    @Test
    void postUpdateNonValidUserTest() throws Exception {
        User user1 = new User(1L, "asdasd@gmail.com", "1234");

        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of("invalid.mail", "Mail is empty!"));
        when(userValidator.isValid(user1)).thenReturn(validationResult);
        doNothing().when(userService).update(user1);

        mockMvc.perform(post("/user/edit")
                        .param("id", String.valueOf(user1.getId()))
                        .param("email", String.valueOf(user1.getMail()))
                        .param("password", String.valueOf(user1.getPassword())))
                .andExpect(status().isOk())
                .andExpect(view().name("editUserPage"))
                .andExpect(model().attribute("errors", validationResult.getErrors()));

        verify(userService, never()).create(user1);
    }
}
