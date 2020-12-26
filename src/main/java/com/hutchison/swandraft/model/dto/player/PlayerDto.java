package com.hutchison.swandraft.model.dto.player;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hutchison.swandraft.model.player.Player;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = PlayerDto.PlayerDtoBuilder.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerDto {
    String name;
    Integer discriminator;
    Long discordId;
    Integer gamesPlayed;
    Integer totalScore;
    Integer totalTrophies;

    public static PlayerDto fromPlayer(Player p) {
        return PlayerDto.builder()
                .name(p.getName())
                .discriminator(p.getDiscriminator())
                .discordId(p.getDiscordId())
                .gamesPlayed(p.getGamesPlayed())
                .totalScore(p.getTotalScore())
                .totalTrophies(p.getTotalTrophies())
                .build();
    }
}
