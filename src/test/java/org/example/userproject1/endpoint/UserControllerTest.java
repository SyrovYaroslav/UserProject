package org.example.userproject1.endpoint;

import org.example.userproject1.dto.UserDto;
import org.example.userproject1.entity.User;
import org.example.userproject1.service.UserService;
import org.example.userproject1.validator.Error;
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

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;

//    @Test
//    void listUserTest() throws Exception {
//        User user1 = new User(1L, "asdasd@gmail.com", "1234");
//        User user2 = new User(2L, "asdas4@gmail.com", "12534");
//
//        List<User> userList = List.of(user1, user2);
//
//        Mockito.when(userService.listAll()).thenReturn(userList);
//
//        mockMvc.perform(get("/user"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("allUserPage"))
//                .andExpect(model().attribute("UserList", userList));
//
//        verify(userService, times(1)).listAll();
//    }

    @Test
    void getCreateUserTest() throws Exception {
        mockMvc.perform(get("/user/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createUserPage"));
    }

    @Test
    void postCreateUserTest() throws Exception {
        String email = "foas@gmail.com";
        String password = "1234";
        UserDto userDto = UserDto.builder()
                .mail(email)
                .password(password)
                .error(new ArrayList<>())
                .build();
        when(userService.saveUser(email, password)).thenReturn(userDto);

        mockMvc.perform(post("/user/create")
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/user"))
                .andExpect(model().attributeDoesNotExist(("errors")));

        verify(userService, times(1)).saveUser(email, password);
    }

    @Test
    void postCreateNonValidUserTest() throws Exception {
        String email = "foasgmail.com";
        String password = "1234";
        List<Error>  errors = new ArrayList<>();
        errors.add(Error.of("invalid.mail", "Mail is empty!"));
        UserDto userDto = UserDto.builder()
                .mail(email)
                .password(password)
                .error(errors)
                .build();


        when(userService.saveUser(email, password)).thenReturn(userDto);

        mockMvc.perform(post("/user/create")
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(view().name("createUserPage"))
                .andExpect(model().attribute(("errors"), errors));

        verify(userService, times(1)).saveUser(email, password);
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
        long id = 1L;
        String email = "foas@gmail.com";
        String password = "1234";
        UserDto userDto = UserDto.builder()
                .id(id)
                .mail(email)
                .password(password)
                .error(new ArrayList<>())
                .build();
        when(userService.saveUser(email, password, id)).thenReturn(userDto);

        mockMvc.perform(post("/user/edit")
                        .param("id", String.valueOf(id))
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/user"))
                .andExpect(model().attributeDoesNotExist(("errors")));

        verify(userService, times(1)).saveUser(email, password, id);
    }

    @Test
    void postUpdateNonValidUserTest() throws Exception {
        long id = 1L;
        String email = "foasgmail.com";
        String password = "1234";
        List<Error>  errors = new ArrayList<>();
        errors.add(Error.of("invalid.mail", "Mail is empty!"));
        UserDto userDto = UserDto.builder()
                .id(id)
                .mail(email)
                .password(password)
                .error(errors)
                .build();
        when(userService.saveUser(email, password, id)).thenReturn(userDto);

        mockMvc.perform(post("/user/edit")
                        .param("id", String.valueOf(id))
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(view().name("editUserPage"))
                .andExpect(model().attribute("errors", errors));

        verify(userService, times(1)).saveUser(email, password, id);
    }
}
