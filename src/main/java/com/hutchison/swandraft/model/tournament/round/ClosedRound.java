package com.hutchison.swandraft.model.tournament.round;

import com.hutchison.swandraft.model.tournament.round.pairing.EnteredPlayer;
import com.hutchison.swandraft.model.tournament.round.pairing.Pairing;
import com.hutchison.swandraft.model.tournament.round.result.Result;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

import java.util.Set;

@Value
@EqualsAndHashCode(callSuper = true)
public class ClosedRound extends Round {
    ClosedRound(OpenRound openRound) {
        super(openRound.getRoundId(),
                openRound.getRoundNumber(),
                openRound.getEnteredPlayers(),
                openRound.getPairings(),
                openRound.getResults());
    }

    protected ClosedRound(Long roundId,
                        int roundNumber,
                        @NonNull Set<EnteredPlayer> enteredPlayers,
                        @NonNull Set<Pairing> pairings,
                        @NonNull Set<Result> results) {
        super(roundId, roundNumber, enteredPlayers, pairings, results);
    }


}
