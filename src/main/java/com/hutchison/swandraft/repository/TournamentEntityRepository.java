package com.hutchison.swandraft.repository;

import com.hutchison.swandraft.model.entity.TournamentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TournamentEntityRepository extends JpaRepository<TournamentEntity, Long> {

    @Query("Select t from tournament t where t.tournamentId = (Select max(tm.tournamentId) from tournament tm)")
    Optional<TournamentEntity> findLatestTournament();
}
