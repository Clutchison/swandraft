package com.hutchison.swandraft.model.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hutchison.swandraft.model.tournament.round.pairing.SeedingStyle;
import com.hutchison.swandraft.model.tournament.Tournament;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity(name = "tournament")
@Table(name = "tournament")
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentEntity {

    static ObjectMapper objectMapper = new ObjectMapper();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, name = "tournament_id")
    Long tournamentId;

    @Column(unique = false, nullable = false, name = "seeding_style")
    SeedingStyle seedingStyle;

    @Column(unique = false, nullable = false, name = "total_rounds")
    Integer totalRounds;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "tournament_player_record",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "player_record_id"))
    Set<PlayerEntity> playerEntities;

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
