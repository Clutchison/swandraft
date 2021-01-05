package com.hutchison.swandraft.model.tournament.round.pairing;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import javax.validation.constraints.Min;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Value
@Builder
public class Pairing {
    Long pairingId;
    @Min(1) int roundNumber;
    @NonNull EnteredPlayer player;
    EnteredPlayer opponent;
    boolean bye;

    private Pairing(Long pairingId,
                   int roundNumber,
                   @NonNull EnteredPlayer player,
                   EnteredPlayer opponent,
                   boolean bye) {
        this.pairingId = pairingId;
        this.roundNumber = roundNumber;
        this.player = player;
        this.opponent = opponent;
        this.bye = bye;
    }

    private Pairing(int roundNumber,
                    @NonNull EnteredPlayer player) {
        this.pairingId = null;
        this.roundNumber = roundNumber;
        this.player = player;
        this.opponent = null;
        this.bye = true;
    }

    public static Pairing create(int roundNumber,
                                 @NonNull EnteredPlayer player,
                                 @NonNull EnteredPlayer opponent) {
        return new Pairing(null, roundNumber, player, opponent, false);
    }

    public static Pairing bye(int roundNumber,
                              @NonNull EnteredPlayer player) {
        return new Pairing(null, roundNumber, player, null, true);
    }

    public Set<EnteredPlayer> getPlayers() {
        return opponent == null ? Set.of(player) : Set.of(player, opponent);
    }

    public static Set<EnteredPlayer> getOpponents(Set<Pairing> pairings) {
        return pairings.stream()
                .map(Pairing::getOpponent)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public Optional<EnteredPlayer> getOpposingPlayer(EnteredPlayer enteredPlayer) throws IllegalArgumentException {
        if (!getPlayers().contains(enteredPlayer)) throw new IllegalArgumentException("Player not in pairing.");
        return Optional.ofNullable(enteredPlayer == player ? opponent : player);
    }
}
