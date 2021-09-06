package com.challenge.deviget.mines.controller;

import com.challenge.deviget.mines.contants.LogConstants;
import com.challenge.deviget.mines.controller.payload.BoardRequest;
import com.challenge.deviget.mines.exception.GameException;
import com.challenge.deviget.mines.service.GameService;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.LogstashMarker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static net.logstash.logback.marker.Markers.append;

@RestController
@RequestMapping("/minesweeper/v1")
@Validated
@Slf4j
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping(value="/game", consumes = "application/json")
    public ResponseEntity newGame(@Valid @RequestBody BoardRequest request) {
        LogstashMarker logMarker = append(LogConstants.KEY_REQUEST_BODY, request);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(gameService.createGame(request));
        } catch (GameException e) {
            log.error(logMarker, LogConstants.OP_NEW_GAME, e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
