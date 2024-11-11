package ru.yandex.practicum.filmorate.dal.storage.user;


import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.BaseDBStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

@Repository("UserDBStorage")
@Primary
public class UserDBStorage extends BaseDBStorage<User> implements UserStorage {
    private static final String FIND_All_QUERY = "SELECT user_id, email, login, user_name, birthday FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT user_id, email, login, user_name, birthday FROM users WHERE user_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users (email, login, user_name, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?,user_name = ?, birthday = ? WHERE user_id = ?";

    public UserDBStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<User> getUsers() {
        return findMany(FIND_All_QUERY);
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public User addUser(User user) {
        int id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User newUser) {
        Optional<User> existingUser = getUserById(newUser.getId());
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + newUser.getId() + " does not exist");
        }
        update(
                UPDATE_QUERY,
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                newUser.getBirthday(),
                newUser.getId()
        );
        return newUser;

    }

}
