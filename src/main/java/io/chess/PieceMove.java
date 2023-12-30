package io.chess;

import static io.chess.Constants.*;
import static io.chess.Constants.RANK_8;

public class PieceMove {
    private final Board board;
    private final Move move;
    private final Piece piece;
    private final Square s1;
    private final Square s2;

    // Details captured for reverting the move if necessary

    private Piece capturedPiece;
    private Piece promotedPiece;
    private boolean pieceMovedState;

    public PieceMove(Board board, Move move) {
        this.board = board;
        this.move = move;
        this.piece = move.getPiece();
        this.s1 = move.getS1();
        this.s2 = move.getS2();
    }

    public void revert() {
        s1.setPiece(piece);
        if (capturedPiece != null) {
            board.addPiece(capturedPiece, s2);
        } else {
            s2.setPiece(null);
        }
        // XXX: en passant
        piece.setSquare(s1);
        if (move.getPromotesTo() != null) {
            board.addPiece(piece, s1);
            board.removePiece(promotedPiece);
        }
        piece.setMoved(pieceMovedState);
    }

    public void play() {
        s1.setPiece(null);
        if (s2.isOccupied()) {
            capturedPiece = s2.getPiece();
            board.removePiece(s2.getPiece());
        }
        // En passant
        if (piece.getType() == PieceType.PAWN && s1.getFile() != s2.getFile() && !s2.isOccupied()) {
            Square oppPawnSquare = board.getSquare(s2.getFile(), s2.getRank() + (board.isMoverColor() ? -1 : 1));
            board.removePiece(oppPawnSquare.getPiece());
            oppPawnSquare.setPiece(null);
        }

        s2.setPiece(piece);
        piece.setSquare(s2);
        if (piece.getType() == PieceType.KING && s1.getFile() == FILE_E && s2.getFile() == FILE_G) {
            board.getPiece(board.isMoverColor(), PieceType.ROOK, 1)
                    .setSquare(board.getSquare(FILE_F, board.isMoverColor() ? RANK_1 : RANK_8));
        }
        if (piece.getType() == PieceType.KING && s1.getFile() == FILE_E && s2.getFile() == FILE_C) {
            board.getPiece(board.isMoverColor(), PieceType.ROOK, 0)
                    .setSquare(board.getSquare(FILE_D, board.isMoverColor() ? RANK_1 : RANK_8));
        }

        if (move.getPromotesTo() != null) {
            board.removePiece(piece);
            Piece newPiece = new Piece(piece.isColor(), move.getPromotesTo());
            newPiece.setMoved(true);
            promotedPiece = newPiece;
            board.addPiece(newPiece, s2);
        }

        pieceMovedState = piece.isMoved();
        piece.setMoved(true);
    }

}
