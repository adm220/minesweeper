package com.challenge.deviget.mines.repository;

import com.challenge.deviget.mines.model.Game;
import com.challenge.deviget.mines.model.States;
import com.challenge.deviget.mines.model.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Long> {

    Optional<GameEntity> findByUserNameAndState(String userName, States states);

}
