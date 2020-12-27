package com.hutchison.swandraft.model.tournament.round.result;

import com.hutchison.swandraft.model.tournament.round.pairing.EnteredPlayer;
import com.hutchison.swandraft.model.tournament.round.pairing.Pairing;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hutchison.swandraft.model.tournament.round.result.ResultState.*;

@Value
public class Result {
    Map<EnteredPlayer, Optional<Report>> reports;
    Pairing pairing;
    ResultState state;

    public static Result create(Pairing pairing) {
        if (pairing.isBye()) {
            return new Result(
                    Map.of(pairing.getPlayer(), Optional.of(new Report(pairing.getPlayer(), 2, 0, 0))),
                    pairing,
                    BYE);
        } else {
            return new Result(
                    createReportMap(pairing),
                    pairing,
                    UNCONFIRMED);
        }
    }


    /*
    Possibles
    - Player has not reported and opponent has not reported
        - Record result and return
    - Player has not reported and opponent has reported and reports match
        - Record result, mark confirmed, return
    - Player has not reported and opponent has reported and reports differ
        - Delete opponent result, return
    - Player has already reported and opponent has not reported and previously reported something different
    - Player has already reported and opponent has reported and reports match
        - Return this
    - Player has already reported and opponent has reported and reports differ
        - Return empty
     */
    public Optional<Result> report(Report report) {
        if (state.equals(ResultState.BYE)) return Optional.empty();

        Optional<Report> previousReport = reports.get(report.getEnteredPlayer());
        Optional<Report> opponentReport = getOpponentReport(report.getEnteredPlayer());

        return previousReport.<Optional<Result>>map(prevRep -> prevRep.equals(report) ? Optional.of(this) : Optional.empty())
                .orElseGet(() -> opponentReport.map(oppRep -> oppRep.matchesOpponent(report) ?
                        Optional.of(record(report, CONFIRMED)) :
                        Optional.of(reverse())
                ).orElseGet(() -> Optional.of(record(report, ResultState.UNCONFIRMED))));
    }

    private Result record(Report report, ResultState newState) {
        HashMap<EnteredPlayer, Optional<Report>> newMap = new HashMap<>(reports);
        newMap.put(report.getEnteredPlayer(), Optional.of(report));
        return new Result(newMap, pairing, newState);
    }

    private Result reverse() {
        return new Result(createReportMap(pairing), pairing, REVERSED);
    }

    private Optional<Report> getOpponentReport(EnteredPlayer enteredPlayer) {
        EnteredPlayer opponent = pairing.getOpposingPlayer(enteredPlayer).orElseThrow(() ->
                new IllegalArgumentException(
                        enteredPlayer.getPlayer().getIdentifier() + " does not have matching opponent in result."));
        return reports.get(opponent);
    }

    private static Map<EnteredPlayer, Optional<Report>> createReportMap(Pairing pairing) {
        return pairing.getPlayers().stream().collect(Collectors.toMap(
                ep -> ep,
                ep -> Optional.empty()
        ));
    }
}
