package io.chess;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Movement {
    private Square[] path;
    private boolean knightMove = false;
    private int numSteps = 0;
    private boolean straightMove = false; // Possible move for rook, queen, king or pawn
    private boolean diagonalMove = false; // Possible move for bishop, queen, king or pawn
}
