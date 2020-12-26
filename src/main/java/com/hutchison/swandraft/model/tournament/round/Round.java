package com.hutchison.swandraft.model.tournament.round;

import com.hutchison.swandraft.model.tournament.round.pairing.EnteredPlayer;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.Map;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ToString
@EqualsAndHashCode
public abstract class Round {
    @Min(1)
    protected int roundNumber;
    protected Map<Long, EnteredPlayer> enteredPlayers;
    protected Map<EnteredPlayer, Pairing> pairings;
    protected Map<Pairing, Result> results;

    protected Round(int roundNumber,
                    @NonNull Map<Long, EnteredPlayer> enteredPlayers,
                    @NonNull Map<EnteredPlayer, Pairing> pairings,
                    @NonNull Map<Pairing, Result> results) {
        this.roundNumber = roundNumber;
        this.enteredPlayers = Collections.unmodifiableMap(enteredPlayers);
        this.pairings = Collections.unmodifiableMap(pairings);
        this.results = Collections.unmodifiableMap(results);
    }
}
