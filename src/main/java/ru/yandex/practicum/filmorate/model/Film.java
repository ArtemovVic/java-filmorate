package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    private Long id;
    @NotBlank(message = "не должно быть пустым")
    private String name;
    @NotBlank(message = "не должно быть пустым")
    @Size(max = 200, message = "размер должен находиться в диапазоне от 0 до 200")
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @PositiveOrZero(message = "должно быть больше или равно 0")
    private Integer duration;
    private final Set<Long> likes = new HashSet<>();
}
