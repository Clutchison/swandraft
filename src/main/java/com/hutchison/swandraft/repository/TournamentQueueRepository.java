package com.hutchison.swandraft.repository;

import com.hutchison.swandraft.model.entity.TournamentQueueEntity;
import com.hutchison.swandraft.model.tournament.TournamentQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TournamentQueueRepository extends JpaRepository<TournamentQueueEntity, Long> {


    @Query("Select q from tournament_queue q where q.tournamentQueueId = (Select max(tm.tournamentQueueId) from tournament_queue tm)")
    Optional<TournamentQueueEntity> findLatestTournamentQueueEntity();

}
