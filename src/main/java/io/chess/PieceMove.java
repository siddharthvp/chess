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

    public void play() {
        s1.setPiece(null);
        if (s2.isOccupied()) {
            capturedPiece = s2.getPiece();
            board.removePiece(s2.getPiece());
        }
        // En passant
        if (piece.getType() == PieceType.PAWN && s1.getFile() != s2.getFile() && !s2.isOccupied()) {
            Square oppPawnSquare = board.getSquare(s2.getFile(), s2.getRank() + (board.isMoverColor() ? -1 : 1));
            capturedPiece = oppPawnSquare.getPiece();
            board.removePiece(oppPawnSquare.getPiece());
            oppPawnSquare.setPiece(null);
        }

        s2.setPiece(piece);
        piece.setSquare(s2);
        if (piece.getType() == PieceType.KING && s1.getFile() == FILE_E && s2.getFile() == FILE_G) {
            Piece rook = board.getPiece(board.isMoverColor(), PieceType.ROOK, 1);
            rook.getSquare().setPiece(null);
            Square newRookSquare = board.getSquare(FILE_F, board.isMoverColor() ? RANK_1 : RANK_8);
            rook.setSquare(newRookSquare);
            newRookSquare.setPiece(rook);
        }
        if (piece.getType() == PieceType.KING && s1.getFile() == FILE_E && s2.getFile() == FILE_C) {
            Piece rook = board.getPiece(board.isMoverColor(), PieceType.ROOK, 0);
            rook.getSquare().setPiece(null);
            Square newRookSquare = board.getSquare(FILE_D, board.isMoverColor() ? RANK_1 : RANK_8);
            rook.setSquare(newRookSquare);
            newRookSquare.setPiece(rook);
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

    public void revert() {
        s1.setPiece(piece);
        if (capturedPiece != null) {
            undoRemoval(capturedPiece, s2);
        }
        s2.setPiece(capturedPiece); // could be null

        // but if it was en passant capture
        if (piece.getType() == PieceType.PAWN && s1.getFile() != s2.getFile() && !s2.isOccupied()) {
            s2.setPiece(null); // captured piece is not on s2, in case of en passant
            // fix square as well
            Square oppPawnSquare = board.getSquare(s2.getFile(), s2.getRank() + (board.isMoverColor() ? -1 : 1));
            capturedPiece.setSquare(oppPawnSquare);
        }

        piece.setSquare(s1);

        if (piece.getType() == PieceType.KING && s1.getFile() == FILE_E && s2.getFile() == FILE_G) {
            Piece rook = board.getPiece(board.isMoverColor(), PieceType.ROOK, 1);
            rook.getSquare().setPiece(null);
            Square rookSquare = board.getSquare(FILE_H, board.isMoverColor() ? RANK_1 : RANK_8);
            rook.setSquare(rookSquare);
            rookSquare.setPiece(rook);
        }
        if (piece.getType() == PieceType.KING && s1.getFile() == FILE_E && s2.getFile() == FILE_C) {
            Piece rook = board.getPiece(board.isMoverColor(), PieceType.ROOK, 0);
            rook.getSquare().setPiece(null);
            Square rookSquare = board.getSquare(FILE_A, board.isMoverColor() ? RANK_1 : RANK_8);
            rook.setSquare(rookSquare);
            rookSquare.setPiece(rook);
        }

        if (move.getPromotesTo() != null) {
            undoRemoval(piece, s1);

            // Revert board.addPiece()
            int colorIdx = promotedPiece.isColor() ? 0 : 1;
            int typeIdx = Piece.typeToIdx(promotedPiece.getType());
            int internalIdx = promotedPiece.getInternalIdx();
            board.getPieces()[colorIdx][typeIdx][internalIdx] = null;
        }
        piece.setMoved(pieceMovedState);
    }

    private void undoRemoval(Piece piece, Square sq) {
        // Reverse board.removePiece()
        piece.setCaptured(false);
        piece.setSquare(sq);
    }

}
