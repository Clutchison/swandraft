package com.hutchison.swandraft.model.tournament.round;

import com.hutchison.swandraft.model.tournament.round.pairing.EnteredPlayer;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ToString
@EqualsAndHashCode
public abstract class Round {
    protected Map<Long, EnteredPlayer> enteredPlayers;
    protected Map<EnteredPlayer, EnteredPlayer> pairings;

    protected Round(@NonNull Map<Long, EnteredPlayer> enteredPlayers,
                    @NonNull Map<EnteredPlayer, EnteredPlayer> pairings) {
//        this.pairings = Pairings.pair(players);
//        this.enteredPlayers = pairings.keySet().stream()
//                .collect(Collectors.toMap(
//                        EnteredPlayer::getDiscordId,
//                        p -> p
//                ));
        this.enteredPlayers = enteredPlayers;
        this.pairings = pairings;
    }
}
