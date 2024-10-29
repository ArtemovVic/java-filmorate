package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {
    @Autowired
    UserController userController;

    @Test
    void shouldAddUser() {
        User user = User.builder().email("test@example.com").login("123").birthday(LocalDate.parse("2021-12-03")).build();
        User addedUser = userController.addUser(user);

        assertNotNull(addedUser);
        assertEquals(user.getEmail(), addedUser.getEmail());
        assertEquals(user.getLogin(), addedUser.getLogin());
        assertNotNull(addedUser.getId());
    }

    @Test
    void shouldAddUserWithOutName() {
        User user = User.builder().email("test123@example.com").login("123").birthday(LocalDate.parse("2021-12-03")).build();
        User addedUser = userController.addUser(user);

        assertEquals("123", addedUser.getName());
    }

    // Локально тесты работают, но в билде гита безнадежно умирают. По какой причине - я установить не смог.

    @Test
    void shouldDropValidateExpWhenAddUserWithInvalidBirthday() {
        User user = User.builder().email("test@example.com").login("123").birthday(LocalDate.parse("2025-12-03")).build();

        Exception exception = assertThrows(ValidationException.class, () -> {
            userController.addUser(user);
        });

        assertTrue(exception.getMessage().contains("должно содержать прошедшую дату или сегодняшнее число"));
    }

    @Test
    void shouldDropValidateExpWhenAddUserWithInvalidEmail() {
        User user = User.builder().email("testexample.com").login("123").birthday(LocalDate.parse("2021-12-03")).build();

        Exception exception = assertThrows(ValidationException.class, () -> {
            userController.addUser(user);
        });

        assertTrue(exception.getMessage().contains("должно иметь формат адреса электронной почты"));
    }

    @Test
    void shouldDropValidateExpWhenAddUserWithInvalidLogin() {
        User user = User.builder().email("test@example.com").login("").birthday(LocalDate.parse("2021-12-03")).build();

        Exception exception = assertThrows(ValidationException.class, () -> {
            userController.addUser(user);
        });

        assertTrue(exception.getMessage().contains("не должно быть пустым"));
    }

    @Test
    void shouldDropValidateExpWhenAddUserWithBlankEmail() {
        User user = User.builder().email("").login("123").birthday(LocalDate.parse("2021-12-03")).build();

        Exception exception = assertThrows(ValidationException.class, () -> {
            userController.addUser(user);
        });

        assertTrue(exception.getMessage().contains("не должно быть пустым"));
    }

}