package com.hutchison.swandraft.model;

import com.hutchison.swandraft.model.dto.Result;
import com.hutchison.swandraft.model.entity.PlayerRecord;
import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.hutchison.swandraft.model.SeedingStyle.CROSS;

@Value
@Builder(toBuilder = true)
public class Tournament {
    UUID tournamentUuid;
    SeedingStyle seedingStyle;
    int totalRounds;
    Set<PlayerRecord> playerRecords;
    List<TournamentSnapshot> snapshots;

    public Tournament(TournamentSnapshot initialSnapshot,
                      SeedingStyle seedingStyle,
                      int totalRounds,
                      Set<PlayerRecord> playerRecords) {
        this.tournamentUuid = UUID.randomUUID();
        this.snapshots = new ArrayList<>();
        snapshots.add(initialSnapshot);
        this.seedingStyle = seedingStyle;
        this.totalRounds = totalRounds;
        this.playerRecords = Collections.unmodifiableSet(playerRecords);
    }

    public String report(Set<Result> results) {
        TournamentSnapshot snapshot = snapshots.get(snapshots.size() - 1).report(results);
        snapshots.add(snapshot);
        return snapshot.getUpdateMessage();
    }

    public static class TournamentBuilder {

        private static final SeedingStyle DEFAULT_SEEDING_STYLE = CROSS;

        public Tournament build() {
            if (playerRecords == null || playerRecords.size() < 6)
                throw new RuntimeException("Error creating tournament, not enough players given.");
            return new Tournament(
                    buildInitialSnapshot(),
                    seedingStyle == null ? DEFAULT_SEEDING_STYLE : seedingStyle,
                    totalRounds == 0 ? calculateTotalRounds(playerRecords.size()) : totalRounds,
                    playerRecords
            );
        }

        private TournamentSnapshot buildInitialSnapshot() {
            Set<Player> players;
            switch (seedingStyle == null ? DEFAULT_SEEDING_STYLE : seedingStyle) {
                case CROSS:
                    players = buildCrossPairings();
                    break;
                case RANDOM:
                    players = buildRandomPairings();
                    break;
                case ADJACENT:
                case SLAUGHTER:
                default:
                    throw new RuntimeException("Unsupported Seeding Style: " + seedingStyle);
            }
            return TournamentSnapshot.builder()
                    .currentRound(0)
                    .players(players.stream().collect(Collectors.toMap(Player::getDiscordId, p -> p)))
                    .updateMessage("Initialized tournament.")
                    .build();
        }

        private Set<Player> buildCrossPairings() {
            List<PlayerRecord> tempRecords = new ArrayList<>(playerRecords);
            List<PlayerRecord> prs = new ArrayList<>();
            IntStream.range(0, tempRecords.size() / 2)
                    .forEach(i -> {
                        prs.add(tempRecords.get(i));
                        prs.add(tempRecords.get(i + tempRecords.size() / 2));
                    });
            if (prs.size() % 2 == 1) prs.add(tempRecords.get(tempRecords.size() - 1));
            return buildPairings(prs);
        }

        private Set<Player> buildRandomPairings() {
            List<PlayerRecord> prs = new ArrayList<>(playerRecords);
            Collections.shuffle(prs);
            return buildPairings(prs);
        }

        private Set<Player> buildPairings(List<PlayerRecord> prs) {
            Set<Player> players = new HashSet<>();
            IntStream.range(0, prs.size())
                    .filter(i -> i % 2 == 0)
                    .forEach(i -> {
                        Player p1 = Player.initialize(prs.get(i), i);
                        Player p2 = Player.initialize(prs.get(i + 1), i + 1);
                        players.add(p1.toBuilder().currentOpponent(p2).build());
                        players.add(p2.toBuilder().currentOpponent(p1).build());
                    });

            // Give bye to remaining player.
            if (prs.size() % 2 == 1) players.add(
                    Player.initialize(prs.get(prs.size() - 1), prs.size() - 1).toBuilder()
                            .plusPoints(3)
                            .reportedThisRound(true)
                            .receivedBye(true)
                            .build()
            );

            return players;
        }

        private int calculateTotalRounds(int playerCount) {
            return (int) Math.ceil(Math.log(playerCount) / Math.log(2));
        }
    }
}
