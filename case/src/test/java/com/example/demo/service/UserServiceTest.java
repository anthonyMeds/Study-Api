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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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

        when(userRepository.findByUsernameOrEmail(Mockito.anyString())).thenReturn(Optional.empty());
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(userDto);

        assertEquals(userDto.name(), createdUser.getName());
        assertEquals(userDto.username(), createdUser.getUsername());
        assertEquals(userDto.email(), createdUser.getEmail());
        assertEquals(userDto.role(), createdUser.getRole());
        assertNotNull(createdUser.getCreationDate());
    }

    @Test
    @DisplayName("Test checkIfUserExists method - Username exists")
    void testCheckIfUserExists_UsernameExists() {

        String existingUsername = "existingUsername";
        when(userRepository.findByUsernameOrEmail(existingUsername))
                .thenReturn(Optional.of(new User()));

        RuntimeException serviceException = assertThrows(RuntimeException.class, () ->
                userService.checkIfUserExists(existingUsername, "Username"));

        assertEquals(serviceException.getMessage(), "Username already exists.");
   }

    @Test
    @DisplayName("Test checkIfUserExists method - Email exists")
    void testCheckIfUserExists_EmailExists() {

        String existingEmail = "existingEmail@example.com";
        when(userRepository.findByUsernameOrEmail(existingEmail))
                .thenReturn(Optional.of(new User()));

        RuntimeException serviceException = assertThrows(RuntimeException.class, () ->
                userService.checkIfUserExists(existingEmail, "Email"));

        assertEquals(serviceException.getMessage(), "Email already exists."); }

    @Test
    @DisplayName("Test checkIfUserExists method - User does not exist")
    void testCheckIfUserExists_UserDoesNotExist() {

        String nonExistingValue = "nonExistingValue";
        when(userRepository.findByUsernameOrEmail(nonExistingValue)).thenReturn(Optional.empty());


        userService.checkIfUserExists(nonExistingValue, "FieldName");

        // No exception should be thrown
    }

    @Test
    public void testGetUserByUsername_WhenUserExists_ThenReturnUser() throws Exception {

        UserDto userDto = new UserDto("Joao", "joaozin", "joao@hotmail.com", "123", UserRole.STUDENT);

        String username = "testUser";
        User user = new User(userDto);
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));

        User result = userService.getUserByUsername(username);

        assertNotNull(result);
        assertEquals(user, result);
        assertEquals(user.getUsername(), result.getUsername());
    }

    @Test
    public void testGetUserByUsername_WhenUserDoesNotExist_ThenThrowException() {

        String username = "nonExistentUser";
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.empty());

        Exception serviceException = assertThrows(Exception.class, () ->
                userService.getUserByUsername(username), "Username");

        assertEquals(serviceException.getMessage(), "User not found.");
    }


}
