package com.hutchison.swandraft.model.tournament.round;

import com.hutchison.swandraft.model.tournament.round.pairing.EnteredPlayer;
import lombok.NonNull;
import lombok.Value;

import javax.validation.constraints.Min;
import java.util.Optional;
import java.util.Set;

@Value
public class Pairing {
    @Min(1)
    int roundNumber;
    @NonNull EnteredPlayer player;
    EnteredPlayer opponent;
    boolean bye;

    public Pairing(int roundNumber,
                   @NonNull EnteredPlayer player,
                   @NonNull EnteredPlayer opponent) {
        this.roundNumber = roundNumber;
        this.player = player;
        this.opponent = opponent;
        this.bye = false;
    }

    public Pairing(int roundNumber,
                   @NonNull EnteredPlayer player) {
        this.roundNumber = roundNumber;
        this.player = player;
        this.opponent = null;
        this.bye = true;
    }

    public Set<EnteredPlayer> getPlayers() {
        return opponent == null ? Set.of(player) : Set.of(player, opponent);
    }

    public Optional<EnteredPlayer> getOpposingPlayer(EnteredPlayer enteredPlayer) throws IllegalArgumentException {
        if (!getPlayers().contains(enteredPlayer)) throw new IllegalArgumentException("Player not in pairing.");
        return Optional.ofNullable(enteredPlayer == player ? opponent : player);
    }
}
