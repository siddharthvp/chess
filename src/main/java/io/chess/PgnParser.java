package io.chess;

import static io.chess.Constants.*;
import static io.chess.Constants.FILE_G;

public class PgnParser {
    private final Board board;
    public PgnParser(Board board) {
        this.board = board;
    }

    public Move parseNotation(String pgn, boolean moverColor) throws Exception {
        Move move = new Move();

        // Special case: castling
        if (pgn.startsWith("O-O")) {
            Piece king = board.getKing(moverColor);
            move.setPiece(king);
            move.setS1(king.getSquare());
            int rank = moverColor ? RANK_1 : RANK_8;
            if (pgn.equals("O-O-O")) {
                move.setQueenSideCastle(true);
                move.setS2(board.getSquare(FILE_C, rank));
            } else {
                move.setKingSideCastle(true);
                move.setS2(board.getSquare(FILE_G, rank));
            }
            return move;
        }

        // strip irrelevant data: check, checkmate, capture, good move, bad move
        pgn = pgn.replaceAll("[+#x!?]", "");

        // get piece type, then discard it pgn string
        PieceType type = Piece.typeFromPgnString(pgn);
        pgn = pgn.substring(type == PieceType.PAWN ? 0 : 1);

        // get pawn promotion data if present, then discard it from pgn string
        if (pgn.contains("=")) {
            // TODO: throw if promoting to king?
            move.setPromotesTo(Piece.typeFromPgnString(pgn.substring(pgn.indexOf('=') + 1)));
            pgn = pgn.replaceAll("=\\w", "");
        }

        // now, we should be left with just start square disambiguator and end square
        // end square: last 2 chars
        String s2 = pgn.substring(pgn.length() - 2);
        // start square disambiguator: whatever is before that
        String startFrom = pgn.substring(0, pgn.length() - 2);

        move.setS2(board.getSquare(s2));
        Piece piece = board.getPiece(moverColor, type, startFrom, board.getSquare(s2));
        // move.setLegal(true); // legality already checked in getPiece()
        move.setPiece(piece);
        move.setS1(piece.getSquare());

        return move;
    }
}
