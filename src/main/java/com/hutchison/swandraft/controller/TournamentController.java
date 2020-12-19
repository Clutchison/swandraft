package com.hutchison.swandraft.controller;

import com.hutchison.swandraft.service.TournamentService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/t")
public class TournamentController {

    TournamentService service;

    @Autowired
    public TournamentController(TournamentService service) {
        this.service = service;
    }

    public ResponseEntity<String> init() {
        return ResponseEntity.ok(service.init());
    }
}
