package com.hutchison.swandraft.model.dto.enter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hutchison.swandraft.model.dto.player.PlayerDto;
import com.hutchison.swandraft.model.player.Player;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = EnterResponse.EnterResponseBuilder.class)
public class EnterResponse {
    PlayerDto player;
    String message;
    @NonNull EnterResponseType type;

    public enum EnterResponseType {
        OK, ERROR;
    }

    public static EnterResponse ok(Player player) {
        return EnterResponse.builder()
                .player(PlayerDto.fromPlayer(player))
                .message(player.getName() + " successfully entered.")
                .type(EnterResponseType.OK)
                .build();
    }

    public static EnterResponse error(Player player, String message) {
        return EnterResponse.builder()
                .player(PlayerDto.fromPlayer(player))
                .message(message)
                .type(EnterResponseType.ERROR)
                .build();
    }

    public static EnterResponse error(String message) {
        return EnterResponse.builder()
                .message(message)
                .type(EnterResponseType.ERROR)
                .build();
    }

}
