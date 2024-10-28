package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {
    @Autowired
    FilmController filmController;


    @Test
    void shouldAddFilm() {
        Film film = Film.builder().name("testexample").description("testexample123").releaseDate(LocalDate.parse("2021-12-03")).duration(30).build();
        Film addedFilm = filmController.addFilm(film);

        assertNotNull(addedFilm);
        assertEquals(film.getName(), addedFilm.getName());
        assertEquals(film.getDescription(), addedFilm.getDescription());
        assertEquals(film.getReleaseDate(), addedFilm.getReleaseDate());
        assertNotNull(addedFilm.getId());
    }

    @Test
    void shouldDropValidateExpWhenAddFilmWithBlankName() {
        Film film = Film.builder().name("").description("testexample123").releaseDate(LocalDate.parse("2021-12-03")).duration(30).build();

        Exception exception = assertThrows(jakarta.validation.ValidationException.class, () -> {
            filmController.addFilm(film);
        });

        assertTrue(exception.getMessage().contains("не должно быть пустым"));
    }

    @Test
    void shouldDropValidateExpWhenAddFilmWithInvalidDescription() {
        Film film = Film.builder().name("testexample").description("testexample123testexample123testexatestexample123testexample123testexample123testexample123testexample123testexample123testexample123testexample123testexample123testexample123testexample123testexample123testexample123testexample123testexample123testexample123testexample123testexample123testexample123testexample123testexample123testexample123testexample123testexample123mple123testexample123testexample123testexample123testexample123testexample123")
                .releaseDate(LocalDate.parse("2021-12-03")).duration(30).build();

        Exception exception = assertThrows(jakarta.validation.ValidationException.class, () -> {
            filmController.addFilm(film);
        });

        assertTrue(exception.getMessage().contains("размер должен находиться в диапазоне от 0 до 200"));
    }

    @Test
    void shouldDropValidateExpWhenAddFilmWithInvalidReleaseDate() {
        Film film = Film.builder().name("testexample").description("testexample123").releaseDate(LocalDate.parse("1021-12-03")).duration(30).build();

        Exception exception = assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });

        assertTrue(exception.getMessage().contains("Некорректная дата релиза"));
    }

    @Test
    void shouldDropValidateExpWhenAddFilmWithInvalidDuration() {
        Film film = Film.builder().name("testexample").description("testexample123").releaseDate(LocalDate.parse("2021-12-03")).duration(-30).build();

        Exception exception = assertThrows(jakarta.validation.ValidationException.class, () -> {
            filmController.addFilm(film);
        });

        assertTrue(exception.getMessage().contains("должно быть больше или равно 0"));
    }

}