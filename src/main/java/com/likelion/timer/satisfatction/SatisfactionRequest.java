package com.likelion.timer.satisfatction;

import com.likelion.timer.domain.model.PartTypeEnum;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//만족도 항목을 추가하거나 수정할 때 필요한 요청 데이터
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SatisfactionRequest {

    @Min(0) @Max(5) @NotNull //rating 필드는 0 이상 5 이하의 값을 가져야 하며 null 허용 x
    private int rating;

    private String comment; 

    @NotNull
    private PartTypeEnum partType;

    private Integer year;
    private Integer month;
    private Integer day;
}
