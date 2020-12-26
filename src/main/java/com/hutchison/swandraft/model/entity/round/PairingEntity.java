package com.hutchison.swandraft.model.entity.round;

import com.hutchison.swandraft.model.tournament.round.pairing.EnteredPlayer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "pairing")
@Table(name = "pairing",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"tournament_id", "player_id", "round_number"})
        })
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PairingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, name = "pairing_id")
    Long pairingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    EnteredPlayer player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opponent_id")
    EnteredPlayer opponent;

    @Column(unique = false, nullable = false, name = "round_number")
    Integer roundNumber;
}
