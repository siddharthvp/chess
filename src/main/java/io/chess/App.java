package io.chess;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Board board = new Board();
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String move = in.nextLine();
            try {
                board.move(move);
                System.out.println(board.visualize());
            } catch (Exception e) {
                System.out.println("Failed to move " + move);
                e.printStackTrace(System.out);
            }
        }
    }
}
