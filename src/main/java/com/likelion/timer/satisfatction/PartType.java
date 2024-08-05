package com.likelion.timer.satisfatction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PartType {
    ALL(0, "ALL"),
    EYES(1, "Eyes"),
    NECK(2, "Neck"),
    WAIST(3, "Waist"),
    LEG(4, "Leg"),
    ETC(5, "Etc");

    private final int code;
    private final String value;
}