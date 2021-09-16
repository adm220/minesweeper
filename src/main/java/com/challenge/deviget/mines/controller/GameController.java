package com.challenge.deviget.mines.controller;

import com.challenge.deviget.mines.contants.LogConstants;
import com.challenge.deviget.mines.controller.payload.BoardRequest;
import com.challenge.deviget.mines.controller.payload.BoardResponse;
import com.challenge.deviget.mines.controller.payload.ErrorResponse;
import com.challenge.deviget.mines.controller.payload.PlayRequest;
import com.challenge.deviget.mines.exception.GameException;
import com.challenge.deviget.mines.exception.GameNotFoundException;
import com.challenge.deviget.mines.exception.InvalidActionException;
import com.challenge.deviget.mines.model.Game;
import com.challenge.deviget.mines.model.Marks;
import com.challenge.deviget.mines.service.GameService;
import io.swagger.annotations.*;
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

    private static final String USERNAME = "username";

    @Autowired
    private GameService gameService;

    @ApiOperation(value = "Create a new game")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created",response = BoardResponse.class),
            @ApiResponse(code = 401, message = "Invalid api-key", response = ErrorResponse.class),
            @ApiResponse(code = 409, message = "Unfinished game has been found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)})
    @PostMapping(value="/game", consumes = "application/json")
    public ResponseEntity<BoardResponse> newGame(@ApiParam(value = "Easy 9x9 10 mines, Medium 16x16 30 mines, Hard 30x16 60 mines" ,required=true )
                                                     @Valid @RequestBody BoardRequest request, @RequestAttribute(name = USERNAME) String username) {
        LogstashMarker logMarker = append(LogConstants.KEY_REQUEST_BODY, request);
        try {
            Game game = gameService.createGame(request, username);

            logMarker.and(append(LogConstants.KEY_HTTP_STATUS, HttpStatus.CREATED).and(append(LogConstants.KEY_RESPONSE_BODY, game)));
            log.info(logMarker, LogConstants.OP_NEW_GAME);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponseBody(game));
        }catch (InvalidActionException e) {
            log.error(logMarker, LogConstants.OP_NEW_GAME, e);
            return new ResponseEntity(ErrorResponse.builder().errorMessage(e.getMessage()).build(),HttpStatus.CONFLICT);
        }
    }
    @ApiOperation(value = "Reveal a cell")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok",response = BoardResponse.class),
            @ApiResponse(code = 401, message = "Invalid api-key", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "No active game found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)})
    @PutMapping(value = "/game/play",  consumes = "application/json")
    public ResponseEntity<BoardResponse> playGame(@Valid @RequestBody PlayRequest request
            ,@RequestAttribute(name = USERNAME) String username) {
        LogstashMarker logMarker = append(LogConstants.KEY_REQUEST_BODY, request)
                .and(append(LogConstants.KEY_USERNAME, username));
        try {
            Game game = gameService.play(username, request);

            logMarker.and(append(LogConstants.KEY_HTTP_STATUS, HttpStatus.OK).and(append(LogConstants.KEY_RESPONSE_BODY, game)));
            log.info(logMarker, LogConstants.OP_PLAY);

            return ResponseEntity.ok( mapToResponseBody(game));
        } catch (GameNotFoundException e) {
            log.error(logMarker, LogConstants.OP_PLAY, e);
            return new ResponseEntity(ErrorResponse.builder().errorMessage(e.getMessage()).build(),HttpStatus.NOT_FOUND);
        }
    }
    @ApiOperation(value = "Mark a cell with a redflag")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok",response = BoardResponse.class),
            @ApiResponse(code = 401, message = "Invalid api-key", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "No active game found", response = ErrorResponse.class),
            @ApiResponse(code = 422, message = "Cell already marked", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)})
    @PutMapping(value = "/game/flag", consumes = "application/json")
    public ResponseEntity<BoardResponse> flag(@Valid @RequestBody PlayRequest request,@RequestAttribute(name = USERNAME) String username) {
        LogstashMarker logMarker = append(LogConstants.KEY_REQUEST_BODY, request)
                .and(append(LogConstants.KEY_USERNAME, username));
        try {
            Game game = gameService.mark(username, request, Marks.FLAG);

            logMarker.and(append(LogConstants.KEY_HTTP_STATUS, HttpStatus.OK).and(append(LogConstants.KEY_RESPONSE_BODY, game)));
            log.info(logMarker, LogConstants.OP_FLAG);

            return ResponseEntity.ok(mapToResponseBody(game));
        } catch (GameNotFoundException e) {
            log.error(logMarker, LogConstants.OP_FLAG, e);
            return new ResponseEntity(ErrorResponse.builder().errorMessage(e.getMessage()).build(),HttpStatus.NOT_FOUND);
        }
        catch (InvalidActionException e) {
            log.error(logMarker, LogConstants.OP_FLAG, e);
            return new ResponseEntity(ErrorResponse.builder().errorMessage(e.getMessage()).build(),HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    @ApiOperation(value = "Mark a cell with a question mark")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok",response = BoardResponse.class),
            @ApiResponse(code = 401, message = "Invalid api-key", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "No active game found", response = ErrorResponse.class),
            @ApiResponse(code = 422, message = "Cell already marked", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)})
    @PutMapping(value = "/game/question",  consumes = "application/json")
    public ResponseEntity<BoardResponse> questionMark(@Valid @RequestBody PlayRequest request, @RequestAttribute(name = USERNAME) String username) {
        LogstashMarker logMarker = append(LogConstants.KEY_REQUEST_BODY, request)
                .and(append(LogConstants.KEY_USERNAME, username));
        try {
            Game game = gameService.mark(username, request, Marks.QUESTION_MARK);

            logMarker.and(append(LogConstants.KEY_HTTP_STATUS, HttpStatus.OK).and(append(LogConstants.KEY_RESPONSE_BODY, game)));
            log.info(logMarker, LogConstants.OP_QUESTION_MARK);

            return ResponseEntity.ok(mapToResponseBody(game));
        } catch (GameNotFoundException e) {
            log.error(logMarker, LogConstants.OP_QUESTION_MARK, e);
            return new ResponseEntity(ErrorResponse.builder().errorMessage(e.getMessage()).build(),HttpStatus.NOT_FOUND);
        }
        catch (InvalidActionException e) {
            log.error(logMarker, LogConstants.OP_QUESTION_MARK, e);
            return new ResponseEntity(ErrorResponse.builder().errorMessage(e.getMessage()).build(),HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    @ApiOperation(value = "Return the last game")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok",response = BoardResponse.class),
            @ApiResponse(code = 401, message = "Invalid api-key", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "No active game found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)})
    @GetMapping(value="/game")
    public ResponseEntity<BoardResponse> resume(@RequestAttribute(name = USERNAME) String username) {
        LogstashMarker logMarker =append(LogConstants.KEY_USERNAME, username);
        try {
            Game game = gameService.resumeGame(username);

            logMarker.and(append(LogConstants.KEY_HTTP_STATUS, HttpStatus.OK).and(append(LogConstants.KEY_RESPONSE_BODY, game)));
            log.info(logMarker, LogConstants.OP_RESUME);

            return ResponseEntity.ok(mapToResponseBody(game));
        } catch (GameNotFoundException e) {
            log.error(logMarker, LogConstants.OP_RESUME, e);
            return  new ResponseEntity(ErrorResponse.builder().errorMessage(e.getMessage()).build(),HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorResponse> generalError(RuntimeException e){
        log.error(null, e,LogConstants.OP_UNKNOWN_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.builder().errorMessage(e.getMessage()).build());
    }
    private BoardResponse mapToResponseBody(Game game) {
        BoardResponse boardResponse = new BoardResponse();
        BeanUtils.copyProperties(game, boardResponse);
        return boardResponse;
    }
}
