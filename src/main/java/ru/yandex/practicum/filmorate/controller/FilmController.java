package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.RatingMPADto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmDBService;

import java.util.Collection;
import java.util.List;

@RestController
//@RequestMapping("/films")
@Validated
public class FilmController {

    private final FilmDBService filmService;


    @Autowired
    public FilmController(FilmDBService filmService) {
        this.filmService = filmService;

    }

    @GetMapping("/films")
    public Collection<FilmDto> getFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping("/films")
    public FilmDto addFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping("/films")
    public FilmDto updateFilm(@Valid @RequestBody Film newFilm) {
        return filmService.updateFilm(newFilm);
    }


    @GetMapping("/films/{id}")
    public FilmDto getFilmById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.deleteLike(userId, id);
    }

    @GetMapping("/films/popular")
    public Collection<FilmDto> getPopular(@RequestParam(defaultValue = "10") int count) {
        return filmService.popularFilm(count);
    }

    @GetMapping("/mpa")
    public List<RatingMPADto> findAllMpa() {
        return filmService.getAllMpa();
    }

    @GetMapping("/mpa/{id}")
    public RatingMPADto findMpaById(@PathVariable int id) {
        return filmService.getMpaById(id);
    }

    @GetMapping("/genres")
    public List<GenreDto> findAllGenre() {
        return filmService.getAllGenre();
    }

    @GetMapping("/genres/{id}")
    public GenreDto findGenreById(@PathVariable int id) {
        return filmService.getGenreById(id);
    }


}
