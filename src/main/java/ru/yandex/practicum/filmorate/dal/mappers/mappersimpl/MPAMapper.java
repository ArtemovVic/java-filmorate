package ru.yandex.practicum.filmorate.dal.mappers.mappersimpl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.RatingMPADto;
import ru.yandex.practicum.filmorate.model.RatingMPA;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MPAMapper {
    public static RatingMPADto mapToMpaDto(RatingMPA mpa) {
        return RatingMPADto.builder()
                .id(mpa.getId())
                .name(mpa.getName())
                .build();
    }
}
