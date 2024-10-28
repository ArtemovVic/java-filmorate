package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = {"email"})
@Builder
public class User {
    Long id;
    @NotBlank
    @Email
    String email;

    @NotBlank
    String login;
    String name;
    @PastOrPresent
    @NonNull
    LocalDate birthday;
}
