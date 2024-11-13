package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.service.FilmDBService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@Validated
public class GenreController {
    private final FilmDBService filmService;

    @Autowired
    public GenreController(FilmDBService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<GenreDto> findAllGenre() {
        return filmService.getAllGenre();
    }

    @GetMapping("/{id}")
    public GenreDto findGenreById(@PathVariable int id) {
        return filmService.getGenreById(id);
    }
}
