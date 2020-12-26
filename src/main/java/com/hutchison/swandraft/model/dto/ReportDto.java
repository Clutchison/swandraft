package com.hutchison.swandraft.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Value
@Builder
@JsonDeserialize(builder = ReportDto.ReportDtoBuilder.class)
@AllArgsConstructor
public class ReportDto {
    @NonNull
    Long discordId;
    @NonNull @Min(0) @Max(2)
    Integer roundsWon;
    @NonNull @Min(0) @Max(2)
    Integer roundsLost;
    @NonNull @Min(0) @Max(3)
    Integer roundsDrawn;
}
