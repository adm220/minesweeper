package com.challenge.deviget.mines.service.impl;

import com.challenge.deviget.mines.controller.payload.BoardRequest;
import com.challenge.deviget.mines.exception.GameException;
import com.challenge.deviget.mines.helper.BoardHelper;
import com.challenge.deviget.mines.model.Cell;
import com.challenge.deviget.mines.model.Game;
import com.challenge.deviget.mines.model.States;
import com.challenge.deviget.mines.model.entity.GameEntity;
import com.challenge.deviget.mines.repository.GameRepository;
import com.challenge.deviget.mines.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
}
