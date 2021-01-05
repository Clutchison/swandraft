package com.hutchison.swandraft.model.entity;

import com.hutchison.swandraft.model.entity.round.RoundsEntity;
import com.hutchison.swandraft.model.tournament.Tournament;
import com.hutchison.swandraft.model.tournament.round.pairing.SeedingStyle;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "tournament")
@Table(name = "tournament")
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, name = "tournament_id")
    Long tournamentId;

    @Column(unique = false, nullable = false, name = "seeding_style")
    SeedingStyle seedingStyle;

    @Column(unique = false, nullable = false, name = "total_rounds")
    Integer totalRounds;

    @Column(unique = false, nullable = false, name = "points_per_win")
    Integer pointsPerWin;

    @Column(unique = false, nullable = false, name = "points_per_draw")
    Integer pointsPerDraw;

    @Column(unique = false, nullable = false, name = "points_per_loss")
    Integer pointsPerLoss;

    @Column(unique = false, nullable = false, name = "games_per_match")
    Integer gamesPerMatch;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "tournament_player",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id"))
    Set<PlayerEntity> playerEntities;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rounds_id")
    RoundsEntity rounds;

    public static TournamentEntity fromTournament(Tournament first) {
        return null;
    }

    public Tournament toTournament() {
        return Tournament.builder()
                .tournamentId(tournamentId)
                .seedingStyle(seedingStyle)
                .totalRounds(totalRounds)
                .pointsPerWin(pointsPerWin)
                .pointsPerDraw(pointsPerDraw)
                .pointsPerLoss(pointsPerLoss)
                .gamesPerMatch(gamesPerMatch)
                .players(playerEntities.stream().map(PlayerEntity::toPlayer).collect(Collectors.toList()))
                .rounds(rounds.toRounds())
                .build();
    }

//    public Tournament toTournament() {
//        return Tournament.builder()
//                .seedingStyle(seedingStyle)
//                .totalRounds(totalRounds)
//                .players(playerEntities)
//                .snapshots(snapshots)
//                .build();
//    }
//
//    public static TournamentEntity fromTournament(Tournament tournament) {
//        return TournamentEntity.builder()
//                .seedingStyle(tournament.getSeedingStyle())
//                .totalRounds(tournament.getTotalRounds())
//                .playerEntities(tournament.getPlayers())
//                .snapshots(tournament.getSnapshots())
//                .build();
//    }
}
