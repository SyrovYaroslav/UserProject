//package org.example.userproject1.endpoint;
//
//import org.example.userproject1.entity.Phone;
//import org.example.userproject1.entity.User;
//import org.example.userproject1.service.PhoneService;
//import org.example.userproject1.service.UserService;
//import org.example.userproject1.validator.Error;
//import org.example.userproject1.validator.PhoneValidator;
//import org.example.userproject1.validator.UserValidator;
//import org.example.userproject1.validator.ValidationResult;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(PhoneController.class)
//public class PhoneControllerTest {
//    @MockBean
//    private PhoneService phoneService;
//    @MockBean
//    private PhoneValidator phoneValidator;
//    @MockBean
//    private UserService userService;
//    @MockBean
//    private UserValidator userValidator;
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    void listContactsTest() throws Exception{
//        User user1 = new User(1L, "asdasd@gmail.com", "1234");
//        Phone phone1 = new Phone(1L, "132123", user1);
//        Phone phone2 = new Phone(2L, "132124", user1);
//
//        List<Phone> phoneList = List.of(phone1, phone2);
//
//        Mockito.when(phoneService.userContacts(user1.getId())).thenReturn(phoneList);
//
//        mockMvc.perform(get("/user/contacts/{id}", user1.getId()))
//                .andExpect(status().isOk())
//                .andExpect(view().name("contactPage"))
//                .andExpect(model().attribute("PhoneNumbers", phoneList))
//                .andExpect(model().attribute("UserId", user1.getId()));
//
//        verify(phoneService).userContacts(user1.getId());
//    }
//
//    @Test
//    void getCreatePhoneTest() throws Exception{
//        mockMvc.perform(get("/user/contacts/{id}/create", 1L))
//                .andExpect(status().isOk())
//                .andExpect(view().name("createContactPage"))
//                .andExpect(model().attribute("UserId", String.valueOf(1L)));
//    }
//
//    @Test
//    void postCreatePhoneTest() throws Exception{
//        User user1 = new User(1L, "asdasd@gmail.com", "1234");
//        Phone phone1 = new Phone();
//        phone1.setPhoneNumber("12312");
//        phone1.setUser(user1);
//        ValidationResult validationResult = new ValidationResult();
//
//        when(userService.getUser(user1.getId())).thenReturn(user1);
//        when(phoneValidator.isValid(phone1)).thenReturn(validationResult);
//
//        mockMvc.perform(post("/user/contacts/{id}/create", user1.getId())
//                        .param("phone", phone1.getPhoneNumber()))
//                .andExpect(status().isFound())
//                .andExpect(redirectedUrl("/user/contacts/" + user1.getId()))
//                .andExpect(model().attributeDoesNotExist(("errors")));
//
//        verify(phoneService).saveContact(phone1);
//        verify(phoneValidator).isValid(phone1);
//    }
//
//    @Test
//    void postCreateNonValidPhoneTest() throws Exception{
//        User user1 = new User(1L, "asdasd@gmail.com", "1234");
//        Phone phone1 = new Phone();
//        phone1.setPhoneNumber("12312");
//        phone1.setUser(user1);
//        ValidationResult validationResult = new ValidationResult();
//        validationResult.add(Error.of("invalid.phone", "Phone is empty!"));
//        when(userService.getUser(user1.getId())).thenReturn(user1);
//        when(phoneValidator.isValid(any(Phone.class))).thenReturn(validationResult);
//
//        mockMvc.perform(post("/user/contacts/{id}/create", user1.getId())
//                        .param("phone", phone1.getPhoneNumber()))
//                .andExpect(status().isOk())
//                .andExpect(view().name("createContactPage"))
//                .andExpect(model().attribute("errors", validationResult.getErrors()));
//
//        verify(phoneService, never()).saveContact(phone1);
//    }
//
//    @Test
//    void deleteCreatePhoneTest() throws Exception{
//        User user1 = new User(1L, "asdasd@gmail.com", "1234");
//        Phone phone1 = new Phone(1L, "12312", user1);
//
//        mockMvc.perform(post("/user/contacts/{id}/delete", user1.getId())
//                        .param("contact_id", String.valueOf(phone1.getId())))
//                        .andExpect(status().isFound())
//                        .andExpect(redirectedUrl("/user/contacts/" + user1.getId()));
//
//        verify(phoneService, times(1)).deleteById(phone1.getId());
//    }
//
//    @Test
//    void getEditPhoneTest() throws Exception{
//        User user1 = new User(1L, "asdasd@gmail.com", "1234");
//        Phone phone1 = new Phone(1L, "12312", user1);
//
//        when(phoneService.getPhone(phone1.getId())).thenReturn(phone1);
//
//        mockMvc.perform(get("/user/contacts/{id}/edit", user1.getId())
//                        .param("contact_id", String.valueOf(phone1.getId())))
//                .andExpect(status().isOk())
//                .andExpect(view().name("editContactPage"))
//                .andExpect(model().attribute("Contact", phone1.getPhoneNumber()))
//                .andExpect(model().attribute("ContactId", phone1.getId()))
//                .andExpect(model().attribute("UserId", user1.getId()));
//
//        verify(phoneService, times(1)).getPhone(user1.getId());
//
//    }
//
//    @Test
//    void postEditPhoneTest() throws Exception{
//        User user1 = new User(1L, "asdasd@gmail.com", "1234");
//        Phone phone1 = new Phone(1L, "12312", user1);
//        ValidationResult validationResult = new ValidationResult();
//        when(userService.getUser(user1.getId())).thenReturn(user1);
//        when(phoneService.getPhone(phone1.getId())).thenReturn(phone1);
//        when(phoneValidator.isValid(phone1)).thenReturn(validationResult);
//
//        mockMvc.perform(post("/user/contacts/{id}/edit", user1.getId())
//                        .param("contact_id", String.valueOf(phone1.getId()))
//                        .param("phone_number", phone1.getPhoneNumber()))
//                .andExpect(status().isFound())
//                .andExpect(redirectedUrl("/user/contacts/" + user1.getId()))
//                .andExpect(model().attributeDoesNotExist(("errors")));
//
//        verify(phoneService, times(1)).update(phone1);
//        verify(phoneValidator).isValid(phone1);
//    }
//
//    @Test
//    void postEditNonValidPhoneTest() throws Exception{
//        User user1 = new User(1L, "asdasd@gmail.com", "1234");
//        Phone phone1 = new Phone(1L, "12312", user1);
//        ValidationResult validationResult = new ValidationResult();
//        validationResult.add(Error.of("invalid.phone", "Phone is empty!"));
//
//        when(userService.getUser(user1.getId())).thenReturn(user1);
//        when(phoneService.getPhone(phone1.getId())).thenReturn(phone1);
//        when(phoneValidator.isValid(phone1)).thenReturn(validationResult);
//
//        mockMvc.perform(post("/user/contacts/{id}/edit", user1.getId())
//                        .param("contact_id", String.valueOf(phone1.getId()))
//                        .param("phone_number", phone1.getPhoneNumber()))
//                .andExpect(status().isOk())
//                .andExpect(view().name("editContactPage"))
//                .andExpect(model().attribute("errors", validationResult.getErrors()));
//
//        verify(phoneService, never()).update(phone1);
//    }
//}
