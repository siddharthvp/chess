package io.chess;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Board board = new Board();
        Scanner in = new Scanner(System.in);
        System.out.print("White to play: ");
        while (in.hasNextLine()) {
            String move = in.nextLine();
            try {
                board.move(move);
                System.out.println(Visualisers.ascii(board));
                System.out.print((board.isMoverColor() ? "White" : "Black") + " to play: ");
            } catch (Exception e) {
                System.out.println("Failed to move " + move);
                e.printStackTrace(System.out);
            }
        }
    }
}
