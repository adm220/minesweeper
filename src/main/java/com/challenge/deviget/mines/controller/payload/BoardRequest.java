package com.challenge.deviget.mines.controller.payload;

import com.challenge.deviget.mines.model.Level;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class BoardRequest {
    @NotEmpty
    private String name;

    @NotNull
    private Level level;
}
