package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@EqualsAndHashCode(of = {"name", "releaseDate"})
@Builder
public class Film {
    Long id;
    @NotBlank
    String name;
    @NotBlank
    @Size(max = 200)
    String description;
    @NonNull
    LocalDate releaseDate;
    @PositiveOrZero
    Integer duration;
}
