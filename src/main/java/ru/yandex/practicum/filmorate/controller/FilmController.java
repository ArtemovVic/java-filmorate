package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationExceptionOfDate;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
@Validated
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        // проверяем выполнение необходимых условий

        if (films.containsValue(film)) {
            throw new DuplicatedDataException("Этот фильм уже добавлен");
        }
        if (film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))) {
            // формируем дополнительные данные
            film.setId(getNextId());
            // сохраняем нового пользователя в памяти приложения
            films.put(film.getId(), film);
            log.info("Film successfully added: " + film);
            return film;
        }

        throw new ValidationExceptionOfDate("Некорректная дата релиза");

    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        // проверяем необходимые условия

        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());

            // если пользователь найден и все условия соблюдены, обновляем её содержимое

            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());

            log.info("Film successfully updated: " + oldFilm);

            return oldFilm;
        }
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    private Long getNextId() {
        Long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
