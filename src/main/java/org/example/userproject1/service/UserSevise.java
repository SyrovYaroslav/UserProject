package org.example.userproject1.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.userproject1.entity.User;
import org.example.userproject1.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@RequiredArgsConstructor
@Service
public class UserSevise{
    private final UserRepository userRepository;
    public List<User> listAll() {
        return new ArrayList<>(userRepository.findAll());
    }

    public User create(User user) {
        userRepository.save(user);
        return user;
    }

    public void deleteById(Long id){
        userRepository.deleteById(id);
    }

    public User getUser(Long id) {
        User user;
        try {
            user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        }catch (EntityNotFoundException e) {
            throw new IllegalArgumentException("User with id=" + id + " does not exist");
        }
        return user;
    }

    public void update(User user) {
        long id = user.getUser_id();
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User with id=" + id + " does not exist");
        }
        userRepository.save(user);
    }

}
