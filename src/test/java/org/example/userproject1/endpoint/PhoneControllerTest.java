package org.example.userproject1.endpoint;

import org.example.userproject1.entity.Phone;
import org.example.userproject1.entity.User;
import org.example.userproject1.service.PhoneServise;
import org.example.userproject1.service.UserServise;
import org.example.userproject1.validator.Error;
import org.example.userproject1.validator.PhoeValidator;
import org.example.userproject1.validator.UserValidator;
import org.example.userproject1.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PhoneController.class)
public class PhoneControllerTest {
    @MockBean
    private PhoneServise phoneServise;
    @MockBean
    private PhoeValidator phoeValidator;
    @MockBean
    private UserServise userServise;
    @MockBean
    private UserValidator userValidator;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void listContactsTest() throws Exception{
        User user1 = new User(1L, "asdasd@gmail.com", "1234");
        Phone phone1 = new Phone(1L, "132123", user1);
        Phone phone2 = new Phone(2L, "132124", user1);

        List<Phone> phoneList = List.of(phone1, phone2);

        Mockito.when(phoneServise.userContacts(user1.getUser_id())).thenReturn(phoneList);

        mockMvc.perform(get("/user/contacts/{id}", user1.getUser_id()))
                .andExpect(status().isOk())
                .andExpect(view().name("contactPage"))
                .andExpect(model().attribute("PhoneNumbers", phoneList))
                .andExpect(model().attribute("UserId", user1.getUser_id()));

        verify(phoneServise).userContacts(user1.getUser_id());
    }

    @Test
    void getCreatePhoneTest() throws Exception{
        mockMvc.perform(get("/user/contacts/{id}/create", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("createContactPage"))
                .andExpect(model().attribute("UserId", String.valueOf(1L)));
    }

    @Test
    void postCreatePhoneTest() throws Exception{
        User user1 = new User(1L, "asdasd@gmail.com", "1234");
        Phone phone1 = new Phone();
        phone1.setPhone("12312");
        phone1.setUser(user1);
        ValidationResult validationResult = new ValidationResult();

        when(userServise.getUser(user1.getUser_id())).thenReturn(user1);
        when(phoeValidator.isValid(phone1)).thenReturn(validationResult);

        mockMvc.perform(post("/user/contacts/{id}/create", user1.getUser_id())
                        .param("phone", phone1.getPhone()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/user/contacts/" + user1.getUser_id()))
                .andExpect(model().attributeDoesNotExist(("errors")));

        verify(phoneServise).createContact(phone1);
        verify(phoeValidator).isValid(phone1);
    }

    @Test
    void postCreateNonValidPhoneTest() throws Exception{
        User user1 = new User(1L, "asdasd@gmail.com", "1234");
        Phone phone1 = new Phone();
        phone1.setPhone("12312");
        phone1.setUser(user1);
        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of("invalid.phone", "Phone is empty!"));
        when(userServise.getUser(user1.getUser_id())).thenReturn(user1);
        when(phoeValidator.isValid(any(Phone.class))).thenReturn(validationResult);

        mockMvc.perform(post("/user/contacts/{id}/create", user1.getUser_id())
                        .param("phone", phone1.getPhone()))
                .andExpect(status().isOk())
                .andExpect(view().name("createContactPage"))
                .andExpect(model().attribute("errors", validationResult.getErrors()));

        verify(phoneServise, never()).createContact(phone1);
    }

    @Test
    void deleteCreatePhoneTest() throws Exception{
        User user1 = new User(1L, "asdasd@gmail.com", "1234");
        Phone phone1 = new Phone(1L, "12312", user1);

        mockMvc.perform(post("/user/contacts/{id}/delete", user1.getUser_id())
                        .param("contact_id", String.valueOf(phone1.getId())))
                        .andExpect(status().isFound())
                        .andExpect(redirectedUrl("/user/contacts/" + user1.getUser_id()));

        verify(phoneServise, times(1)).deleteById(phone1.getId());
    }

    @Test
    void getEditPhoneTest() throws Exception{
        User user1 = new User(1L, "asdasd@gmail.com", "1234");
        Phone phone1 = new Phone(1L, "12312", user1);

        when(phoneServise.getPhone(phone1.getId())).thenReturn(phone1);

        mockMvc.perform(get("/user/contacts/{id}/edit", user1.getUser_id())
                        .param("contact_id", String.valueOf(phone1.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name("editContactPage"))
                .andExpect(model().attribute("Contact", phone1.getPhone()))
                .andExpect(model().attribute("ContactId", phone1.getId()))
                .andExpect(model().attribute("UserId", user1.getUser_id()));

        verify(phoneServise, times(1)).getPhone(user1.getUser_id());

    }

    @Test
    void postEditPhoneTest() throws Exception{
        User user1 = new User(1L, "asdasd@gmail.com", "1234");
        Phone phone1 = new Phone(1L, "12312", user1);
        ValidationResult validationResult = new ValidationResult();
        when(userServise.getUser(user1.getUser_id())).thenReturn(user1);
        when(phoneServise.getPhone(phone1.getId())).thenReturn(phone1);
        when(phoeValidator.isValid(phone1)).thenReturn(validationResult);

        mockMvc.perform(post("/user/contacts/{id}/edit", user1.getUser_id())
                        .param("contact_id", String.valueOf(phone1.getId()))
                        .param("phone_number", phone1.getPhone()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/user/contacts/" + user1.getUser_id()))
                .andExpect(model().attributeDoesNotExist(("errors")));

        verify(phoneServise, times(1)).update(phone1);
        verify(phoeValidator).isValid(phone1);
    }

    @Test
    void postEditNonValidPhoneTest() throws Exception{
        User user1 = new User(1L, "asdasd@gmail.com", "1234");
        Phone phone1 = new Phone(1L, "12312", user1);
        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of("invalid.phone", "Phone is empty!"));

        when(userServise.getUser(user1.getUser_id())).thenReturn(user1);
        when(phoneServise.getPhone(phone1.getId())).thenReturn(phone1);
        when(phoeValidator.isValid(phone1)).thenReturn(validationResult);

        mockMvc.perform(post("/user/contacts/{id}/edit", user1.getUser_id())
                        .param("contact_id", String.valueOf(phone1.getId()))
                        .param("phone_number", phone1.getPhone()))
                .andExpect(status().isOk())
                .andExpect(view().name("editContactPage"))
                .andExpect(model().attribute("errors", validationResult.getErrors()));

        verify(phoneServise, never()).update(phone1);
    }
}
