package org.example.userproject1.service;

import org.example.userproject1.customexception.PhoneNotFoundException;
import org.example.userproject1.customexception.UserNotFoundException;
import org.example.userproject1.dto.PhoneDto;
import org.example.userproject1.dto.UserDto;
import org.example.userproject1.entity.Phone;
import org.example.userproject1.entity.User;
import org.example.userproject1.repository.PhoneRepository;
import org.example.userproject1.repository.UserRepository;
import org.example.userproject1.validator.Error;
import org.example.userproject1.validator.PhoneValidator;
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

import static org.junit.jupiter.api.Assertions.*;



@ExtendWith(MockitoExtension.class)
public class PhoneServiceTest {
    @InjectMocks
    private PhoneService phoneService;

    @Mock
    private PhoneRepository phoneRepository;
    @Mock
    private PhoneValidator phoneValidator;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;

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

        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        Mockito.when(phoneRepository.findAll()).thenReturn(phoneList);

        List<Phone> result = phoneService.userContacts(1L);

        assertEquals(phoneList, result);
    }

    @Test
    void listOfExceptionPhoneNumbers(){
        Mockito.when(userRepository.findById(phone1.getId())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> phoneService.userContacts(user1.getId()));
    }

    @Test
    void createContactTest(){
        Phone phone = Phone.builder()
                .phoneNumber("132123")
                .user(user1)
                .build();

        ValidationResult validationResult = new ValidationResult();
        Mockito.when(phoneValidator.isValid(phone)).thenReturn(validationResult);

        PhoneDto phoneDto = PhoneDto.builder()
                .phoneNumber(phone.getPhoneNumber())
                .user(phone.getUser())
                .error(validationResult.getErrors())
                .build();


        Mockito.when(userService.getUser(user1.getId())).thenReturn(user1);
        Mockito.when(phoneRepository.save(phone)).thenReturn(phone);

        PhoneDto resultPhoneDto = phoneService.saveContact(user1.getId(), "132123");

        assertEquals(phoneDto, resultPhoneDto);
    }

    @Test
    void createNonValidContactTest(){
        Phone phone = Phone.builder()
                .phoneNumber("132123")
                .user(user1)
                .build();

        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of("invalid.password", "Password is empty!"));
        Mockito.when(phoneValidator.isValid(phone)).thenReturn(validationResult);

        PhoneDto phoneDto = PhoneDto.builder()
                .phoneNumber(phone.getPhoneNumber())
                .user(phone.getUser())
                .error(validationResult.getErrors())
                .build();


        Mockito.when(userService.getUser(user1.getId())).thenReturn(user1);

        PhoneDto resultPhoneDto = phoneService.saveContact(user1.getId(), "132123");

        assertEquals(phoneDto, resultPhoneDto);
    }

    @Test
    void deleteByIdTest() {
        phoneRepository.deleteById(phone1.getId());
        Mockito.verify(phoneRepository, Mockito.times(1)).deleteById(phone1.getId());
    }
    @Test
    void deleteByIdPhoneExceptionTest() {
        phoneRepository.deleteById(phone1.getId());
        Mockito.verify(phoneRepository, Mockito.times(1)).deleteById(phone1.getId());
        assertThrows(PhoneNotFoundException.class, () -> phoneService.deleteById(user1.getId()));
    }

    @Test
    void getPhoneTest() {
        Mockito.when(phoneRepository.findById(1L)).thenReturn(Optional.of(phone1));
        Phone result = phoneService.getPhone(phone1.getId());
        assertEquals(phone1, result);
    }

    @Test
    void getExceptionPhoneTest() {
        Mockito.when(phoneRepository.findById(phone1.getId())).thenReturn(Optional.empty());
        assertThrows(PhoneNotFoundException.class, () -> phoneService.getPhone(phone1.getId()));
    }

    @Test
    void updateTest() {
        ValidationResult validationResult = new ValidationResult();
        Mockito.when(phoneValidator.isValid(phone1)).thenReturn(validationResult);

        PhoneDto phoneDto = PhoneDto.builder()
                .id(phone1.getId())
                .phoneNumber(phone1.getPhoneNumber())
                .user(phone1.getUser())
                .error(validationResult.getErrors())
                .build();


        Mockito.when(userService.getUser(user1.getId())).thenReturn(user1);
        Mockito.when(phoneRepository.save(phone1)).thenReturn(phone1);

        PhoneDto resultPhoneDto = phoneService.saveContact(user1.getId(), "132123", phoneDto.getId());

        assertEquals(phoneDto, resultPhoneDto);
    }

    @Test
    void updateExceptionTest() {
        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of("invalid.password", "Password is empty!"));
        Mockito.when(phoneValidator.isValid(phone1)).thenReturn(validationResult);

        PhoneDto phoneDto = PhoneDto.builder()
                .id(phone1.getId())
                .phoneNumber(phone1.getPhoneNumber())
                .user(phone1.getUser())
                .error(validationResult.getErrors())
                .build();


        Mockito.when(userService.getUser(user1.getId())).thenReturn(user1);


        PhoneDto resultPhoneDto = phoneService.saveContact(user1.getId(), "132123", phoneDto.getId());

        assertEquals(phoneDto, resultPhoneDto);
    }
}
