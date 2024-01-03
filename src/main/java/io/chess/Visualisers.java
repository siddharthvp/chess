package io.chess;

public class Visualisers {
    public static String ascii(Board board) {
        StringBuilder out = new StringBuilder();
        String rankSeparator = "\n  +" + "--+".repeat(8) + "\n";
        out.append(rankSeparator);
        for (int rank = 7; rank >= 0; rank--) {
            out.append((rank + 1)).append(" |");
            for (int file = 0; file <= 7; file++) {
                Square sq = board.getSquares()[file][rank];
                out.append(sq.isOccupied() ? sq.getPiece().fenString() : " ").append(" |");
            }
            out.append(rankSeparator);
        }
        out.append("   a  b  c  d  e  f  g  h  ");
        return out.toString();
    }

    public static String fen(Board board) {
        StringBuilder str = new StringBuilder();
        for (int rank = 7; rank >= 0; rank--) {
            int blanks = 0;
            for (int file = 0; file <= 7; file++) {
                Piece piece = board.getSquare(file, rank).getPiece();
                if (piece == null) {
                    blanks++;
                } else {
                    if (blanks != 0) {
                        str.append(blanks);
                        blanks = 0;
                    }
                    str.append(piece.fenString());
                }
            }
            if (blanks != 0) {
                str.append(blanks);
            }
            str.append("/");
        }
        String result = str.toString();
        return result.substring(0, result.length() - 1);
    }
}
