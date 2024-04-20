package com.example.demo.controller;

import com.example.demo.domain.users.User;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Create a user",
            description = """
                    
                    To create an user it is requested to inform a valid : 
                    - Name
                    - Username
                    - Email
                    - Password
                    - Role
                    
                    """)
    @PostMapping("/create")
    public ResponseEntity<User> createUser
            (
                    @Valid
                    @RequestBody
                    UserDto userDto
            ) {

        User newUser = userService.createUser(userDto);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        
    }

    @Operation(summary = "Get an user data",
            description = "Get an user data by username and return name, email and role.")
    @GetMapping("/getUser")
    public ResponseEntity<UserResponseDto> getUserData
            (
                    @Parameter(description = "Username", example = "John")
                    @RequestParam String username
            ) throws Exception {

        User userData = userService.getUserByUsername(username);

        UserResponseDto userResponse = new UserResponseDto(userData.getName(), userData.getEmail(), userData.getRole());

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @Operation(summary = "Get all users",
            description = "Internal api to check all users.")
    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUser() throws Exception {
        List<User> userList = userService.getAllUsers();

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

}
