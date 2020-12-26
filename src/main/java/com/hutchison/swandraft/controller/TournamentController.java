package com.hutchison.swandraft.controller;

import com.hutchison.swandraft.model.dto.ReportDto;
import com.hutchison.swandraft.model.dto.enter.EnterRequest;
import com.hutchison.swandraft.model.dto.enter.EnterResponse;
import com.hutchison.swandraft.model.player.PlayerIdentifier;
import com.hutchison.swandraft.model.tournament.round.pairing.SeedingStyle;
import com.hutchison.swandraft.service.ReportResponse;
import com.hutchison.swandraft.service.TournamentService;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hutchison.swandraft.model.log.SwanLogFactory.debug;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/t")
public class TournamentController {

    TournamentService service;

    @Autowired
    public TournamentController(TournamentService service) {
        this.service = service;
    }

    @PostMapping(path = "/start")
    public ResponseEntity<String> start(@RequestBody Boolean forceRestart) {
        return ResponseEntity.ok(service.start(forceRestart));
    }

    @PostMapping(path = "/enter", consumes = "application/json")
    public ResponseEntity<EnterResponse> enter(
            @RequestBody @NonNull EnterRequest r
    ) {
        EnterResponse response = service.enter(new PlayerIdentifier(r.getDiscordId(), r.getName(), r.getDiscriminator()));
        debug(response.getMessage());
        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/configure")
    public ResponseEntity<String> configure(@RequestBody String seedingStyle,
                                            @RequestBody Integer totalRounds) {
        return ResponseEntity.ok(service.configure(SeedingStyle.fromString(seedingStyle), totalRounds));
    }

    @PostMapping(path = "/recover")
    public ResponseEntity<String> recover() {
        return ResponseEntity.ok(service.recover());
    }

    @PostMapping(path = "/advance")
    public ResponseEntity<String> advance() {
        return ResponseEntity.ok(service.advance());
    }

    @PostMapping(path = "/report")
    public ResponseEntity<ReportResponse> report(@RequestBody ReportDto reportDto) {
        return ResponseEntity.ok(service.report(reportDto));
    }

    @PostMapping(path = "/end")
    public ResponseEntity<String> end() {
        return ResponseEntity.ok(service.end());
    }
}
