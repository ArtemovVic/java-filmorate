package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;

public interface UserStorage {

    Collection<User> getUsers();

    User getUserById(Long id);

    User addUser(User user);

    User updateUser(User newUser);

    void addFriend(Long id, Long friendId);

    void removeFriend(Long id, Long friendId);

    Collection<User> getFriends(Long id);

    Collection<User> getCommonFriends(Long id, Long friendId);
}
