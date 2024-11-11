package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.dal.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

@Service
@Slf4j
public class LikeService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public LikeService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Integer userId, Integer filmId) {
        validateUserAndFilm(userId, filmId);
        //filmStorage.getFilmById(filmId).getLikes().add(userId);
        log.info("Like successfully added to film: " + filmId);
    }

    public void removeLike(Integer userId, Integer filmId) {
        validateUserAndFilm(userId, filmId);
        //filmStorage.getFilmById(filmId).getLikes().remove(userId);
        log.info("Like successfully remove from film: " + filmId);
    }

    private void validateUserAndFilm(Integer userId, Integer filmId) {
        if (userStorage.getUserById(userId) == null) {
            log.error("Invalid id for user: {}", userId);
            throw new NotFoundException("User with ID=" + userId + " not found!");
        }
        if (filmStorage.getFilmById(filmId) == null) {
            log.error("Invalid id for film: {}", filmId);
            throw new NotFoundException("Film with ID=" + filmId + " not found!");
        }
    }
}
