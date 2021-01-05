package com.hutchison.swandraft.model.tournament.round;

import com.hutchison.swandraft.model.tournament.round.pairing.EnteredPlayer;
import com.hutchison.swandraft.model.tournament.round.pairing.Pairing;
import com.hutchison.swandraft.model.tournament.round.result.Result;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ToString
@EqualsAndHashCode
public abstract class Round {
    protected Long roundId;
    @Min(1)
    protected int roundNumber;
    protected Map<Long, EnteredPlayer> enteredPlayers;
    protected Map<EnteredPlayer, Pairing> pairings;
    protected Map<Pairing, Result> results;

    protected Round(Long roundId,
                    int roundNumber,
                    @NonNull Map<Long, EnteredPlayer> enteredPlayers,
                    @NonNull Map<EnteredPlayer, Pairing> pairings,
                    @NonNull Map<Pairing, Result> results) {
        this.roundId = roundId;
        this.roundNumber = roundNumber;
        this.enteredPlayers = Collections.unmodifiableMap(enteredPlayers);
        this.pairings = Collections.unmodifiableMap(pairings);
        this.results = Collections.unmodifiableMap(results);
    }

    protected Round(Long roundId,
                    int roundNumber,
                    @NonNull Set<EnteredPlayer> enteredPlayers,
                    @NonNull Set<Pairing> pairings,
                    @NonNull Set<Result> results) {
        this.roundId = roundId;
        this.roundNumber = roundNumber;
        this.enteredPlayers = enteredPlayers.stream().collect(Collectors.toMap(
                ep -> ep.getPlayer().getDiscordId(),
                ep -> ep
        ));
        this.pairings = pairings.stream().collect(Collectors.toMap(
                Pairing::getPlayer,
                p -> p
        ));
        this.results = results.stream().collect(Collectors.toMap(
                Result::getPairing,
                r -> r
        ));
    }

    public static RoundBuilder builder() {
        return new RoundBuilder();
    }

    public static class RoundBuilder {
        private Long roundId;
        private Integer roundNumber;
        private Set<EnteredPlayer> enteredPlayers;
        private Set<Pairing> pairings;
        private Set<Result> results;

        public ClosedRound buildClosedRound() {
            return new ClosedRound(roundId, roundNumber, enteredPlayers, pairings, results);
        }

        public OpenRound buildOpenRound() {
            return new OpenRound(roundId, roundNumber, enteredPlayers, pairings, results);
        }

        public RoundBuilder enteredPlayers(Set<EnteredPlayer> enteredPlayers) {
            this.enteredPlayers = enteredPlayers;
            return this;
        }

        public RoundBuilder pairings(Set<Pairing> pairings) {
            this.pairings = pairings;
            return this;
        }

        public RoundBuilder results(Set<Result> results) {
            this.results = results;
            return this;
        }

        public RoundBuilder roundId(Long roundId) {
            this.roundId = roundId;
            return this;
        }

        public RoundBuilder roundNumber(Integer roundNumber) {
            this.roundNumber = roundNumber;
            return this;
        }


    }

}
