package com.hutchison.swandraft.model.tournament.round;

import com.hutchison.swandraft.model.player.Player;
import com.hutchison.swandraft.model.tournament.round.pairing.SeedingStyle;
import com.hutchison.swandraft.model.tournament.round.result.Report;
import com.hutchison.swandraft.model.tournament.round.result.ResultState;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Value
@Builder(toBuilder = true)
public class Rounds {
    Long roundsId;
    List<ClosedRound> closedRounds;
    OpenRound openRound;

    private Rounds(Long roundsId, List<ClosedRound> closedRounds, @NonNull OpenRound openRound) {
        this.roundsId = roundsId;
        this.closedRounds = closedRounds == null ? new ArrayList<>() : closedRounds;
        this.openRound = openRound;
    }

    public Rounds(List<Player> players, SeedingStyle seedingStyle) {
        this.roundsId = null;
        this.closedRounds = new ArrayList<>();
        openRound = OpenRound.createFirstRound(players, seedingStyle);
    }

    public Optional<Pair<Rounds, ResultState>> report(@NonNull Report report) {
        Optional<Pair<OpenRound, ResultState>> newRound = openRound.report(report);
        return newRound.map(nr -> Pair.of(
                newRound.get().getFirst().equals(openRound) ? this :
                        this.toBuilder()
                                .closedRounds(closedRounds)
                                .openRound(nr.getFirst())
                                .build(),
                nr.getSecond()));
    }
}
