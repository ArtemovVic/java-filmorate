package ru.yandex.practicum.filmorate.dal.mappers.rowmappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MPARowMapper implements RowMapper<RatingMPA> {
    @Override
    public RatingMPA mapRow(ResultSet rs, int rowNum) throws SQLException {
        return RatingMPA.builder()
                .id(rs.getInt("rating_id"))
                .name(rs.getString("rating_name"))
                .build();
    }
}
