package com.challenge.deviget.mines.controller.payload;

import com.challenge.deviget.mines.model.Cell;
import com.challenge.deviget.mines.model.States;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardResponse {
    private String username;
    private States state;
    private Cell[][] field;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
