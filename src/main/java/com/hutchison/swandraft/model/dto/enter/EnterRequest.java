package com.hutchison.swandraft.model.dto.enter;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class EnterRequest {
    @NonNull
    Long discordId;
    @NonNull
    String name;
    @Min(1000)
    @Max(9999)
    int discriminator;
}
