package com.hutchison.swandraft.model.tournament.round;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Set;

@Value
@EqualsAndHashCode(callSuper = true)
public class ClosedRound extends Round {
    ClosedRound(OpenRound openRound) {
        super(openRound.getEnteredPlayers(), openRound.getPairings());
    }
}
