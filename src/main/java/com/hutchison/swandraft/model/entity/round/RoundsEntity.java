package com.hutchison.swandraft.model.entity.round;

import com.hutchison.swandraft.model.entity.round.ClosedRoundEntity;
import com.hutchison.swandraft.model.entity.round.OpenRoundEntity;
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
import java.util.Set;

@Entity(name = "rounds")
@Table(name = "rounds")
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoundsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, name = "rounds_id")
    Long tournamentId;

    @OneToMany
    @JoinColumn(name = "rounds_id", referencedColumnName = "rounds_id")
    Set<ClosedRoundEntity> closedRounds;

    @Column(unique = false, nullable = false, name = "open_round")
    OpenRoundEntity openRound;

    @Column(unique = false, nullable = false, name = "points_per_win")
    Integer pointsPerWin;

    @Column(unique = false, nullable = false, name = "points_per_draw")
    Integer pointsPerDraw;

    @Column(unique = false, nullable = false, name = "points_per_loss")
    Integer pointsPerLoss;
}
