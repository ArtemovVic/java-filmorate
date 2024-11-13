package ru.yandex.practicum.filmorate.dal.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;


@Slf4j
@Component("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage, FriendStorage {

    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        if (users.containsKey(id)) {
            return Optional.ofNullable(users.get(id));
        }
        throw new NotFoundException("User with ID=" + id + " not found!");
    }

    @Override
    public User addUser(User user) {
        if (users.values().stream().anyMatch(existUser -> existUser.getEmail().equals(user.getEmail()))) {
            log.error("Duplicate email: {}", user.getEmail());
            throw new DuplicatedDataException("This email is already in use");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User updateUser(User newUser) {
        if (newUser.getName() == null) {
            newUser.setName(newUser.getLogin());
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());

            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());

            return oldUser;
        }
        log.error("Invalid id: {}", newUser.getId());
        throw new NotFoundException("User with ID=" + newUser.getId() + " not found!");
    }


    @Override
    public void addFriend(Integer id, Integer friendId) {

        if (!users.containsKey(id) || !users.containsKey(friendId)) {
            log.error("One of the users not found: ID=" + id + " or ID=" + friendId);
            throw new NotFoundException("One of the users not found: ID=" + id + " or ID=" + friendId);
        }

        users.get(id).getFriendIds().add(friendId);
        users.get(friendId).getFriendIds().add(id);
    }

    @Override
    public void removeFriend(Integer id, Integer friendId) {

        if (!users.containsKey(id) || !users.containsKey(friendId)) {
            log.error("One of the users not found: ID=" + id + " or ID=" + friendId);
            throw new NotFoundException("One of the users not found: ID=" + id + " or ID=" + friendId);
        }

        users.get(id).getFriendIds().remove(friendId);
        users.get(friendId).getFriendIds().remove(id);

    }

    @Override
    public Collection<User> getFriends(Integer id) {
        if (!users.containsKey(id)) {
            log.error("Invalid id: {}", id);
            throw new NotFoundException("User with ID=" + id + " not found!");
        }

        return users.get(id).getFriendIds().stream()
                .map(users::get)
                .toList();
    }

    @Override
    public Collection<User> getCommonFriends(Integer id, Integer friendId) {
        if (!users.containsKey(id) || !users.containsKey(friendId)) {
            throw new NotFoundException("One of the users not found: ID=" + id + " or ID=" + friendId);
        }

        Set<Integer> commonFriendIds = new HashSet<>(users.get(id).getFriendIds());
        commonFriendIds.retainAll(users.get(friendId).getFriendIds());

        return commonFriendIds.stream()
                .map(users::get)
                .toList();
    }

    private Integer getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
