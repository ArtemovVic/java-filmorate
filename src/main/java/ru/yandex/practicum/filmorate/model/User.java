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
    @NotBlank(message = "не должно быть пустым")
    @Email(message = "должно иметь формат адреса электронной почты")
    private String email;
    @NotBlank(message = "не должно быть пустым")
    private String login;
    private String name;
    @PastOrPresent(message = "должно содержать прошедшую дату или сегодняшнее число")
    @NonNull
    private LocalDate birthday;
}
