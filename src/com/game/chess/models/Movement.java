package com.game.chess.models;

import com.game.chess.Position;

public interface Movement {

    boolean isValidMove(Position destPosition);
    void move(Position position);
    void forceMove(Position position);
}
