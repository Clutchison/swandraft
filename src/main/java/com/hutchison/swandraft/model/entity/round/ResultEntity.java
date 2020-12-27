package com.hutchison.swandraft.model.entity.round;


import com.hutchison.swandraft.model.tournament.round.result.ResultState;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Set;

@Entity(name = "results")
@Table(name = "results")
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, name = "result_id")
    Long resultId;


    @ManyToMany
    @JoinTable(
            name = "result_players",
            joinColumns = @JoinColumn(name = "result_id", referencedColumnName = "result_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id", referencedColumnName = "entered_player_id"))
    Set<EnteredPlayerEntity> enteredPlayers;

    @OneToMany
    @JoinColumn(name="result_id", referencedColumnName="result_id")
    Set<ReportEntity> reports;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_id")
    PairingEntity pairing;

    @Column(unique = false, nullable = false, name = "state")
    ResultState state;
}
