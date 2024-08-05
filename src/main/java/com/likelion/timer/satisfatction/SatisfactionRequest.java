package com.likelion.timer.satisfatction;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SatisfactionRequest {

    @Min(0) @Max(5) @NotNull
    private int rating;

    private String comment;

    @NotNull
    private PartType partType;

    private Integer year;
    private Integer month;
    private Integer day;
}
