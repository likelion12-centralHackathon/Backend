package com.likelion.timer.challenge.mapper;

import com.likelion.timer.challenge.DTO.BootchallengeCreateDTO;
import com.likelion.timer.challenge.DTO.BootchallengeDTO;
import com.likelion.timer.challenge.DTO.BootchallengeResponseDTO;
import com.likelion.timer.challenge.DTO.BootchallengeUpdateDTO;
import com.likelion.timer.challenge.domain.entity.Bootchallenge;


public class BootchallengeMapper {

    public static BootchallengeDTO toDTO(Bootchallenge bootchallenge) {
        return new BootchallengeDTO(
                bootchallenge.getId(),
                bootchallenge.getUser(),
                bootchallenge.getCategory(),
                bootchallenge.getTitle(),
                bootchallenge.getContent(),
                bootchallenge.getAuthMethod(),
                bootchallenge.getEndDate(),
                bootchallenge.getNote(),
                bootchallenge.getState(),
                bootchallenge.getHit(),
                bootchallenge.getParticipants()
        );
    }

    public static Bootchallenge toEntity(BootchallengeCreateDTO dto) {
        Bootchallenge bootchallenge = new Bootchallenge();
        bootchallenge.setCategory(dto.getCategory());
        bootchallenge.setTitle(dto.getTitle());
        bootchallenge.setContent(dto.getContent());
        bootchallenge.setAuthMethod(dto.getAuthMethod());
        bootchallenge.setEndDate(dto.getEndDate());
        bootchallenge.setNote(dto.getNote());
        return bootchallenge;
    }

    public static Bootchallenge toEntity(BootchallengeUpdateDTO dto, Bootchallenge bootchallenge) {
        bootchallenge.setTitle(dto.getTitle());
        bootchallenge.setContent(dto.getContent());
        bootchallenge.setAuthMethod(dto.getAuthMethod());
        bootchallenge.setEndDate(dto.getEndDate());
        bootchallenge.setNote(dto.getNote());
        return bootchallenge;
    }

    public static BootchallengeResponseDTO toResponseDTO(Bootchallenge bootchallenge) {
        return new BootchallengeResponseDTO(
                bootchallenge.getId(),
                bootchallenge.getUser(),
                bootchallenge.getCategory(),
                bootchallenge.getTitle(),
                bootchallenge.getContent(),
                bootchallenge.getAuthMethod(),
                bootchallenge.getEndDate(),
                bootchallenge.getNote(),
                bootchallenge.getState(),
                bootchallenge.getHit(),
                bootchallenge.getParticipants()
        );
    }
}
