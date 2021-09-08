package com.challenge.deviget.mines.helper;

import com.challenge.deviget.mines.controller.payload.BoardRequest;
import com.challenge.deviget.mines.model.Cell;

import java.util.Random;

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


    public static void randomlyInstallMines(BoardRequest boardRequest, Cell[][] matrix) {
        int minesPlaced = 0;
        Random random = new Random();
        while(minesPlaced < boardRequest.getLevel().getMines()) {
            int x = random.nextInt(boardRequest.getLevel().getRow());
            int y = random.nextInt(boardRequest.getLevel().getCollumn());
            if(!matrix[y][x].isMine()) {
                matrix[y][x].setMine(true);
                minesPlaced ++;
            }
        }
    }


    public static void setMinesArround(BoardRequest boardRequest, Cell[][] matrix) {
        for (int x=0; x < boardRequest.getLevel().getRow(); x ++) {
            for (int y= 0; y < boardRequest.getLevel().getCollumn(); y++) {
                matrix[x][y].setMinesAround(minesNear(matrix, x, y));
            }
        }
    }

    private static int minesNear(Cell[][] matrix, int x, int y) {
        int mines = 0;
        mines += mineAt(matrix, y - 1, x - 1);
        mines += mineAt(matrix, y - 1, x);
        mines += mineAt(matrix,y - 1, x + 1);
        mines += mineAt(matrix, y,x - 1);
        mines += mineAt(matrix, y, x + 1);
        mines += mineAt(matrix, y + 1, x - 1);
        mines += mineAt(matrix, y + 1, x);
        mines += mineAt(matrix,y + 1, x + 1);
        return mines;
    }

    private static int mineAt(Cell[][] matrix, int y, int x) {
        //matrix[0] is hardcoded because it is a regular matrix
        if(y >= 0 && y < matrix[0].length && x >= 0 && x < matrix.length && matrix[y][x].isMine()) {
            return 1;
        } else {
            return 0;
        }
    }

    public static boolean mineFound(Cell[][] matrix, int row, int column) {
        return matrix[row][column].isMine();
    }

    public static boolean alreadyWon(Cell[][] matrix) {
        for (int i=0 ; i < matrix.length ; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (!matrix[i][j].isMine() && !matrix[i][j].isRevealed()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void clearEmptySpots(Cell[][] matrix, int x, int y, int xMax, int yMax) {
        if (x < 0 || x > xMax || y < 0 || y > yMax){
            return;
        }

        if ( matrix[x][y].getMinesAround() == 0 && !matrix[x][y].isRevealed()) {
            matrix[x][y].setRevealed(true);
            clearEmptySpots(matrix, x+1, y , xMax, yMax);
            clearEmptySpots(matrix, x+1, y+1 , xMax, yMax);
            clearEmptySpots(matrix, x+1, y-1 , xMax, yMax);
            clearEmptySpots(matrix, x-1, y , xMax, yMax);
            clearEmptySpots(matrix, x-1, y-1 , xMax, yMax);
            clearEmptySpots(matrix, x-1, y+1 , xMax, yMax);
            clearEmptySpots(matrix, x, y-1 , xMax, yMax);
            clearEmptySpots(matrix, x, y+1 , xMax, yMax);
        } else {
            return;
        }
    }
}
