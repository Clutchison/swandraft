package com.hutchison.swandraft.controller;

import com.hutchison.swandraft.model.tournament.Tournament;
import com.hutchison.swandraft.service.TestService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/test")
public class TestController {

    TestService service;

    public TestController(TestService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello!");
    }

    @GetMapping(path = "/tournament")
    public ResponseEntity<Tournament> testTournament() {
        return ResponseEntity.ok(service.testTournament());
    }

    @GetMapping(path = "/tournament/report")
    public ResponseEntity<Tournament> testReport() {
        return ResponseEntity.ok(service.testReport());
    }
}
