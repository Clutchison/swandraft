package com.hutchison.swandraft.model.tournament;

import com.hutchison.swandraft.model.Player;
import com.hutchison.swandraft.model.SeedingStyle;
import com.hutchison.swandraft.model.dto.Result;
import com.hutchison.swandraft.model.entity.PlayerEntity;
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
    Set<PlayerEntity> playerEntities;
    List<TournamentSnapshot> snapshots;


    // For deserialization
    Tournament(
            UUID tournamentUuid,
            SeedingStyle seedingStyle,
            int totalRounds,
            Set<PlayerEntity> playerEntities,
            List<TournamentSnapshot> snapshots
    ) {
        this.tournamentUuid = tournamentUuid;
        this.seedingStyle = seedingStyle;
        this.totalRounds = totalRounds;
        this.playerEntities = Collections.unmodifiableSet(playerEntities);
        this.snapshots = snapshots;
    }

    // Brand new tournament
    Tournament(
            SeedingStyle seedingStyle,
            int totalRounds,
            TournamentSnapshot initialSnapshot,
            Set<PlayerEntity> playerEntities) {
        this.tournamentUuid = UUID.randomUUID();
        this.snapshots = new ArrayList<>();
        snapshots.add(initialSnapshot);
        this.seedingStyle = seedingStyle;
        this.totalRounds = totalRounds;
        this.playerEntities = Collections.unmodifiableSet(playerEntities);
    }

    public String report(Set<Result> results) {
        TournamentSnapshot snapshot = getLatestSnapshot().report(results);
        snapshots.add(snapshot);
        return snapshot.getMessage();
    }

    public String getMessage() {
        return getLatestSnapshot().getMessage();
    }

    public String advance() {
        TournamentSnapshot snapshot = getLatestSnapshot().advance();
        snapshots.add(snapshot);
        return snapshot.getMessage();
    }

    private TournamentSnapshot getLatestSnapshot() {
        return snapshots.get(snapshots.size() - 1);
    }

    public static class TournamentBuilder {

        static final SeedingStyle DEFAULT_SEEDING_STYLE = CROSS;

        public Tournament build() {
            if (playerEntities == null || playerEntities.size() < 6)
                throw new RuntimeException("Error creating tournament, not enough players given.");
            if (snapshots == null || snapshots.size() == 0) {
                return new Tournament(
                        seedingStyle == null ? DEFAULT_SEEDING_STYLE : seedingStyle,
                        totalRounds == 0 ? calculateTotalRounds(playerEntities.size()) : totalRounds,
                        buildInitialSnapshot(),
                        playerEntities
                );
            } else {
                return new Tournament(
                        tournamentUuid,
                        seedingStyle,
                        totalRounds,
                        playerEntities,
                        snapshots
                );
            }
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
                    .message("Initialized tournament.")
                    .build();
        }

        private Set<Player> buildCrossPairings() {
            List<PlayerEntity> tempRecords = new ArrayList<>(playerEntities);
            List<PlayerEntity> prs = new ArrayList<>();
            IntStream.range(0, tempRecords.size() / 2)
                    .forEach(i -> {
                        prs.add(tempRecords.get(i));
                        prs.add(tempRecords.get(i + tempRecords.size() / 2));
                    });
            if (prs.size() % 2 == 1) prs.add(tempRecords.get(tempRecords.size() - 1));
            return buildPairings(prs);
        }

        private Set<Player> buildRandomPairings() {
            List<PlayerEntity> prs = new ArrayList<>(playerEntities);
            Collections.shuffle(prs);
            return buildPairings(prs);
        }

        private Set<Player> buildPairings(List<PlayerEntity> prs) {
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
