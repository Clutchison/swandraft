package com.hutchison.swandraft.model;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder(toBuilder = true)
public class TournamentSnapshot {
    Map<String, Player> players;
    int currentRound;
    String updateMessage;
}
