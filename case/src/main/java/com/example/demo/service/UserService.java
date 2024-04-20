package com.example.demo.service;

import com.example.demo.domain.users.User;
import com.example.demo.dto.UserDto;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(UserDto userDto) {

        User user = new User(userDto);

        checkIfUserExists(user.getUsername(), "Username");
        checkIfUserExists(user.getEmail(), "Email");

        userRepository.save(user);

        return user;
    }

    public User findUserById(Long id) throws Exception {
        return this.userRepository.findUserById(id).orElseThrow(() -> new Exception("User not found."));
    }

    public User getUserByUsername(String username) throws Exception {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new Exception("User not found."));
    }

    public List<User> getAllUsers() throws Exception {
         List<User> userList =  userRepository.findAll();

         if (userList.isEmpty()) {
             throw new Exception("Users not found");
         }

         return userList;
    }

    public void checkIfUserExists(String value, String fieldName) {
        Optional<User> existingUser = userRepository.findByUsernameOrEmail(value);
        existingUser.ifPresent(user -> {
            throw new RuntimeException(fieldName + " already exists.");
        });
    }
}
