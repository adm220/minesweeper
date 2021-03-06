package com.challenge.deviget.mines.controller.payload;

import com.challenge.deviget.mines.model.Level;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequest {

    @NotNull
    private Level level;
}
