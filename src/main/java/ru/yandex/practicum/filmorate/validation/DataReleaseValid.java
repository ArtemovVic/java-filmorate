package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DataReleaseValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataReleaseValid {
    String message() default "Дата релиза не может быть раньше 28.12.1985";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
