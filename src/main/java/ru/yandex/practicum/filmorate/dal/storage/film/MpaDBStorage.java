package ru.yandex.practicum.filmorate.dal.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.BaseDBStorage;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaDBStorage extends BaseDBStorage<RatingMPA> {
    private static final String FIND_All_QUERY = "SELECT rating_id, rating_name FROM rating_mpa";
    private static final String FIND_MPA_BY_ID_QUERY = "SELECT rating_id, rating_name FROM rating_mpa WHERE rating_id= ?";
    private static final String FIND_MPA_BY_FILM_ID_QUERY =
            "SELECT r.rating_id, r.rating_name FROM films f JOIN rating_mpa r ON f.rating_id=r.rating_id WHERE f.film_id = ?";

    public MpaDBStorage(JdbcTemplate jdbc, RowMapper<RatingMPA> mapper) {
        super(jdbc, mapper);
    }

    public List<RatingMPA> getAllMpa() {
        return findMany(FIND_All_QUERY);
    }

    public Optional<RatingMPA> findMpaByID(int id) {
        return findOne(FIND_MPA_BY_ID_QUERY, id);
    }

    public Optional<RatingMPA> findMpaByFilmId(int id) {
        return findOne(FIND_MPA_BY_FILM_ID_QUERY, id);
    }
}
