package org.example.userproject1.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.userproject1.entity.Phone;
import org.example.userproject1.entity.User;
import org.example.userproject1.repository.PhoneRepository;
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

import static org.junit.jupiter.api.Assertions.*;



@ExtendWith(MockitoExtension.class)
public class PhoneServiseTest {
    @InjectMocks
    private PhoneServise phoneServise;

    @Mock
    private PhoneRepository phoneRepository;

    @Mock
    private UserRepository userRepository;

    private User user1;
    private Phone phone1;
    private Phone phone2;

    @BeforeEach
    void setUp(){
        user1 = new User(1L, "asdasd@wqdqw.com", "123");
        phone1 = new Phone(1L, "132123", user1);
        phone2 = new Phone(2L, "132124", user1);
    }

    @Test
    void listOfPhoneNumbers(){
        List<Phone> phoneList = List.of(phone1, phone2);

        Mockito.when(userRepository.findById(user1.getUser_id())).thenReturn(Optional.of(user1));
        Mockito.when(phoneRepository.findAll()).thenReturn(phoneList);

        List<Phone> result = phoneServise.userContacts(1L);

        assertEquals(phoneList, result);
    }

    @Test
    void listOfExceptionPhoneNumbers(){
        Mockito.when(userRepository.findById(phone1.getId())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> phoneServise.userContacts(user1.getUser_id()));
    }

    @Test
    void createContactTest(){
        phoneServise.createContact(phone1);
        Mockito.verify(phoneRepository, Mockito.times(1)).save(phone1);
    }

    @Test
    void deleteByIdTest() {
        phoneServise.deleteById(phone1.getId());
        Mockito.verify(phoneRepository, Mockito.times(1)).deleteById(phone1.getId());
    }

    @Test
    void getPhoneTest() {
        Mockito.when(phoneRepository.findById(1L)).thenReturn(Optional.of(phone1));
        Phone result = phoneServise.getPhone(phone1.getId());
        assertEquals(phone1, result);
    }

    @Test
    void getExceptionPhoneTest() {
        Mockito.when(phoneRepository.findById(phone1.getId())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> phoneServise.getPhone(phone1.getId()));
    }

    @Test
    void updateTest() {
        Mockito.when(phoneRepository.existsById(phone1.getId())).thenReturn(true);
        phoneServise.update(phone1);
        Mockito.verify(phoneRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(phoneRepository, Mockito.times(1)).save(phone1);
    }

    @Test
    void updateExceptionTest() {
        Mockito.when(phoneRepository.existsById(phone1.getId())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> phoneServise.update(phone1));
    }
}
