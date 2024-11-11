package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FilmControllerTest {
    @Autowired
    FilmController filmController;

    @Test
    void shouldDropValidateExpWhenAddFilmWithInvalidDuration() {
        Film film = Film.builder().name("testexample").description("testexample123").releaseDate(LocalDate.parse("2021-12-03")).duration(-30).build();

        Exception exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));

        assertTrue(exception.getMessage().contains("должно быть больше или равно 0"));
    }

    @Test
    void shouldDropValidateExpWhenAddFilmWithInvalidDescription() {
        Film film = Film.builder().name("testexample").description("t".repeat(201))
                .releaseDate(LocalDate.parse("2021-12-03")).duration(30).build();

        Exception exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));

        assertTrue(exception.getMessage().contains("размер должен находиться в диапазоне от 0 до 200"));
    }

    @Test
    void shouldDropValidateExpWhenAddFilmWithBlankName() {
        Film film = Film.builder().name("").description("testexample123").releaseDate(LocalDate.parse("2021-12-03")).duration(30).build();

        Exception exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));

        assertTrue(exception.getMessage().contains("не должно быть пустым"));
    }

}