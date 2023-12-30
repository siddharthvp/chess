package io.chess;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Square implements Cloneable {
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

    @Override
    public Square clone() {
        try {
            Square clone = (Square) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
