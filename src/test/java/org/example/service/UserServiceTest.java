package org.example.service;

import org.example.data.UsersRepository;
import org.example.model.User;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UsersRepository usersRepository;
    String firstName;
    String lastName;
    String email;
    String password;
    String repeatedPassword;

    @BeforeEach
    void init() {
        firstName = "John";
        lastName = "wick";
        email = "john.wick@mail.com";
        password = "12345678";
        repeatedPassword = "12345678";
    }

    @DisplayName("User Object Created")
    @Test
    void testCreateUser_WhenUserDetailsProvided_returnsUserObject() {
        Mockito.when(usersRepository.save(Mockito.any(User.class))).thenReturn(true);
        User user = userService.createUser(firstName, lastName, email, password, repeatedPassword);
        assertNotNull(user);
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(email, user.getEmail());
    }
}
