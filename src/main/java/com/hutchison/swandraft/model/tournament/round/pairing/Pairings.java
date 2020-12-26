package com.hutchison.swandraft.model.tournament.round.pairing;

import com.hutchison.swandraft.model.player.Player;
import com.hutchison.swandraft.model.tournament.Tournament;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Pairings {

    private Pairings() {
        throw new AssertionError();
    }

    public static Set<Pairing> initial(@NonNull List<Player> players,
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

    public static Set<Pairing> swiss(Tournament tournament) {
        return null;
    }

    private static Set<Pairing> cross(List<Player> players) {
        List<Player> prs = new ArrayList<>();
        IntStream.range(0, players.size() / 2)
                .forEach(i -> {
                    prs.add(players.get(i));
                    prs.add(players.get(i + players.size() / 2));
                });
        if (prs.size() % 2 == 1) prs.add(players.get(players.size() - 1));
        return buildInitialPairings(playersToEnteredPlayers(prs));
    }

    private static Set<Pairing> random(List<Player> players) {
        List<Player> shuffled = new ArrayList<>(players);
        Collections.shuffle(shuffled);
        return buildInitialPairings(playersToEnteredPlayers(shuffled));
    }

    private static List<EnteredPlayer> playersToEnteredPlayers(@NonNull List<Player> prs) {
        return IntStream.range(0, prs.size())
                .mapToObj(i -> EnteredPlayer.create(prs.get(i), i))
                .collect(Collectors.toList());
    }

    private static Set<Pairing> buildInitialPairings(List<EnteredPlayer> enteredPlayers) {
        Set<Pairing> pairings = IntStream.range(0, enteredPlayers.size())
                .filter(i -> i % 2 == 0)
                .mapToObj(i -> new Pairing(1, enteredPlayers.get(i), enteredPlayers.get(i + 1)))
                .collect(Collectors.toSet());

        // Give bye to remaining player.
        if (enteredPlayers.size() % 2 == 1)
            pairings.add(new Pairing(1, enteredPlayers.get(enteredPlayers.size() - 1)));

        return pairings;
    }
}
