package com.challenge.deviget.mines.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cell implements Serializable {

    private boolean revealed;
    private int minesAround;
    private boolean mine;
    private boolean flag;
    private boolean questionMark;

    public Cell(boolean mine) {
        this.mine = mine;
    }
}
