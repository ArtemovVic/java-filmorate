package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.RatingMPADto;
import ru.yandex.practicum.filmorate.service.FilmDBService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Validated
public class MPAController {
    private final FilmDBService filmService;

    @Autowired
    public MPAController(FilmDBService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<RatingMPADto> findAllMpa() {
        return filmService.getAllMpa();
    }

    @GetMapping("/{id}")
    public RatingMPADto findMpaById(@PathVariable int id) {
        return filmService.getMpaById(id);
    }
}
