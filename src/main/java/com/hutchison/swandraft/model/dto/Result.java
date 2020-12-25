package com.hutchison.swandraft.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = Result.ResultBuilder.class)
@AllArgsConstructor
public class Result {
    @NonNull
    Long discordId;
    @NonNull
    int points;
}
