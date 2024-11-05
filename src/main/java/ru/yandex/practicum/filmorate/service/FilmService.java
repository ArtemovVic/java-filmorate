package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getFilms() {
        log.info("Requested list of users");
        return filmStorage.getFilms();
    }

    public Film getFilmById(Long id) {
        Film result = filmStorage.getFilmById(id);
        log.info("Film successfully received ID: {}", id);
        return result;
    }

    public Film addFilm(Film film) {
        Film result = filmStorage.addFilm(film);
        log.info("Film successfully added ID: {}", result.getId());
        return result;
    }

    public Film updateFilm(Film newFilm) {
        Film result = filmStorage.updateFilm(newFilm);
        log.info("Film successfully update ID: {}", result.getId());
        return result;
    }

    public Collection<Film> popularFilms(int size) {

        List<Film> films = filmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .limit(size)
                .collect(Collectors.toList());
        Collections.reverse(films);
        return films;
    }
}
