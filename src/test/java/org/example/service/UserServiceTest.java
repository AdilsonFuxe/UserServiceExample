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
    @Mock
    EmailVerificationService emailVerificationService;
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
        Mockito.verify(usersRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @DisplayName("Empty last name causes correct exception")
    @Test
    void testCreateUser_WhenFirstNameIsEmpty_throwsIllegalException() {
        // Arrange
        String firstName = "";
        String expectedExceptionMessage = "User's first name is empty";

        // Act & Assert

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(firstName, lastName, email, password, repeatedPassword);
        });

        // Assert
        assertEquals(expectedExceptionMessage, thrown.getMessage());
    }

    @DisplayName("Empty last name causes correct exception")
    @Test
    void testCreateUser_WhenLastNameIsEmpty_throwsIllegalException() {
        // Arrange
        String lastName = "";
        String expectedExceptionMessage = "User's last name is empty";

        // Act & Assert

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(firstName, lastName, email, password, repeatedPassword);
        });

        // Assert
        assertEquals(expectedExceptionMessage, thrown.getMessage());
    }

    @DisplayName("If save() method causes RuntimeException, a UserException is thrown")
    @Test
    void testCreateUser_whenSaveMethodThrowsException_thenThrowsUserServiceException() {
        Mockito.when(usersRepository.save(Mockito.any(User.class))).thenThrow(RuntimeException.class);

        assertThrows(UserServiceException.class, () -> {
            userService.createUser(firstName, lastName, email, password, repeatedPassword);
        });

        // assertEquals("Error saving user", thrown.getMessage());
    }

    @DisplayName("EmailNotificationException is handled")
    @Test
    void testCreateUser_whenEmailNotificationExceptionThrow_throwsUserServiceException() {
        Mockito.when(usersRepository.save(Mockito.any(User.class))).thenReturn(true);
        Mockito.doThrow(EmailNotificationServiceException.class).when(emailVerificationService).scheduleEmailConfirmation(Mockito.any(User.class));
        assertThrows(UserServiceException.class, () -> {
            userService.createUser(firstName, lastName, email, password, repeatedPassword);
        });
        Mockito.verify(emailVerificationService, Mockito.times(1)).scheduleEmailConfirmation(Mockito.any(User.class));
    }
}
