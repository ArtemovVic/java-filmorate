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
    private Long id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    @PastOrPresent
    @NonNull
    private LocalDate birthday;
}
