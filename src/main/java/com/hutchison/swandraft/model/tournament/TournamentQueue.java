package com.hutchison.swandraft.model.tournament;

import com.hutchison.swandraft.exception.PlayerAlreadyInQueueException;
import com.hutchison.swandraft.model.player.Player;
import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Value
@Builder(toBuilder = true)
public class TournamentQueue {
    Long tournamentQueueId;
    Map<Long, Player> players;

    public TournamentQueue() {
        this.tournamentQueueId = null;
        this.players = Collections.emptyMap();
    }

    public TournamentQueue(Long tournamentQueueId, Set<Player> players) {
        this.tournamentQueueId = tournamentQueueId;
        this.players = Collections.unmodifiableMap(players.stream()
                .collect(Collectors.toMap(
                        Player::getDiscordId,
                        pr -> pr
                )));
//        message = "Initialized from existing records.";
    }

    private TournamentQueue(Long tournamentQueueId, Map<Long, Player> players) {
        this.tournamentQueueId = tournamentQueueId;
        this.players = players;
    }

    public TournamentQueue add(Player player) throws PlayerAlreadyInQueueException {
        if (players.get(player.getDiscordId()) != null) throw new PlayerAlreadyInQueueException(player);
        return this.toBuilder()
                .players(
                        Collections.unmodifiableMap(Stream.concat(
                                Map.copyOf(this.players).values().stream(),
                                Stream.of(player))
                                .collect(Collectors.toMap(
                                        Player::getDiscordId,
                                        pr -> pr
                                ))))
                .build();
    }

    public Set<Player> getPlayers() {
        return Set.copyOf(this.players.values());
    }
}
