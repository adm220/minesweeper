package com.challenge.deviget.mines.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    private String username;
    private States state;
    private Cell[][] mines;
    private boolean flag;
    private boolean questionMark;

}

