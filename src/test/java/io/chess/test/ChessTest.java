package io.chess.test;

import io.chess.Board;
import io.chess.Move;
import io.chess.PieceType;
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

    @Test
    void testClone() throws Exception {
        Board board = new Board();
        board.moveUnsafe(new Move(board.getPiece(true, PieceType.PAWN, 3), board.getSquare("e2"), board.getSquare("e4")));
        board.visualize();
        Board newBoard = board.clone();
        newBoard.moveUnsafe(new Move(newBoard.getPiece(true, PieceType.PAWN, 3), newBoard.getSquare("e7"), newBoard.getSquare("e5")));
        newBoard.visualize();
        board.visualize();
    }
}
