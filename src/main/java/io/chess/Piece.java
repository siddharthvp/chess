package io.chess;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Piece {
    private boolean captured = false;
    private boolean moved = false;
    private Square square;
    private boolean color;
    private PieceType type;
    private int internalIdx;

    public Piece(boolean color, PieceType type) {
        this.color = color;
        this.type = type;
    }

    public static int typeToIdx(PieceType type) {
        return switch (type) {
            case PAWN -> 0;
            case ROOK -> 1;
            case KNIGHT -> 2;
            case BISHOP -> 3;
            case QUEEN -> 4;
            case KING -> 5;
        };
    }

    public String fenString() {
        String piece = switch (this.type) {
            case PAWN -> "p";
            case ROOK -> "r";
            case KNIGHT -> "n";
            case BISHOP -> "b";
            case QUEEN -> "q";
            case KING -> "k";
        };
        if (this.isColor()) {
            piece = piece.toUpperCase();
        }
        return piece;
    }

    public String pgnString() {
        return switch (this.type) {
            case PAWN -> "";
            case ROOK -> "R";
            case KNIGHT -> "N";
            case BISHOP -> "B";
            case QUEEN -> "Q";
            case KING -> "K";
        };
    }

    public static PieceType typeFromPgnString(String pgn) {
        return switch (pgn.substring(0, 1)) {
            case "K" -> PieceType.KING;
            case "Q" -> PieceType.QUEEN;
            case "R" -> PieceType.ROOK;
            case "N" -> PieceType.KNIGHT;
            case "B" -> PieceType.BISHOP;
            default -> PieceType.PAWN;
        };
    }


    public String toString() {
        return (color ? "White" : "Black") + " " +
                type.toString().toLowerCase() +
                (internalIdx > 0 ? (" (" + internalIdx + ")") : "");
    }

}
