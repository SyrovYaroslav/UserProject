package org.example.userproject1.endpoint;

import org.example.userproject1.entity.Phone;
import org.example.userproject1.entity.User;
import org.example.userproject1.service.PhoneServise;
import org.example.userproject1.service.UserSevise;
import org.example.userproject1.validator.PhoeValidator;
import org.example.userproject1.validator.UserValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class PhoneControllerTest {
    @MockBean
    private PhoneServise phoneServise;
    @MockBean
    private PhoeValidator phoeValidator;
    @MockBean
    private UserSevise userSevise;
    @MockBean
    private UserValidator userValidator;
    @Autowired
    private MockMvc mockMvc;

//    @Test
//    void listContactsTest() throws Exception{
//        User user1 = new User(1L, "asdasd@gmail.com", "1234");
//        Phone phone1 = new Phone(1L, "132123", user1);
//        Phone phone2 = new Phone(2L, "132124", user1);
//
//        List<Phone> phoneList = List.of(phone1, phone2);
//
//        Mockito.when(phoneServise.userContacts(user1.getUser_id())).thenReturn(phoneList);
//
//        mockMvc.perform(get("/user/contacts/{id}", user1.getUser_id()))
//                .andExpect(status().isOk())
//                .andExpect(view().name("contactPage"))
//                .andExpect(model().attribute("PhoneNumbers", phoneList))
//                .andExpect(model().attribute("UserId", user1.getUser_id()));
//
//        verify(phoneServise).userContacts(user1.getUser_id());
//    }
}
