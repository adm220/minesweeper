package com.challenge.deviget.mines.helper;

import com.challenge.deviget.mines.controller.payload.BoardRequest;
import com.challenge.deviget.mines.model.Cell;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BoardHelper {


    public static Cell[][] initializeBoardGame(BoardRequest boardRequest) {
        Cell matrix[][];
        matrix = new Cell[boardRequest.getLevel().getRow()][boardRequest.getLevel().getCollumn()];
        for (int i=0 ; i < boardRequest.getLevel().getRow(); i++) {
            for (int j = 0; j < boardRequest.getLevel().getCollumn(); j++) {
                matrix[i][j] = new Cell();
            }
        }

        return matrix;
    }


}
