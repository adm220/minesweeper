package com.challenge.deviget.mines.model.entity;

import com.challenge.deviget.mines.model.Cell;
import com.challenge.deviget.mines.model.States;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
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

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Cell[][] field;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

}