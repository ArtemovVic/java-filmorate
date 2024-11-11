package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.mappers.mappersimpl.FilmMapper;
import ru.yandex.practicum.filmorate.dal.mappers.mappersimpl.GenreMapper;
import ru.yandex.practicum.filmorate.dal.mappers.mappersimpl.MPAMapper;
import ru.yandex.practicum.filmorate.dal.storage.film.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.RatingMPADto;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class FilmDBService {

    private final FilmStorage filmDBStorage;
    private final GenreDBStorage genreDBStorage;
    private final LikeDBStorage likeDBStorage;
    private final UserDBService userDBService;
    private final MpaDBStorage mpaDBStorage;
    private final FilmGenreDBStorage filmGenreDBStorage;


    public FilmDBService(@Qualifier("FilmDBStorage") FilmStorage filmDBStorage, GenreDBStorage genreDBStorage,
                         UserDBService userDBService, LikeDBStorage likeDBStorage,
                         MpaDBStorage mpaDBStorage, FilmGenreDBStorage filmGenreDBStorage) {
        this.filmDBStorage = filmDBStorage;
        this.genreDBStorage = genreDBStorage;
        this.userDBService = userDBService;
        this.likeDBStorage = likeDBStorage;
        this.mpaDBStorage = mpaDBStorage;
        this.filmGenreDBStorage = filmGenreDBStorage;

    }

    public FilmDto createFilm(Film film) {
        if (mpaDBStorage.findMpaByID(film.getMpa().getId()).isEmpty()) {
            throw new ValidationException("Неверный id mpa");
        }
        filmDBStorage.addFilm(film);
        if (!film.getGenres().isEmpty()) {
            addToFilmGenreDB(film);
        }
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto updateFilm(Film film) {
        checkForUpdate(film);
        filmDBStorage.updateFilm(film);
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto getFilmById(int id) {
        if (filmDBStorage.getFilmById(id).isEmpty()) {
            log.error("Пользователь ввел неверный id");
            throw new ValidationException("Неверный id фильма");
        }
        Film film = filmDBStorage.getFilmById(id).get();
        searchAndSetMpa(film);
        searchAndSetGenre(film);
        return FilmMapper.mapToFilmDto((film));
    }

    public Collection<FilmDto> getAllFilms() {
        return listFilmToListDto(filmDBStorage.getFilms());

    }

    public void addLike(int filmId, int userId) {
        userDBService.checkUserId(userId);
        likeDBStorage.addLikeToFilm(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        userDBService.checkUserId(userId);
        likeDBStorage.deleteLike(filmId, userId);
    }

    public List<FilmDto> popularFilm(int count) {
        return listFilmToListDto(filmDBStorage.popularFilm(count));
    }

    public List<RatingMPADto> getAllMpa() {
        return mpaDBStorage.getAllMpa().stream()
                .map(MPAMapper::mapToMpaDto)
                .toList();
    }

    public RatingMPADto getMpaById(int id) {
        if (mpaDBStorage.findMpaByID(id).isEmpty()) {
            log.error("Пользователь ввел неверный id");
            throw new NotFoundException("Неверный id mpa");
        }
        return MPAMapper.mapToMpaDto(mpaDBStorage.findMpaByID(id).get());
    }

    public List<GenreDto> getAllGenre() {
        return genreDBStorage.getAllGenre().stream()
                .map(GenreMapper::mapToGenreDto)
                .toList();
    }

    public GenreDto getGenreById(int id) {
        if (genreDBStorage.getGenreById(id).isEmpty()) {
            log.error("Пользователь ввел неверный id");
            throw new NotFoundException("Неверный id жанра");
        }
        return GenreMapper.mapToGenreDto(genreDBStorage.getGenreById(id).get());
    }

    public void checkForUpdate(Film film) {
        if (isIdNull(film.getId())) {
            log.error("Пользователь не ввел id");
            throw new InternalServerException("Id должен быть указан");
        }
        if (filmDBStorage.getFilmById(film.getId()).isEmpty()) {

            log.error("Фильм с= " + film.getId() + " не найден");
            throw new InternalServerException("Фильм с id = " + film.getId() + " не найден");
        }
    }

    public boolean isIdNull(int id) {
        return id == 0;
    }


    public void searchAndSetMpa(Film film) {
        if (mpaDBStorage.findMpaByFilmId(film.getId()).isPresent()) {
            film.setMpa(mpaDBStorage.findMpaByFilmId(film.getId()).get());
        }
    }

    public void searchAndSetGenre(Film film) {
        film.getGenres().addAll(filmGenreDBStorage.getGenreForFilm(film.getId()));
    }

    public void addToFilmGenreDB(Film film) {
        filmGenreDBStorage.insertGenreForFilm(film.getId(), film.getGenres());
    }

    public List<FilmDto> listFilmToListDto(Collection<Film> listFilm) {
        return listFilm
                .stream()
                /*.map(this::setMpa)
                .map(this::setGenre)*/
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }
}
