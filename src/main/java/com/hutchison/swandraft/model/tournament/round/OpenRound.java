package com.hutchison.swandraft.model.tournament.round;

import com.hutchison.swandraft.model.player.Player;
import com.hutchison.swandraft.model.tournament.round.pairing.EnteredPlayer;
import com.hutchison.swandraft.model.tournament.round.pairing.Pairing;
import com.hutchison.swandraft.model.tournament.round.pairing.Pairings;
import com.hutchison.swandraft.model.tournament.round.pairing.SeedingStyle;
import com.hutchison.swandraft.model.tournament.round.result.Report;
import com.hutchison.swandraft.model.tournament.round.result.Result;
import com.hutchison.swandraft.model.tournament.round.result.ResultState;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.springframework.data.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ToString
@EqualsAndHashCode(callSuper = true)
@Value
public class OpenRound extends Round {

    private OpenRound(Long roundId,
                      int roundNumber,
                      @NonNull Map<Long, EnteredPlayer> enteredPlayers,
                      @NonNull Map<EnteredPlayer, Pairing> pairings,
                      @NonNull Map<Pairing, Result> results) {
        super(roundId, roundNumber, enteredPlayers, pairings, results);
    }

    protected OpenRound(Long roundId,
                        int roundNumber,
                        @NonNull Set<EnteredPlayer> enteredPlayers,
                        @NonNull Set<Pairing> pairings,
                        @NonNull Set<Result> results) {
        super(roundId, roundNumber, enteredPlayers, pairings, results);
    }

    public static OpenRound createFirstRound(@NonNull List<Player> players,
                                             @NonNull SeedingStyle seedingStyle) {
        Set<Pairing> pairings = Pairings.initial(players, seedingStyle);
        Map<Long, EnteredPlayer> playerMap = pairings.stream()
                .map(Pairing::getPlayer)
                .collect(Collectors.toMap(
                        p -> p.getPlayer().getDiscordId(),
                        p -> p
                ));
        Map<EnteredPlayer, Pairing> pairingMap = pairings.stream()
                .flatMap(p -> Stream.of(Pair.of(p.getPlayer(), p), Pair.of(p.getOpponent(), p)))
                .collect(Collectors.toMap(
                        Pair::getFirst,
                        Pair::getSecond
                ));
        Map<Pairing, Result> resultsMap = pairings.stream()
                .collect(Collectors.toMap(
                        p -> p,
                        Result::create
                ));
        return new OpenRound(
                null,
                1,
                playerMap,
                pairingMap,
                resultsMap
        );
    }

    public Optional<Pair<OpenRound, ResultState>> report(@NonNull Report report) {
        Pairing pairing = pairings.get(report.getEnteredPlayer());
        Optional<Result> result = results.get(pairing).report(report);
        if (result.isPresent()) {
            if (result.get().equals(results.get(pairing))) {
                return Optional.of(Pair.of(this, result.get().getState()));
            } else {
                HashMap<Pairing, Result> newResults = new HashMap<>(results);
                newResults.put(pairing, result.get());
                return Optional.of(Pair.of(
                        new OpenRound(
                                roundId,
                                roundNumber,
                                enteredPlayers,
                                pairings,
                                newResults),
                        result.get().getState()));
            }
        } else {
            return Optional.empty();
        }
    }

    private static Map<Long, EnteredPlayer> mapToDiscordId(Set<EnteredPlayer> enteredPlayers) {
        return enteredPlayers.stream().collect(Collectors.toMap(
                ep -> ep.getPlayer().getDiscordId(),
                ep -> ep
        ));
    }
}
