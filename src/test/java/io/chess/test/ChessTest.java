package io.chess.test;

import io.chess.Board;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ChessTest {

    @Test
    void testKingCantMoveIntoCheck() throws Exception {
        Board board = new Board();
        board.playMoves("1. e4 a5 2. e5 Nf6 3. exf6 e5 4. Nf3");
        Assertions.assertThrows(Exception.class, () -> board.move("Ke7"));
    }

    @Test
    void testPieceCantMoveExposingKingToCheck() throws Exception {
        Board board = new Board();
        board.playMoves("e4 e5 Nf3 Bb4");
        Assertions.assertThrows(Exception.class, () -> board.move("d4"));
    }
}
