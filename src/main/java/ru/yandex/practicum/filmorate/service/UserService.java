package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final InMemoryUserStorage storage;

    @Autowired
    public UserService(InMemoryUserStorage storage) {
        this.storage = storage;
    }

    public Collection<User> getUsers() {
        log.info("Requested list of users");
        return storage.getUsers();
    }

    public Optional<User> getUserById(Integer id) {
        Optional<User> result = storage.getUserById(id);
        log.info("User successfully received ID: {}", id);
        return result;
    }

    public User addUser(User user) {
        User result = storage.addUser(user);
        log.info("User successfully added ID: {}", result.getId());
        return result;
    }

    public User updateUser(User newUser) {
        User result = storage.updateUser(newUser);
        log.info("User successfully update ID: {}", result.getId());
        return result;
    }

    public void addFriend(Integer id, Integer friendId) {
        storage.addFriend(id, friendId);
        log.info("Friend successfully added ID: {}", friendId);
    }

    public void removeFriend(Integer id, Integer friendId) {
        storage.removeFriend(id, friendId);
        log.info("Friend successfully remove ID: {}", friendId);

    }

    public Collection<User> getFriends(Integer id) {
        Collection<User> result = storage.getFriends(id);
        log.info("Requested list of friends: {}", result.size());
        return result;
    }

    public Collection<User> getCommonFriends(Integer id, Integer friendId) {
        Collection<User> result = storage.getCommonFriends(id, friendId);
        log.info("Requested list of CommonFriends: {}", result.size());
        return result;
    }
}
