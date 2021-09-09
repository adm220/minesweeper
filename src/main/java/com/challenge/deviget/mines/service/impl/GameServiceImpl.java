package com.challenge.deviget.mines.service.impl;

import com.challenge.deviget.mines.controller.payload.BoardRequest;
import com.challenge.deviget.mines.controller.payload.PlayRequest;
import com.challenge.deviget.mines.exception.GameException;
import com.challenge.deviget.mines.exception.GameNotFoundException;
import com.challenge.deviget.mines.exception.InvalidActionException;
import com.challenge.deviget.mines.helper.BoardHelper;
import com.challenge.deviget.mines.model.Cell;
import com.challenge.deviget.mines.model.Game;
import com.challenge.deviget.mines.model.Marks;
import com.challenge.deviget.mines.model.States;
import com.challenge.deviget.mines.model.entity.GameEntity;
import com.challenge.deviget.mines.repository.GameRepository;
import com.challenge.deviget.mines.service.GameService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;


    @Override
    public Game createGame(BoardRequest boardRequest, String username) {
        Cell[][] matrixBoard;
        try {
            if (gameRepository.findByUserNameAndState(username, States.ACTIVE).isPresent()) {
                throw new InvalidActionException(String.format("A game has been found for username=%s", username));
            }
            matrixBoard = BoardHelper.initializeBoardGame(boardRequest);

            BoardHelper.randomlyInstallMines(boardRequest, matrixBoard);
            BoardHelper.setMinesArround(boardRequest, matrixBoard);

            GameEntity game = GameEntity.builder()
                    .field(matrixBoard)
                    .userName(username)
                    .startTime(LocalDateTime.now())
                    .state(States.ACTIVE)
                    .build();
            game = gameRepository.save(game);


            return Game.builder().username(game.getUserName()).state(game.getState()).field(game.getField()).build();

        } catch(Exception ex) {
            throw new GameException(String.format("Error creating a new game for username=%s",
                    username), ex);
        }
    }

    @Override
    public Game play(String username, PlayRequest request) {
        Cell[][] matrixBoard;
        int row = request.getRow();
        int column = request.getColumn();

        Optional<GameEntity> game = gameRepository.findByUserNameAndState(username, States.ACTIVE);
        if (!game.isPresent()) {
            throw new GameNotFoundException(String.format("There's no active game for username=%s", username));
        }
        matrixBoard = game.get().getField();


        if (BoardHelper.mineFound(matrixBoard, row, column)) {
            game.get().setState(States.EXPLODE);
            game.get().setEndTime(LocalDateTime.now());
        } else {

            BoardHelper.clearEmptySpots(matrixBoard, request.getRow(), request.getColumn(), matrixBoard.length - 1, matrixBoard[0].length - 1);
            matrixBoard[row][column].setRevealed(true);
            game.get().setField(matrixBoard);

            if (BoardHelper.alreadyWon(matrixBoard)) {
                game.get().setState(States.WON);
                game.get().setEndTime(LocalDateTime.now());
            }
        }
        Game gameNew = new Game();
        BeanUtils.copyProperties(gameRepository.save(game.get()),gameNew);

        return gameNew;
    }

    @Override
    public Game mark(String username, PlayRequest request, Marks mark) {
        Optional<GameEntity> game = gameRepository.findByUserNameAndState(username, States.ACTIVE);

        if (!game.isPresent()) {
            throw new GameNotFoundException(String.format("There's no active game for username=%s", username));
        }

        if (game.get().getField()[request.getRow()][request.getColumn()].isRevealed()) {
            throw new InvalidActionException("Cell already revealed");
        }

        if (mark.equals(Marks.FLAG)) {
            game.get().getField()[request.getRow()][request.getColumn()].setFlag(true);
        } else {
            game.get().getField()[request.getRow()][request.getColumn()].setQuestionMark(true);
        }

        Game gameNew = new Game();
        BeanUtils.copyProperties(gameRepository.save(game.get()),gameNew);

        return gameNew;
    }

    @Override
    public Game resumeGame(String username) {
        return gameRepository.findByUserNameAndState(username, States.ACTIVE)
                .map(game -> Game.builder()
                        .field(game.getField())
                        .username(game.getUserName())
                        .state(game.getState())
                        .startTime(game.getStartTime())
                        .endTime(game.getEndTime())
                        .build())
                .orElseThrow(() -> new GameNotFoundException(String.format("There's no active game for username=%s", username)));
    }
}
