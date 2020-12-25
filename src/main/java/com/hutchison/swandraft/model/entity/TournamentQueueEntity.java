package com.hutchison.swandraft.model.entity;

import com.hutchison.swandraft.model.tournament.TournamentQueue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "tournament_queue")
@Table(name = "tournament_queue")
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class TournamentQueueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, name = "tournament_queue_id")
    Long tournamentQueueId;

    @OneToMany
    @JoinColumn(name = "tournament_queue_id", referencedColumnName = "tournament_queue_id")
    Set<PlayerEntity> playerEntities;

    public TournamentQueueEntity() {
        playerEntities = new HashSet<>();
    }

    public TournamentQueueEntity(Long tournamentQueueId, Set<PlayerEntity> playerEntities) {
        this.tournamentQueueId = tournamentQueueId;
        this.playerEntities = playerEntities == null ? new HashSet<>() : playerEntities;
    }


    public TournamentQueue toTournamentQueue() {
        return new TournamentQueue(
                tournamentQueueId,
                playerEntities.stream()
                        .map(PlayerEntity::toPlayer)
                        .collect(Collectors.toSet()));
    }

    public static TournamentQueueEntity fromTournamentQueue(TournamentQueue t) {
        return new TournamentQueueEntity(
                t.getTournamentQueueId(),
                t.getPlayers().stream()
                        .map(PlayerEntity::fromPlayer)
                        .collect(Collectors.toSet())
        );
    }
}
