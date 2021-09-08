package com.challenge.deviget.mines.controller;

import com.challenge.deviget.mines.contants.LogConstants;
import com.challenge.deviget.mines.controller.payload.BoardRequest;
import com.challenge.deviget.mines.controller.payload.BoardResponse;
import com.challenge.deviget.mines.controller.payload.PlayRequest;
import com.challenge.deviget.mines.exception.GameException;
import com.challenge.deviget.mines.model.Game;
import com.challenge.deviget.mines.model.Marks;
import com.challenge.deviget.mines.service.GameService;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.LogstashMarker;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
            Game game = gameService.createGame(request);

            logMarker.and(append(LogConstants.KEY_HTTP_STATUS, HttpStatus.CREATED).and(append(LogConstants.KEY_RESPONSE_BODY, game)));
            log.info(logMarker, LogConstants.OP_NEW_GAME);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponseBody(game));
        } catch (GameException e) {
            log.error(logMarker, LogConstants.OP_NEW_GAME, e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping(value = "/game/{username}",  consumes = "application/json")
    public ResponseEntity playGame(@Valid @RequestBody PlayRequest request, @PathVariable String username) {
        LogstashMarker logMarker = append(LogConstants.KEY_REQUEST_BODY, request)
                .and(append(LogConstants.KEY_USERNAME, username));
        try {
            Game game = gameService.play(username, request);

            logMarker.and(append(LogConstants.KEY_HTTP_STATUS, HttpStatus.OK).and(append(LogConstants.KEY_RESPONSE_BODY, game)));
            log.info(logMarker, LogConstants.OP_PLAY);

            return ResponseEntity.ok( mapToResponseBody(game));
        } catch (GameException e) {
            log.error(logMarker, LogConstants.OP_PLAY, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(value = "/game/{username}/flag", consumes = "application/json")
    public ResponseEntity flag(@Valid @RequestBody PlayRequest request, @PathVariable String username) {
        LogstashMarker logMarker = append(LogConstants.KEY_REQUEST_BODY, request)
                .and(append(LogConstants.KEY_USERNAME, username));
        try {
            Game game = gameService.mark(username, request, Marks.FLAG);

            logMarker.and(append(LogConstants.KEY_HTTP_STATUS, HttpStatus.OK).and(append(LogConstants.KEY_RESPONSE_BODY, game)));
            log.info(logMarker, LogConstants.OP_FLAG);

            return ResponseEntity.ok(mapToResponseBody(game));
        } catch (GameException e) {
            log.error(logMarker, LogConstants.OP_FLAG, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(value = "/game/{username}/question",  consumes = "application/json")
    public ResponseEntity questionMark(@Valid @RequestBody PlayRequest request, @PathVariable String username) {
        LogstashMarker logMarker = append(LogConstants.KEY_REQUEST_BODY, request)
                .and(append(LogConstants.KEY_USERNAME, username));
        try {
            Game game = gameService.mark(username, request, Marks.QUESTION_MARK);

            logMarker.and(append(LogConstants.KEY_HTTP_STATUS, HttpStatus.OK).and(append(LogConstants.KEY_RESPONSE_BODY, game)));
            log.info(logMarker, LogConstants.OP_QUESTION_MARK);

            return ResponseEntity.ok(mapToResponseBody(game));
        } catch (GameException e) {
            log.error(logMarker, LogConstants.OP_QUESTION_MARK, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value="/game/{username}")
    public ResponseEntity resume(@PathVariable String username) {
        LogstashMarker logMarker =append(LogConstants.KEY_USERNAME, username);
        try {
            Game game = gameService.resumeGame(username);

            logMarker.and(append(LogConstants.KEY_HTTP_STATUS, HttpStatus.OK).and(append(LogConstants.KEY_RESPONSE_BODY, game)));
            log.info(logMarker, LogConstants.OP_RESUME);

            return ResponseEntity.ok(mapToResponseBody(game));
        } catch (GameException e) {
            log.error(logMarker, LogConstants.OP_PLAY, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    private BoardResponse mapToResponseBody(Game game) {
        BoardResponse boardResponse = new BoardResponse();
        BeanUtils.copyProperties(game, boardResponse);
        return boardResponse;
    }
}
