package ru.yandex.practicum.filmorate.service;


import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.mappers.mappersimpl.UserMapper;
import ru.yandex.practicum.filmorate.dal.storage.user.FriendsDBStorage;
import ru.yandex.practicum.filmorate.dal.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserDBService {

    private final UserStorage userDBStorage;
    private final FriendsDBStorage friendsDBStorage;

    public UserDBService(@Qualifier("UserDBStorage") UserStorage userDBStorage, FriendsDBStorage friendsDBStorage) {
        this.userDBStorage = userDBStorage;
        this.friendsDBStorage = friendsDBStorage;
    }

    public List<UserDto> getAllUsers() {
        return listUserToListDto(userDBStorage.getUsers());

    }

    public Optional<UserDto> getUserByID(Integer id) {
        return userDBStorage.getUserById(id).map(UserMapper::mapToUserDto);
    }

    public UserDto addUser(User user) {
        checkForCreate(user);
        return UserMapper.mapToUserDto(userDBStorage.addUser(user));
    }

    public UserDto updateUser(User user) {
        checkForUpdate(user);
        return UserMapper.mapToUserDto(userDBStorage.updateUser(user));
    }


    public void addFriend(int idUser, int idFriend) {
        if (idUser == idFriend) {
            log.error("Пользователь указал одинаковые id");
            throw new ValidationException("Вы не можете указать одинаковые id для двух пользователей");
        }
        checkUserId(idUser);
        checkUserId(idFriend);

        if (friendsDBStorage.checkFriendsInDB(idUser, idFriend).isEmpty()) {
            friendsDBStorage.insertFriend(idUser, idFriend, false);
        } else {
            log.error("Пользователь повторно добавил в друзья пользователя");
            throw new ValidationException("Пользователь с id " + idFriend + " уже есть в списке друзей id " + idUser);
        }


        //updateFriendStatus(idUser, idFriend);
        listUserToListDto(friendsDBStorage.getFriendsById(idUser));

    }

    public List<UserDto> friendsListById(int id) {
        checkUserId(id);
        return listUserToListDto(friendsDBStorage.getFriendsById(id));
    }

    public void removeFriend(int idUser, int idFriend) {
        checkUserId(idUser);
        checkUserId(idFriend);

        /*Optional<Boolean> friendship = friendsDBStorage.checkFriendsInDB(idUser, idFriend);
        if (friendship.isEmpty()) {
            log.error("Пользователи не являются друзьями");
            throw new ValidationException("Пользователи не являются друзьями");
        }*/

        friendsDBStorage.deleteFriend(idUser, idFriend);

        listUserToListDto(friendsDBStorage.getFriendsById(idUser));
    }

    public void checkForCreate(User user) {
        if (isValueNull(user.getName()) || isLineBlank(user.getName())) {
            user.setName(user.getLogin());
        }
    }

    public void checkForUpdate(User user) {
        if (isIdNull(user.getId())) {
            log.error("Пользователь не ввел id");
            throw new ValidationException("Id должен быть указан");
        }
        checkUserId(user.getId());
        checkForCreate(user);
    }

    public void checkUserId(int id) {
        if (userDBStorage.getUserById(id).isEmpty()) {
            log.error("Пользователь ввел неверный id");
            throw new NotFoundException("Пользователя с таким id нет");
        }
    }

    public List<UserDto> listOfCommonFriends(int idUserOne, int idUserTwo) {
        checkUserId(idUserOne);
        checkUserId(idUserTwo);
        return listUserToListDto(friendsDBStorage.getCommonFriends(idUserOne, idUserTwo));
    }

    public boolean isIdNull(int id) {
        return id == 0;
    }

    public boolean isLineBlank(String value) {
        return value.isBlank();
    }

    public boolean isValueNull(String value) {
        return value == null;
    }

    public List<UserDto> listUserToListDto(Collection<User> listUser) {
        return listUser
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }


}
