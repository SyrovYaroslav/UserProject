package org.example.userproject1.endpoint;

import org.example.userproject1.dto.PhoneDto;
import org.example.userproject1.entity.Phone;
import org.example.userproject1.entity.User;
import org.example.userproject1.service.PhoneService;
import org.example.userproject1.service.UserService;
import org.example.userproject1.validator.Error;
import org.example.userproject1.validator.PhoneValidator;
import org.example.userproject1.validator.UserValidator;
import org.example.userproject1.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PhoneController.class)
public class PhoneControllerTest {
    @MockBean
    private PhoneService phoneService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void listContactsTest() throws Exception{
        long id = 1L;
        User user1 = new User(1L, "asdasd@gmail.com", "1234");
        Phone phone1 = new Phone(1L, "132123", user1);
        Phone phone2 = new Phone(2L, "132124", user1);

        List<Phone> phoneList = List.of(phone1, phone2);

        Mockito.when(phoneService.userContacts(id)).thenReturn(phoneList);

        mockMvc.perform(get("/user/contacts/{id}", user1.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("contactPage"))
                .andExpect(model().attribute("PhoneNumbers", phoneList))
                .andExpect(model().attribute("UserId", id));

        verify(phoneService, times(1)).userContacts(user1.getId());
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
        PhoneDto phoneDto = PhoneDto.builder()
                .phoneNumber("12312")
                .user(user1)
                .error(new ArrayList<>())
                .build();

        when(phoneService.saveContact(user1.getId(), phoneDto.getPhoneNumber())).thenReturn(phoneDto);


        mockMvc.perform(post("/user/contacts/{id}/create", user1.getId())
                        .param("phone", phoneDto.getPhoneNumber()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/user/contacts/" + user1.getId()))
                .andExpect(model().attributeDoesNotExist(("errors")));

        verify(phoneService, times(1)).saveContact(user1.getId(), phoneDto.getPhoneNumber());
    }

    @Test
    void postCreateNonValidPhoneTest() throws Exception{
        User user1 = new User(1L, "asdasd@gmail.com", "1234");
        List<Error>  errors = new ArrayList<>();
        errors.add(Error.of("invalid.mail", "Phone is empty!"));
        PhoneDto phoneDto = PhoneDto.builder()
                .phoneNumber("12312")
                .user(user1)
                .error(errors)
                .build();

        when(phoneService.saveContact(user1.getId(), phoneDto.getPhoneNumber())).thenReturn(phoneDto);

        mockMvc.perform(post("/user/contacts/{id}/create", user1.getId())
                        .param("phone", phoneDto.getPhoneNumber()))
                .andExpect(status().isOk())
                .andExpect(view().name("createContactPage"))
                .andExpect(model().attribute("errors", errors));

        verify(phoneService, times(1)).saveContact(user1.getId(), phoneDto.getPhoneNumber());
    }

    @Test
    void deleteCreatePhoneTest() throws Exception{
        long id = 1L;
        mockMvc.perform(post("/user/contacts/{id}/delete", id)
                        .param("contact_id", String.valueOf(id)))
                        .andExpect(status().isFound())
                        .andExpect(redirectedUrl("/user/contacts/" + id));

        verify(phoneService, times(1)).deleteById(id);
    }

    @Test
    void getEditPhoneTest() throws Exception{
        User user1 = new User(1L, "asdasd@gmail.com", "1234");
        Phone phone1 = new Phone(1L, "12312", user1);

        when(phoneService.getPhone(phone1.getId())).thenReturn(phone1);

        mockMvc.perform(get("/user/contacts/{id}/edit", user1.getId())
                        .param("contact_id", String.valueOf(phone1.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name("editContactPage"))
                .andExpect(model().attribute("Phone", phone1))
                .andExpect(model().attribute("UserId", user1.getId()));

        verify(phoneService, times(1)).getPhone(user1.getId());
    }

    @Test
    void postEditPhoneTest() throws Exception{
        User user1 = new User(1L, "asdasd@gmail.com", "1234");
        PhoneDto phoneDto = PhoneDto.builder()
                .id(1L)
                .phoneNumber("12312")
                .user(user1)
                .error(new ArrayList<>())
                .build();

        when(phoneService.saveContact(user1.getId(), phoneDto.getPhoneNumber(), phoneDto.getId())).thenReturn(phoneDto);


        mockMvc.perform(post("/user/contacts/{id}/edit", user1.getId())
                        .param("phone_number", phoneDto.getPhoneNumber())
                        .param("phone_id", String.valueOf(phoneDto.getId())))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/user/contacts/" + user1.getId()))
                .andExpect(model().attributeDoesNotExist(("errors")));

        verify(phoneService, times(1)).saveContact(user1.getId(), phoneDto.getPhoneNumber(), phoneDto.getId());
    }

    @Test
    void postEditNonValidPhoneTest() throws Exception{
        User user1 = new User(1L, "asdasd@gmail.com", "1234");
        List<Error>  errors = new ArrayList<>();
        errors.add(Error.of("invalid.mail", "Phone is empty!"));
        PhoneDto phoneDto = PhoneDto.builder()
                .id(1L)
                .phoneNumber("")
                .user(user1)
                .error(errors)
                .build();

        when(phoneService.saveContact(user1.getId(), phoneDto.getPhoneNumber(), phoneDto.getId())).thenReturn(phoneDto);


        mockMvc.perform(post("/user/contacts/{id}/edit", user1.getId())
                        .param("phone_id", String.valueOf(phoneDto.getId()))
                        .param("phone_number", phoneDto.getPhoneNumber()))
                .andExpect(status().isOk())
                .andExpect(view().name("editContactPage"))
                .andExpect(model().attribute("errors", errors));

        verify(phoneService, times(1)).saveContact(user1.getId(), phoneDto.getPhoneNumber(), phoneDto.getId());
    }
}
