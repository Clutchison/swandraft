package com.hutchison.swandraft.model.tournament;

import com.hutchison.swandraft.model.player.Player;
import com.hutchison.swandraft.model.tournament.round.result.Report;
import com.hutchison.swandraft.model.tournament.round.result.ResultState;
import com.hutchison.swandraft.model.tournament.round.Rounds;
import com.hutchison.swandraft.model.tournament.round.pairing.SeedingStyle;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.hutchison.swandraft.model.tournament.round.pairing.SeedingStyle.CROSS;

@Value
@Builder(toBuilder = true)
public class Tournament {
    // Constants
    static final SeedingStyle DEFAULT_SEEDING_STYLE = CROSS;
    static final int DEFAULT_POINTS_PER_WIN = 3;
    static final int DEFAULT_POINTS_PER_DRAW = 1;
    static final int DEFAULT_POINTS_PER_LOSS = 0;
    static final int DEFAULT_GAMES_PER_MATCH = 3;

    //    Immutable fields
    Long tournamentId;
    SeedingStyle seedingStyle;
    int totalRounds;
    int pointsPerWin;
    int pointsPerDraw;
    int pointsPerLoss;
    int gamesPerMatch;
    List<Player> players;

    //    Mutable fields
    @NonFinal
    Rounds rounds;

    // For deserialization
    private Tournament(
            Long tournamentId,
            SeedingStyle seedingStyle,
            int totalRounds,
            Integer pointsPerWin,
            Integer pointsPerDraw,
            Integer pointsPerLoss,
            int gamesPerMatch,
            List<Player> players,
            Rounds rounds
    ) {
        this.tournamentId = tournamentId;
        this.seedingStyle = seedingStyle == null ? DEFAULT_SEEDING_STYLE : seedingStyle;
        this.totalRounds = totalRounds == 0 ? calculateTotalRounds(players.size()) : totalRounds;
        this.pointsPerWin = pointsPerWin == null ? DEFAULT_POINTS_PER_WIN : pointsPerWin;
        this.pointsPerDraw = pointsPerDraw == null ? DEFAULT_POINTS_PER_DRAW : pointsPerDraw;
        this.pointsPerLoss = pointsPerLoss == null ? DEFAULT_POINTS_PER_LOSS : pointsPerLoss;
        this.gamesPerMatch = gamesPerMatch == 0 ? DEFAULT_GAMES_PER_MATCH : gamesPerMatch;
        this.players = Collections.unmodifiableList(players);
        this.rounds = rounds == null ? new Rounds(this.players, this.seedingStyle) : rounds;
        validate();
    }

    private void validate() {
        List<String> errors = new ArrayList<>();
        if (totalRounds <= 0) errors.add("Total rounds must be a positive number.");
        if (players == null || players.size() < 6) errors.add("Not enough players.");
        if (rounds == null) errors.add("Tournament rounds have not been initialized.");
        if (errors.size() > 0) throw new IllegalArgumentException("Error creating tournament: \n- " +
                String.join("\n- ", errors));
    }

    public Optional<Pair<Tournament, ResultState>> report(Report report) {
        Optional<Pair<Rounds, ResultState>> newRounds = rounds.report(report);
        return newRounds.map(nr -> Pair.of(
                newRounds.get().getFirst().equals(rounds) ? this :
                        this.toBuilder().rounds(nr.getFirst()).build(),
                nr.getSecond()));
    }

    public String advance() {
//        TournamentSnapshot snapshot = getLatestSnapshot().advance();
//        snapshots.add(snapshot);
//        return snapshot.getMessage();
        throw new NotYetImplementedException();
    }

    private static int calculateTotalRounds(int playerCount) {
        if (playerCount <= 0) throw new IllegalArgumentException("Cannot calculate total rounds: player count <= 0.");
        return (int) Math.ceil(Math.log(playerCount) / Math.log(2));
    }

//    public static class TournamentBuilder {
//        public Tournament build() {
//            return new Tournament(
//                    tournamentId,
//                    seedingStyle,
//                    totalRounds,
//                    pointsPerWin,
//                    pointsPerDraw,
//                    pointsPerLoss,
//                    gamesPerMatch,
//                    players,
//                    Rounds.builder()
//                            .players(players)
//                            .seedingStyle(seedingStyle)
//                            .pointsPerWin(pointsPerWin)
//                            .pointsPerDraw(pointsPerDraw)
//                            .pointsPerLoss(pointsPerLoss)
//                            .build()
//            );
//        }
//
//        private Tournament rounds(Rounds rounds) {
//            throw new AssertionError();
//        }
//    }
}
