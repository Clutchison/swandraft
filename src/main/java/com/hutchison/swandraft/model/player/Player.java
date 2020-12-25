package com.hutchison.swandraft.model.player;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

import javax.persistence.Transient;

@Value
@Builder
public class Player {
    @NonNull Long playerId;
    @NonNull String name;
    @NonNull Integer discriminator;
    @NonNull Long discordId;
    int gamesPlayed;
    int totalScore;
    int totalTrophies;

    @Transient
    @NonFinal
    PlayerIdentifier identifier;

    public String getIdentifier() {
        identifier = identifier == null ? PlayerIdentifier.fromPlayer(this) : identifier;
        return identifier.toString();
    }
}
