package com.hutchison.swandraft.model.tournament.round.pairing;

import com.hutchison.swandraft.model.player.Player;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EnteredPlayer {

    Long enteredPlayerId;
    @NonNull Player player;
    int points;
    int startingPosition;
    boolean receivedBye;
    @NonNull List<Pairing> pairings;

    static EnteredPlayer create(Player p, int startingPosition) {
        return EnteredPlayer.builder()
                .player(p)
                .points(0)
                .startingPosition(startingPosition)
                .receivedBye(false)
                .pairings(List.of())
                .build();
    }

    public static EnteredPlayerBuilder builder() {
        return new EnteredPlayerBuilder();
    }

    public List<EnteredPlayer> getOpponents() {
        return pairings.stream()
                .map(Pairing::getOpponent)
                .collect(Collectors.toList());
    }
}
