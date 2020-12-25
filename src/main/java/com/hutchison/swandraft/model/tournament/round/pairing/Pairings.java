package com.hutchison.swandraft.model.tournament.round.pairing;

import com.hutchison.swandraft.model.player.Player;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class Pairings {

    private Pairings() {
        throw new AssertionError();
    }

    public static Map<EnteredPlayer, EnteredPlayer> initial(@NonNull Set<Player> players,
                                                            @NonNull SeedingStyle seedingStyle) {
        switch (seedingStyle) {
            case CROSS:
                return cross(players);
            case RANDOM:
                return random(players);
            case ADJACENT:
            case SLAUGHTER:
            default:
                throw new UnsupportedOperationException("Unsupported Seeding Style: " + seedingStyle);
        }
    }

    public static Map<EnteredPlayer, EnteredPlayer> swiss(Set<EnteredPlayer> players) {
        return null;
    }

    private static Map<EnteredPlayer, EnteredPlayer> cross(Set<Player> players) {
        List<Player> tempRecords = new ArrayList<>(players);
        List<Player> prs = new ArrayList<>();
        IntStream.range(0, tempRecords.size() / 2)
                .forEach(i -> {
                    prs.add(tempRecords.get(i));
                    prs.add(tempRecords.get(i + tempRecords.size() / 2));
                });
        if (prs.size() % 2 == 1) prs.add(tempRecords.get(tempRecords.size() - 1));
        return buildInitialPairings(prs);
    }

    private static Map<EnteredPlayer, EnteredPlayer> random(Set<Player> enteredPlayers) {
        List<Player> prs = new ArrayList<>(enteredPlayers);
        Collections.shuffle(prs);
        return buildInitialPairings(prs);
    }

    private static Map<EnteredPlayer, EnteredPlayer> buildInitialPairings(List<Player> prs) {
        Map<EnteredPlayer, EnteredPlayer> enteredPlayers = new HashMap<>();
        IntStream.range(0, prs.size())
                .filter(i -> i % 2 == 0)
                .forEach(i -> {
                    EnteredPlayer p1 = EnteredPlayer.create(prs.get(i), i);
                    EnteredPlayer p2 = EnteredPlayer.create(prs.get(i + 1), i + 1);
                    enteredPlayers.put(p1, p2);
                    enteredPlayers.put(p2, p1);
                });

        // Give bye to remaining player.
        if (prs.size() % 2 == 1) enteredPlayers.put(
                EnteredPlayer.create(prs.get(prs.size() - 1), prs.size() - 1),
                EnteredPlayer.NO_OPPONENT);

        return enteredPlayers;
    }
}
