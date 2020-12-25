package com.hutchison.swandraft.model.tournament.round;

import com.hutchison.swandraft.model.player.Player;
import com.hutchison.swandraft.model.tournament.Tournament;
import com.hutchison.swandraft.model.tournament.round.pairing.SeedingStyle;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.aspectj.apache.bcel.classfile.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Value
@Builder
public class Rounds {
    List<ClosedRound> closedRounds;
    OpenRound openRound;
    int pointsPerWin;
    int pointsPerDraw;
    int pointsPerLoss;

    private Rounds(Set<Player> players,
                  SeedingStyle seedingStyle,
                  int pointsPerWin,
                  int pointsPerDraw,
                  int pointsPerLoss) {
        this.closedRounds = new ArrayList<>();
        openRound = OpenRound.createFirstRound(players, seedingStyle);
        this.pointsPerWin = pointsPerWin;
        this.pointsPerDraw = pointsPerDraw;
        this.pointsPerLoss = pointsPerLoss;
    }

    public static class RoundsBuilder {

        private Set<Player> players;
        private SeedingStyle seedingStyle;

        public Rounds build() {
            return new Rounds(
                    players,
                    seedingStyle,
                    pointsPerWin,
                    pointsPerDraw,
                    pointsPerLoss
            );
        }

        public RoundsBuilder players(Set<Player> players) {
            this.players = players;
            return this;
        }

        public RoundsBuilder seedingStyle(SeedingStyle seedingStyle) {
            this.seedingStyle = seedingStyle;
            return this;
        }

        private RoundsBuilder closedRounds(List<ClosedRound> closedRounds) {
            throw new AssertionError();
        }

        private RoundsBuilder openRound(OpenRound openRound) {
            throw new AssertionError();
        }
    }
}
