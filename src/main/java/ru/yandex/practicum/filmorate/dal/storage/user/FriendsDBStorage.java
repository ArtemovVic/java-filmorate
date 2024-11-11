package ru.yandex.practicum.filmorate.dal.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.BaseDBStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class FriendsDBStorage extends BaseDBStorage<User> {
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO friends (user_id, friend_id, status) " +
            "VALUES (?,?,?)";
    private static final String UPDATE_FRIEND_STATUS_QUERY = "UPDATE friends " +
            "SET status = ? WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_STATUS_QUERY = "SELECT status FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_ALL_FRIEND_FOR_ID_QUERY =
            "SELECT u.user_id, u.user_name, u.email, u.login, u.birthday" +
                    " FROM friends f" +
                    " JOIN users u ON f.friend_id = u.user_id" +
                    " WHERE f.user_id = ?";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_COMMON_FRIEND_QUERY =
            "SELECT u.user_id, u.user_name, u.email, u.login, u.birthday" +
                    " FROM friends f" +
                    " JOIN users u ON f.friend_id = u.user_id" +
                    " WHERE f.user_id = ?" +
                    " INTERSECT" +
                    " SELECT u.user_id, u.user_name, u.email, u.login, u.birthday" +
                    " FROM friends f" +
                    " JOIN users u ON f.friend_id = u.user_id" +
                    " WHERE f.user_id = ?";
    protected final RowMapper<Integer> mapperInt;

    public FriendsDBStorage(JdbcTemplate jdbc, RowMapper<User> mapper, RowMapper<Integer> mapperInt) {
        super(jdbc, mapper);
        this.mapperInt = mapperInt;
    }

    public void insertFriend(int userId, int friendId, boolean status) {
        insertWithoutKey(
                INSERT_FRIEND_QUERY,
                userId,
                friendId,
                status
        );
        log.info("Добавили пользователя " + friendId + " в друзья к пользователю " + userId);
    }

    public void updateFriendStatus(int userId, int friendId, boolean status) {
        update(
                UPDATE_FRIEND_STATUS_QUERY,
                status,
                userId,
                friendId
        );
    }


    public List<User> getFriendsById(int userId) {
        return findMany(FIND_ALL_FRIEND_FOR_ID_QUERY, userId);
    }

    /*public Optional<User> checkFriendsInDB(int userId, int friendId) {
        return findOne(FIND_STATUS_QUERY, mapperInt, userId, friendId);
    }*/

    public Optional<Boolean> checkFriendsInDB(int userId, int friendId) {
        String sql = "SELECT status FROM friends WHERE user_id = ? AND friend_id = ?";
        try {
            Boolean status = jdbc.queryForObject(sql, Boolean.class, userId, friendId);
            return Optional.ofNullable(status);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteFriend(int userId, int friendId) {
        update(
                DELETE_FRIEND_QUERY,
                userId,
                friendId
        );
        log.info("Удалили пользователя " + friendId + " из друзей пользователя " + userId);
    }

    public List<User> getCommonFriends(int firstUserId, int secondUserId) {
        return findMany(
                FIND_COMMON_FRIEND_QUERY,
                firstUserId,
                secondUserId
        );
    }
}
