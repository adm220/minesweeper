package com.challenge.deviget.mines.service.impl;

import com.challenge.deviget.mines.controller.payload.BoardRequest;
import com.challenge.deviget.mines.controller.payload.PlayRequest;
import com.challenge.deviget.mines.exception.GameException;
import com.challenge.deviget.mines.helper.BoardHelper;
import com.challenge.deviget.mines.model.Cell;
import com.challenge.deviget.mines.model.Game;
import com.challenge.deviget.mines.model.Marks;
import com.challenge.deviget.mines.model.States;
import com.challenge.deviget.mines.model.entity.GameEntity;
import com.challenge.deviget.mines.repository.GameRepository;
import com.challenge.deviget.mines.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Slf4j
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;


    @Override
    public Game createGame(BoardRequest boardRequest) {
        Cell[][] matrixBoard;
        try {
            if (gameRepository.findByUserNameAndState(boardRequest.getName(), States.ACTIVE).isPresent()) {
                throw new GameException(String.format("A game has been found for username=%s", boardRequest.getName()));
            }
            matrixBoard = BoardHelper.initializeBoardGame(boardRequest);

            BoardHelper.randomlyInstallMines(boardRequest, matrixBoard);
            BoardHelper.setMinesArround(boardRequest, matrixBoard);

            GameEntity game = GameEntity.builder()
                    .mines(matrixBoard)
                    .userName(boardRequest.getName())
                    .build();
            game = gameRepository.save(game);


            return Game.builder().username(game.getUserName()).state(game.getState()).mines(game.getMines()).build();

        } catch(Exception ex) {
            throw new GameException(String.format("Error creating a new game for username=%s",
                    boardRequest.getName()), ex);
        }
    }

    @Override
    public Game play(String username, PlayRequest request) {
        Cell[][] matrixBoard;
        int row = request.getRow();
        int column = request.getColumn();

        Optional<GameEntity> game = gameRepository.findByUserNameAndState(username, States.ACTIVE);

        if (!game.isPresent()) {
            throw new GameException(String.format("There's no active game for username=%s", username));
        }
        matrixBoard = game.get().getMines();


        if (BoardHelper.mineFound(matrixBoard, row, column)) {
            game.get().setState(States.EXPLODE);
        } else {

            BoardHelper.clearEmptySpots(matrixBoard, request.getRow(), request.getColumn(), matrixBoard.length - 1, matrixBoard[0].length - 1);
            matrixBoard[row][column].setRevealed(true);
            game.get().setMines(matrixBoard);

            if (BoardHelper.alreadyWon(matrixBoard)) {
                game.get().setState(States.WON);
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
            throw new GameException(String.format("There's no active game for username=%s", username));
        }

        if (game.get().getMines()[request.getRow()][request.getColumn()].isRevealed()) {
            throw new GameException("Cell already revealed");
        }

        if (mark.equals(Marks.FLAG)) {
            game.get().setFlag(true);
        } else {
            game.get().setQuestionMark(true);
        }

        Game gameNew = new Game();
        BeanUtils.copyProperties(gameRepository.save(game.get()),gameNew);

        return gameNew;
    }
}
