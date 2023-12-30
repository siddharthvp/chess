package io.chess;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Move {
    private Piece piece;
    private Square s1;
    private Square s2;

    // to store if legality has already been checked
    private boolean isLegal = false;

    private boolean isKingSideCastle = false;
    private boolean isQueenSideCastle = false;
    private PieceType promotesTo = null;
}
