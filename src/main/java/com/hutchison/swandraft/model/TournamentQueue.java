package com.hutchison.swandraft.model;

import lombok.Data;

import java.util.Set;

@Data
public class TournamentQueue {
    Set<PlayerRecord> playerRecords;

    public void add(PlayerRecord playerRecord) {

    }
}
