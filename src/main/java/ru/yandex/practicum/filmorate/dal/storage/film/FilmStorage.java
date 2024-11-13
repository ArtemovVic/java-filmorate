package ru.yandex.practicum.filmorate.dal.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> getFilms();

    Optional<Film> getFilmById(Integer id);

    Film addFilm(Film film);

    Film updateFilm(Film newFilm);

    Collection<Film> popularFilm(int count);


}
