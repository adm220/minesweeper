package com.challenge.deviget.mines.model.entity;

import com.challenge.deviget.mines.model.Cell;
import com.challenge.deviget.mines.model.States;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String userName;

    @Column
    @Enumerated(EnumType.STRING)
    private States state;

    @Lob
    private Cell[][] field;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

}