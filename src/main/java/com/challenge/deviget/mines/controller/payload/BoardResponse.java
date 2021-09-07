package com.challenge.deviget.mines.controller.payload;

import com.challenge.deviget.mines.model.Cell;
import com.challenge.deviget.mines.model.States;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardResponse {
    private String username;
    private States state;
    private Cell[][] mines;
    private boolean flag;
    private boolean questionMark;
}
