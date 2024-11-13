package ru.yandex.practicum.filmorate.dal.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    Collection<User> getUsers();

    Optional<User> getUserById(Integer id);

    User addUser(User user);

    User updateUser(User newUser);


}

