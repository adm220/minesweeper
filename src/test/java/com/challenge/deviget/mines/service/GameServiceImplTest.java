package com.challenge.deviget.mines.service;

import com.challenge.deviget.mines.controller.payload.BoardRequest;
import com.challenge.deviget.mines.controller.payload.PlayRequest;
import com.challenge.deviget.mines.exception.GameException;
import com.challenge.deviget.mines.model.*;
import com.challenge.deviget.mines.model.entity.GameEntity;
import com.challenge.deviget.mines.repository.GameRepository;
import com.challenge.deviget.mines.service.impl.GameServiceImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {GameServiceImpl.class})
class GameServiceImplTest {

    private static final String USERNAME = "deviget";

    @Autowired
    private GameServiceImpl gameService;

    @MockBean
    private GameRepository gameRepository;

    private GameEntity game;

    @BeforeEach
    void setup() {
        Cell[][] field = new Cell[2][2];
        game = GameEntity.builder().id(321L).userName(USERNAME).field(field).build();
    }

    @Test
    void testCreateGameSuccessful() {
        when(gameRepository.findByUserNameAndState(USERNAME, States.ACTIVE)).thenReturn(Optional.empty());
        when(gameRepository.save(any(GameEntity.class))).thenReturn(game);
        gameService.createGame(BoardRequest.builder().level(Level.EASY).build(),USERNAME);

        verify(gameRepository, times(1)).save(any(GameEntity.class));
    }

    @Test
    void testCreateGameNameAlreadyExists() {
        when(gameRepository.findByUserNameAndState(USERNAME, States.ACTIVE))
                .thenReturn(Optional.of(new GameEntity()));
        Assertions.assertThrows(GameException.class, () -> {
            gameService.createGame(BoardRequest.builder().level(Level.EASY).build(), USERNAME);
        });

    }

    @Test
    void testResumeGame() {
        when(gameRepository.findByUserNameAndState(USERNAME, States.ACTIVE)).thenReturn(Optional.of(game));
        assertThat(gameService.resumeGame(USERNAME), is(notNullValue()));
    }

    @Test
    void testPlayGameNotFound() {
        when(gameRepository.findByUserNameAndState(USERNAME, States.ACTIVE)).thenReturn(Optional.empty());
        Assertions.assertThrows(GameException.class, () -> {
            gameService.play(USERNAME, PlayRequest.builder().column(0).row(0).build());
        });
    }

    @Test
    void testResumeGameNotFound() {
        when(gameRepository.findByUserNameAndState(USERNAME, States.ACTIVE)).thenReturn(Optional.empty());
        Assertions.assertThrows(GameException.class, () -> {
            gameService.resumeGame(USERNAME);
        });
    }

    @Test
    void testMarkFlag() {
        game.getField()[0][0] = new Cell();
        game.getField()[0][0].setRevealed(false);
        when(gameRepository.findByUserNameAndState(USERNAME, States.ACTIVE)).thenReturn(Optional.of(game));
        when(gameRepository.save(any(GameEntity.class))).thenReturn(game);
        Game result = gameService.mark(USERNAME, PlayRequest.builder().column(0).row(0).build(), Marks.FLAG);
        assertThat(result, is(notNullValue()));
        assertThat(result.getField()[0][0].isFlag(), is(Boolean.TRUE));
    }

    @Test
    void testMarkQuestionMark() {
        game.getField()[0][0] = new Cell();
        game.getField()[0][0].setRevealed(false);
        when(gameRepository.findByUserNameAndState(USERNAME, States.ACTIVE)).thenReturn(Optional.of(game));
        when(gameRepository.save(any(GameEntity.class))).thenReturn(game);
        Game result = gameService.mark(USERNAME, PlayRequest.builder().column(0).row(0).build(), Marks.QUESTION_MARK);
        assertThat(result, is(notNullValue()));
        assertThat(result.getField()[0][0].isQuestionMark(), is(Boolean.TRUE));
    }

    @Test
    void testMarkFlagGameNotFound() {
        when(gameRepository.findByUserNameAndState(USERNAME, States.ACTIVE)).thenReturn(Optional.empty());
        Assertions.assertThrows(GameException.class, () -> {
            gameService.mark(USERNAME, PlayRequest.builder().column(0).row(0).build(), Marks.FLAG);
        });
    }

    @Test
    void testMarkQuestionMarkCellRevealed() {
        game.getField()[0][0] = new Cell();
        game.getField()[0][0].setRevealed(true);
        when(gameRepository.findByUserNameAndState(USERNAME, States.ACTIVE)).thenReturn(Optional.of(game));
        Assertions.assertThrows(GameException.class, () -> {
            gameService.mark(USERNAME, PlayRequest.builder().column(0).row(0).build(), Marks.QUESTION_MARK);
        });
    }

    @Test
    void testWon() {
        game.getField()[0][0] = new Cell(false);
        game.getField()[0][1] = new Cell(true);
        game.getField()[1][0] = new Cell(true);
        game.getField()[1][1] = new Cell(true);
        when(gameRepository.findByUserNameAndState(USERNAME, States.ACTIVE)).thenReturn(Optional.of(game));
        when(gameRepository.save(any(GameEntity.class))).thenReturn(game);
        gameService.play(USERNAME, PlayRequest.builder().column(0).row(0).build());

        ArgumentCaptor<GameEntity> captor = ArgumentCaptor.forClass(GameEntity.class);
        verify(gameRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getState(), is(States.WON));
    }

    @Test
    void testExplode() {
        game.getField()[0][0] = new Cell(true);
        when(gameRepository.findByUserNameAndState(USERNAME, States.ACTIVE)).thenReturn(Optional.of(game));
        when(gameRepository.save(any(GameEntity.class))).thenReturn(game);
        gameService.play(USERNAME, PlayRequest.builder().column(0).row(0).build());

        ArgumentCaptor<GameEntity> captor = ArgumentCaptor.forClass(GameEntity.class);
        verify(gameRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getState(), is(States.EXPLODE));
    }
}

