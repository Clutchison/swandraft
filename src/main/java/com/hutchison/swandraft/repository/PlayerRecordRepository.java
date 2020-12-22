package com.hutchison.swandraft.repository;

import com.hutchison.swandraft.model.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRecordRepository extends JpaRepository<PlayerEntity, Long> {
    Optional<PlayerEntity> findByDiscordId(Long discordId);
}
