package com.example.demo.service;

import com.example.demo.domain.users.User;
import com.example.demo.domain.users.UserRole;
import com.example.demo.dto.UserDto;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @DisplayName("Test createUser method - Success: User created successfully")
    @Test
    void testCreateUser_Success() {

        UserDto userDto = new UserDto("John Doe", "johndoe", "john@example.com",
                "password", UserRole.STUDENT);
        User user = new User(userDto);

        Mockito.when(userRepository.findByUsernameOrEmail(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(userDto);

        Assertions.assertEquals(userDto.name(), createdUser.getName());
        Assertions.assertEquals(userDto.username(), createdUser.getUsername());
        Assertions.assertEquals(userDto.email(), createdUser.getEmail());
        Assertions.assertEquals(userDto.role(), createdUser.getRole());
        Assertions.assertNotNull(createdUser.getCreationDate());
    }

    @Test
    @DisplayName("Test checkIfUserExists method - Username exists")
    void testCheckIfUserExists_UsernameExists() {

        String existingUsername = "existingUsername";
        Mockito.when(userRepository.findByUsernameOrEmail(existingUsername))
                .thenReturn(Optional.of(new User()));

        RuntimeException serviceException = assertThrows(RuntimeException.class, () ->
                userService.checkIfUserExists(existingUsername, "Username"));

        Assertions.assertEquals(serviceException.getMessage(), "Username already exists.");
   }

    @Test
    @DisplayName("Test checkIfUserExists method - Email exists")
    void testCheckIfUserExists_EmailExists() {

        String existingEmail = "existingEmail@example.com";
        Mockito.when(userRepository.findByUsernameOrEmail(existingEmail))
                .thenReturn(Optional.of(new User()));

        RuntimeException serviceException = assertThrows(RuntimeException.class, () ->
                userService.checkIfUserExists(existingEmail, "Email"));

        Assertions.assertEquals(serviceException.getMessage(), "Email already exists."); }

    @Test
    @DisplayName("Test checkIfUserExists method - User does not exist")
    void testCheckIfUserExists_UserDoesNotExist() {

        String nonExistingValue = "nonExistingValue";
        Mockito.when(userRepository.findByUsernameOrEmail(nonExistingValue)).thenReturn(Optional.empty());


        userService.checkIfUserExists(nonExistingValue, "FieldName");

        // No exception should be thrown
    }


}
