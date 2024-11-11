package ru.yandex.practicum.filmorate.dal.storage.film;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.BaseDBStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

@Repository("FilmDBStorage")
@Primary
public class FilmDBStorage extends BaseDBStorage<Film> implements FilmStorage {

    private static final String FIND_ALL_QUERY =
            "SELECT film_id, film_name, description, release_date, duration, rating_id FROM films";
    private static final String FIND_BY_ID_QUERY =
            "SELECT film_id, film_name, description, release_date, duration, rating_id FROM films WHERE film_id = ?";
    private static final String INSERT_QUERY =
            "INSERT INTO films (film_name, description, duration, release_date, rating_id) " +
                    "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY =
            "UPDATE films SET film_name = ?, description = ?, duration = ?, release_date = ?,  rating_id = ? WHERE film_id = ?";
    private static final String POPULAR_FILM_QUERY =
            "SELECT f.film_id, f.film_name, f.description, f.release_date, f.duration, f.rating_id" +
                    " FROM films f" +
                    " JOIN (SELECT film_id, COUNT(user_id) AS like_count" +
                    " FROM likes" +
                    " GROUP BY film_id" +
                    " ORDER BY like_count DESC) l ON f.film_id = l.film_id LIMIT ?";

    public FilmDBStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Film> getFilms() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Collection<Film> popularFilm(int count) {
        return findMany(POPULAR_FILM_QUERY, count);

    }

    @Override
    public Film addFilm(Film film) {
        int id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId()
        );
        film.setId(id);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId()
        );
        return film;
    }
}
