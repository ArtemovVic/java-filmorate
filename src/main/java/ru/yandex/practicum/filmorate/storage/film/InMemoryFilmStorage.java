package ru.yandex.practicum.filmorate.storage.film;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public Film getFilmById(Long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        }
        log.error("Film not found ID: {}", id);
        throw new NotFoundException("User with ID=" + id + " not found!");
    }

    @Override
    public Film addFilm(Film film) {
        if (!film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))) {
            log.error("Invalid Date for film ID: {}", film.getId());
            throw new ValidationException("Некорректная дата релиза");
        }
        if (films.containsValue(film)) {
            log.error("Film already added ID: {}", film.getId());
            throw new DuplicatedDataException("Этот фильм уже добавлен");
        }

        // формируем дополнительные данные
        film.setId(getNextId());
        // сохраняем нового пользователя в памяти приложения
        films.put(film.getId(), film);
        log.info("Film successfully added: " + film);
        return film;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());

            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            if (!newFilm.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))) {
                log.error("Invalid Date for film ID: {}", newFilm.getId());
                throw new ValidationException("Некорректная дата релиза");
            }
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());

            log.info("Film successfully updated: " + oldFilm);

            return oldFilm;
        }
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    @Override
    public Long getNextId() {
        Long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
