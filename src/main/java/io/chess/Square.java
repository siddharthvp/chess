package io.chess;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Square {
    private int rank;
    private int file;
    private Piece piece;

    public boolean isOccupied() {
        return piece != null;
    }

    public Square(int file, int rank) {
        this.file = file;
        this.rank = rank;
    }

    public String toString() {
        return "" + (char)('a' + file) + (1 + rank);
    }

}
