package com.challenge.deviget.mines.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    private String username;
    private States state;
    private Cell[][] field;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}

