package com.hutchison.swandraft.model.tournament.round;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class ClosedRound extends Round {
    ClosedRound(OpenRound openRound) {
        super(openRound.getRoundNumber(),
                openRound.getEnteredPlayers(),
                openRound.getPairings(),
                openRound.getResults());
    }
}
