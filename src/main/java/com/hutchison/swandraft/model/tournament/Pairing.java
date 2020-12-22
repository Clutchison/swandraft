package com.hutchison.swandraft.model.tournament;

import com.hutchison.swandraft.model.Player;
import com.hutchison.swandraft.model.entity.PlayerEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class Pairing {
    public static Set<Player> buildCrossPairings(Set<PlayerEntity> playerEntities) {
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

    public static Set<Player> buildRandomPairings(Set<PlayerEntity> playerEntities) {
        List<PlayerEntity> prs = new ArrayList<>(playerEntities);
        Collections.shuffle(prs);
        return buildPairings(prs);
    }

    private static Set<Player> buildPairings(List<PlayerEntity> prs) {
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
}
