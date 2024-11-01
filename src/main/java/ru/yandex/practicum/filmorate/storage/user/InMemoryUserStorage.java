package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User getUserById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
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
        // формируем дополнительные данные
        user.setId(getNextId());
        // сохраняем нового пользователя в памяти приложения
        users.put(user.getId(), user);
        log.info("User successfully added: " + user);
        return user;
    }

    @Override
    public User updateUser(User newUser) {
        if (newUser.getName() == null) {
            newUser.setName(newUser.getLogin());
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());

            // если пользователь найден и все условия соблюдены, обновляем её содержимое
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());

            log.info("User successfully updated: " + oldUser);
            return oldUser;
        }
        log.error("Invalid id: {}", newUser.getId());
        throw new NotFoundException("User with ID=" + newUser.getId() + " not found!");
    }

    @Override
    public void addFriend(Long id, Long friendId) {

        if (!users.containsKey(id) || !users.containsKey(friendId)) {
            log.error("One of the users not found: ID=" + id + " or ID=" + friendId);
            throw new NotFoundException("One of the users not found: ID=" + id + " or ID=" + friendId);
        }

        users.get(id).getFriendIds().add(friendId);
        users.get(friendId).getFriendIds().add(id);
        log.info("Friend successfully added to User with ID: " + id);
    }

    @Override
    public void removeFriend(Long id, Long friendId) {

        if (!users.containsKey(id) || !users.containsKey(friendId)) {
            log.error("One of the users not found: ID=" + id + " or ID=" + friendId);
            throw new NotFoundException("One of the users not found: ID=" + id + " or ID=" + friendId);
        }

        users.get(id).getFriendIds().remove(friendId);
        users.get(friendId).getFriendIds().remove(id);
        log.info("Friend successfully removed from User with ID: " + id);

    }

    @Override
    public Collection<User> getFriends(Long id) {
        if (!users.containsKey(id)) {
            log.error("Invalid id: {}", id);
            throw new NotFoundException("User with ID=" + id + " not found!");
        }

        List<User> friends = users.get(id).getFriendIds().stream()
                .map(users::get)
                .toList();

        log.info("Retrieved friends for user ID {}: ", id);
        return friends;
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long friendId) {
        if (!users.containsKey(id) || !users.containsKey(friendId)) {
            throw new NotFoundException("One of the users not found: ID=" + id + " or ID=" + friendId);
        }

        Set<Long> commonFriendIds = new HashSet<>(users.get(id).getFriendIds());
        commonFriendIds.retainAll(users.get(friendId).getFriendIds());

        List<User> commonFriends = commonFriendIds.stream()
                .map(users::get)
                .toList();

        log.info("Retrieved common friends between user ID {} and user ID {}: {}", id, friendId, commonFriends);
        return commonFriends;
    }

    private Long getNextId() {
        Long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
