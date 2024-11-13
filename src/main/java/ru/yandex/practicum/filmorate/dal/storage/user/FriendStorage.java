package ru.yandex.practicum.filmorate.dal.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FriendStorage {
    void addFriend(Integer id, Integer friendId);

    void removeFriend(Integer id, Integer friendId);

    Collection<User> getFriends(Integer id);

    Collection<User> getCommonFriends(Integer id, Integer friendId);
}
