package com.hutchison.swandraft.model.tournament.round;

import com.hutchison.swandraft.exception.PlayerAlreadyReportedException;
import com.hutchison.swandraft.model.player.Player;
import com.hutchison.swandraft.model.dto.Result;
import com.hutchison.swandraft.model.tournament.round.pairing.EnteredPlayer;
import com.hutchison.swandraft.model.tournament.round.pairing.Pairings;
import com.hutchison.swandraft.model.tournament.round.pairing.SeedingStyle;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ToString
@EqualsAndHashCode(callSuper = true)
public class OpenRound extends Round {

    Map<EnteredPlayer, Result> results;

    private OpenRound(Map<Long, EnteredPlayer> enteredPlayers,
                      Map<EnteredPlayer, EnteredPlayer> pairings,
                      Map<EnteredPlayer, Result> results) {
        super(enteredPlayers, pairings);
        this.results = results;
    }

    public static OpenRound createFirstRound(@NonNull Set<Player> players,
                                             @NonNull SeedingStyle seedingStyle) {
        Map<EnteredPlayer, EnteredPlayer> pairings = Pairings.initial(players, seedingStyle);
        return new OpenRound(
                mapToDiscordId(pairings.keySet()),
                pairings,
                new HashMap<>()
        );
    }

    public void report(@NonNull Result result) {
        EnteredPlayer player = enteredPlayers.get(result.getDiscordId());
        Result previousResult = results.get(player);
        if (previousResult == null) {
            EnteredPlayer opponent = pairings.get(player);
            Result opponentResult = results.get(opponent);
            if (opponentResult == null)
                results.put(player, result);
        } else if (previousResult.equals(result)) {
            throw new PlayerAlreadyReportedException(player);
        }
    }

    private static Map<Long, EnteredPlayer> mapToDiscordId(Set<EnteredPlayer> enteredPlayers) {
        return enteredPlayers.stream().collect(Collectors.toMap(
                EnteredPlayer::getDiscordId,
                ep -> ep
        ));
    }
}
