package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
public class UserService {

    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public Collection<User> getUsers() {
        return storage.getUsers();
    }

    public User getUserById(Long id) {
        return storage.getUserById(id);
    }

    public User addUser(User user) {
        return storage.addUser(user);
    }

    public User updateUser(User newUser) {
        return storage.updateUser(newUser);
    }

    public void addFriend(Long id, Long friendId) {
        storage.addFriend(id, friendId);
    }

    public void removeFriend(Long id, Long friendId) {
        storage.removeFriend(id, friendId);
    }

    public Collection<User> getFriends(Long id) {
        return storage.getFriends(id);
    }

    public Collection<User> getCommonFriends(Long id, Long friendId) {
        return storage.getCommonFriends(id,friendId);
    }
}
