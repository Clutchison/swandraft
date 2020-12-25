package com.hutchison.swandraft.exception;

import com.hutchison.swandraft.model.tournament.round.pairing.EnteredPlayer;

public class PlayerAlreadyReportedException extends IllegalStateException {
    public PlayerAlreadyReportedException(EnteredPlayer p) {
        super(p.getName() + " has already reported.");
    }

}
