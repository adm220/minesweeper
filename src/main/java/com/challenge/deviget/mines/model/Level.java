package com.challenge.deviget.mines.model;

public enum Level {
    EASY(9,9,10), MEDIUM(16,16,30), HARD(30,16,60);

    Level(int row, int collumn, int bombs){
        this.row = row;
        this.collumn = collumn;
        this.mines = bombs;
    }
    private final int row;

    public int getRow() {
        return row;
    }

    public int getCollumn() {
        return collumn;
    }

    public int getMines() {
        return mines;
    }

    private final int collumn;
    private final int mines;
}
