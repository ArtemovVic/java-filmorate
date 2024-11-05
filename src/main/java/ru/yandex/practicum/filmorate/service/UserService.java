package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
@Slf4j
public class UserService {

    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public Collection<User> getUsers() {
        log.info("Requested list of users");
        return storage.getUsers();
    }

    public User getUserById(Long id) {
        User result = storage.getUserById(id);
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

    public void addFriend(Long id, Long friendId) {
        storage.addFriend(id, friendId);
        log.info("Friend successfully added ID: {}", friendId);
    }

    public void removeFriend(Long id, Long friendId) {
        storage.removeFriend(id, friendId);
        log.info("Friend successfully remove ID: {}", friendId);

    }

    public Collection<User> getFriends(Long id) {
        Collection<User> result = storage.getFriends(id);
        log.info("Requested list of friends: {}", result.size());
        return result;
    }

    public Collection<User> getCommonFriends(Long id, Long friendId) {
        Collection<User> result = storage.getCommonFriends(id, friendId);
        log.info("Requested list of CommonFriends: {}", result.size());
        return result;
    }
}
