package ru.yandex.practicum.filmorate.dal.storage.film;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.BaseDBStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

@Repository
@Slf4j
public class FilmGenreDBStorage extends BaseDBStorage<Genre> {
    private static final String GENRE_FOR_FILM_QUERY =
            "SELECT * FROM film_genres, genres WHERE film_genres.film_id = ? AND film_genres.genre_id = genres.genre_id ORDER BY genres.genre_id asc";

    public FilmGenreDBStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> getGenreForFilm(int id) {
        return findMany(GENRE_FOR_FILM_QUERY, id);
    }

    public void insertGenreForFilm(int filmId, Collection<Genre> genres) {

        String checkGenreSql = "SELECT COUNT(*) FROM genres WHERE genre_id = ?";
        List<Genre> validGenres = genres.stream()
                .filter(genre -> jdbc.queryForObject(checkGenreSql, Integer.class, genre.getId()) > 0)
                .toList();

        if (validGenres.isEmpty()) {
            log.warn("No valid genres found for film with ID: {}", filmId);
            throw new ValidationException("No valid genres found for film");
        }

        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        List<Object[]> batchArgs = validGenres.stream()
                .map(genre -> new Object[]{filmId, genre.getId()})
                .toList();

        log.info("Adding genres to film {}: {}", filmId, validGenres.stream().map(Genre::getId).toList());
        jdbc.batchUpdate(sql, batchArgs);
    }
}
