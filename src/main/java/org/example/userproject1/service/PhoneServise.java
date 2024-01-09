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
import java.util.Set;
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
            throw new IllegalArgumentException("Note with id=" + id + " does not exist");
        }
        Phone phone;
        return phoneRepository.
                findAll()
                .stream()
                .filter(o -> Objects.equals(o.getUser().getUser_id(), user.getUser_id()))
                .collect(Collectors.toList());
    }
}
