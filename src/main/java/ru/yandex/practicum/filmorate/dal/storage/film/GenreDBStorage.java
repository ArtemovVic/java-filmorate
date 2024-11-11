package ru.yandex.practicum.filmorate.dal.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.BaseDBStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreDBStorage extends BaseDBStorage<Genre> {
    private static final String GENRE_QUERY = "SELECT genre_id, genre_name FROM genres WHERE genre_id = ?";
    private static final String ALL_GENRE_QUERY = "SELECT genre_id, genre_name FROM genres";

    public GenreDBStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Optional<Genre> getGenreById(int id) {
        return findOne(GENRE_QUERY, id);
    }

    public List<Genre> getListGenre(Collection<Genre> list) {
        String placeholders = String.join(",", Collections.nCopies(list.size(), "?"));
        String listGenreQuery = "SELECT genre_id, genre_name FROM genres WHERE id IN (" + placeholders + ")";
        List<Integer> listInt = list.stream()
                .map(Genre::getId)
                .toList();
        Object[] params = listInt.toArray(new Object[0]);
        return findMany(listGenreQuery, params);
    }

    public List<Genre> getAllGenre() {
        return findMany(ALL_GENRE_QUERY);
    }
}
