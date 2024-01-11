package org.example.userproject1.endpoint;

import org.example.userproject1.entity.User;
import org.example.userproject1.service.UserSevise;
import org.example.userproject1.validator.Error;
import org.example.userproject1.validator.UserValidator;
import org.example.userproject1.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @MockBean
    private UserSevise userSevise;
    @MockBean
    private UserValidator userValidator;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void listUserTest() throws Exception{
        User user1 = new User(1L, "asdasd@gmail.com", "1234");
        User user2 = new User(2L, "asdas4@gmail.com", "12534");

        List<User> userList = List.of(user1, user2);

        Mockito.when(userSevise.listAll()).thenReturn(userList);

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(view().name("allUserPage"))
                .andExpect(model().attribute("UserList", userList));

        verify(userSevise, Mockito.times(1)).listAll();
    }

    @Test
    void getCreateUserTest() throws Exception {
        mockMvc.perform(get("/user/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createUserPage"));
    }

    @Test
    void postCreateUserTest() throws Exception {
        User user1 = new User(1L, "asdasd@gmail.com", "1234");
        ValidationResult validationResult = new ValidationResult();
        when(userValidator.isValid(user1)).thenReturn(validationResult);
        when(userSevise.create(user1)).thenReturn(user1);


        mockMvc.perform(post("/user/create")
                        .param("email", user1.getMail())
                        .param("password", user1.getPassword()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/user"))
                .andExpect(model().attributeDoesNotExist("errors"));

        verify(userSevise).create(user1);
        verify(userValidator).isValid(user1);
    }

    @Test
    void deleteUserTest() throws Exception {
        User user1 = new User(1L, "asdasd@gmail.com", "1234");

        mockMvc.perform(post("/user/delete")
                        .param("id", String.valueOf(user1.getUser_id())))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/user"));

        verify(userSevise, Mockito.times(1)).deleteById(user1.getUser_id());
    }

    @Test
    void getUpdateUserTest() throws Exception {
        User user1 = new User(1L, "asdasd@gmail.com", "1234");

        Mockito.when(userSevise.getUser(user1.getUser_id())).thenReturn(user1);

        mockMvc.perform(get("/user/edit")
                        .param("id", String.valueOf(user1.getUser_id())))
                .andExpect(status().isOk())
                .andExpect(view().name("editUserPage"))
                .andExpect(model().attribute("User", user1));
        verify(userSevise, Mockito.times(1)).getUser(user1.getUser_id());
    }

}
