package org.example.userproject1.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.userproject1.entity.Phone;
import org.example.userproject1.entity.User;
import org.example.userproject1.repository.PhoneRepository;
import org.example.userproject1.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PhoneServise {
    private final PhoneRepository phoneRepository;
    private final UserRepository userRepository;
    public List<Phone> userContacts(long id) {
        User user;
        try {
            user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        }catch (EntityNotFoundException e) {
            throw new IllegalArgumentException("User with id=" + id + " does not exist");
        }
        return phoneRepository.
                findAll()
                .stream()
                .filter(o -> Objects.equals(o.getUser().getUser_id(), user.getUser_id()))
                .collect(Collectors.toList());
    }

    public void createContact(Phone phone) {
        phoneRepository.save(phone);
    }

    public void deleteById(long id) {
        phoneRepository.deleteById(id);
    }

    public Phone getPhone(long id){
        Phone phone;
        try {
            phone = phoneRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        } catch (EntityNotFoundException e) {
            throw new IllegalArgumentException("Phone with id=" + id + " does not exist");
        }
        return phone;
    }

    public void update(Phone phone) {
        long id = phone.getId();
        if (!phoneRepository.existsById(id)) {
            throw new IllegalArgumentException("User with id=" + id + " does not exist");
        }
        phoneRepository.save(phone);
    }
}
