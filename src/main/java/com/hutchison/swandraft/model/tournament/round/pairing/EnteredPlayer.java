package com.hutchison.swandraft.model.tournament.round.pairing;

import com.hutchison.swandraft.model.player.Player;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Value
@Builder(toBuilder = true)
public class EnteredPlayer implements Serializable {

    public static final EnteredPlayer NO_OPPONENT = new EnteredPlayer(0L,
            0L,
            "",
            0,
            0,
            -1,
            false,
            new HashSet<>());

    // EnteredPlayer info
    @NonNull Long playerId;
    @NonNull Long discordId;
    @NonNull String name;
    @NonNull Integer discriminator;

    // Tournament info
    int points;
    int startingPosition;
    boolean receivedBye;
    //    boolean reported;
    Set<EnteredPlayer> previousOpponents;

    static EnteredPlayer create(Player p, int startingPosition) {
        return EnteredPlayer.builder()
                .playerId(p.getPlayerId())
                .discordId(p.getDiscordId())
                .name(p.getName())
                .discriminator(p.getDiscriminator())
                .points(0)
                .startingPosition(startingPosition)
                .receivedBye(false)
                .previousOpponents(Set.of())
                .build();
    }

    private static EnteredPlayerBuilder builder() {
        return new EnteredPlayerBuilder();
    }
}
