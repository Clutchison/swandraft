package com.hutchison.swandraft.model.tournament.round.pairing;

import com.hutchison.swandraft.model.player.Player;
import com.hutchison.swandraft.model.tournament.round.Pairing;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(toBuilder = true)
public class EnteredPlayer implements Serializable {

    @NonNull Player player;
    int points;
    int startingPosition;
    boolean receivedBye;
    //    boolean reported;
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

    private static EnteredPlayerBuilder builder() {
        return new EnteredPlayerBuilder();
    }

    public List<EnteredPlayer> getOpponents() {
        return pairings.stream()
                .map(Pairing::getOpponent)
                .collect(Collectors.toList());
    }
}
