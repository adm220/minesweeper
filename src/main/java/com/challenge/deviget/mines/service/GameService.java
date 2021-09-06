package com.challenge.deviget.mines.service;

import com.challenge.deviget.mines.controller.payload.BoardRequest;
import com.challenge.deviget.mines.model.Game;

public interface GameService {
    Game createGame(BoardRequest boardRequest);

}
