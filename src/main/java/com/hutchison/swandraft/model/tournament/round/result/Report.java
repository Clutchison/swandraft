package com.hutchison.swandraft.model.tournament.round.result;

import com.hutchison.swandraft.model.dto.report.ReportDto;
import com.hutchison.swandraft.model.tournament.round.pairing.EnteredPlayer;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Value
@AllArgsConstructor
public class Report {
    @NonNull
    EnteredPlayer enteredPlayer;
    @Min(0) @Max(2)
    int roundsWon;
    @Min(0) @Max(2)
    int roundsLost;
    @Min(0) @Max(3)
    int roundsDrawn;

    public Report(@NonNull EnteredPlayer enteredPlayer, @NonNull ReportDto dto) {
        this.enteredPlayer = enteredPlayer;
        this.roundsWon = dto.getRoundsWon();
        this.roundsLost = dto.getRoundsLost();
        this.roundsDrawn = dto.getRoundsDrawn();
    }

    public boolean matchesOpponent(Report report) {
        return roundsWon == report.roundsLost &&
                roundsLost == report.roundsLost &&
                roundsDrawn == report.roundsDrawn;
    }
}
