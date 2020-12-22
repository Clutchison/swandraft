package com.hutchison.swandraft.controller;

import com.hutchison.swandraft.model.SeedingStyle;
import com.hutchison.swandraft.model.dto.Result;
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

import java.util.Set;

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

    @PostMapping(path = "/enter")
    public ResponseEntity<String> enter(
            @RequestBody @NonNull Long discordId,
            @RequestBody(required = false) String name,
            @RequestBody(required = false) Integer discriminator
    ) {
        return ResponseEntity.ok(service.enter(discordId, name, discriminator));
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
    public ResponseEntity<String> report(@RequestBody Set<Result> results) {
        return ResponseEntity.ok(service.report(results));
    }

    @PostMapping(path = "/end")
    public ResponseEntity<String> end() {
        return ResponseEntity.ok(service.end());
    }
}
