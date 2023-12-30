package io.chess.test;

import io.chess.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ChessTest {

    @Test
    void testKingCantMoveIntoCheck() throws Exception {
        Board board = new Board();
        board.playMoves("1. e4 a5 2. e5 Nf6 3. exf6 e5 4. Nf3");
        assertThrows(Exception.class, () -> board.move("Ke7"));
    }

    @Test
    void testPieceCantMoveExposingKingToCheck() throws Exception {
        Board board = new Board();
        board.playMoves("e4 e5 Nf3 Bb4");
        assertThrows(Exception.class, () -> board.move("d4"));
    }

    @Test
    void testWhiteCastlesKingSide() throws Exception {
        Board board = new Board();
        board.playMoves("e4 e5 Bc4 Bc5 Nf3 Nf6 O-O");
        assertEquals("rnbqk2r/pppp1ppp/5n2/2b1p3/2B1P3/5N2/PPPP1PPP/RNBQ1RK1", board.fen());
    }

    @Test
    void testWhiteCastlesQueenSide() throws Exception {
        Board board = new Board();
        board.playMoves("1. d4 d5 2. Bf4 Bf5 3. Nc3 Nc6 4. Qd2 Qd7 5. O-O-O");
        assertEquals("r3kbnr/pppqpppp/2n5/3p1b2/3P1B2/2N5/PPPQPPPP/2KR1BNR", board.fen());
    }

    @Test
    void testBlackCastlesKingSide() throws Exception {
        Board board = new Board();
        board.playMoves("1. e4 e5 2. Nf3 Nf6 3. Bc4 Bc5 4. Nc3 O-O");
        assertEquals("rnbq1rk1/pppp1ppp/5n2/2b1p3/2B1P3/2N2N2/PPPP1PPP/R1BQK2R", board.fen());
    }

    @Test
    void testBlackCastlesQueenSide() throws Exception {
        Board board = new Board();
        board.playMoves("1. d4 d5 2. Nc3 Nc6 3. Bf4 Bf5 4. Qd2 Qd7 5. Nf3 O-O-O");
        assertEquals("2kr1bnr/pppqpppp/2n5/3p1b2/3P1B2/2N2N2/PPPQPPPP/R3KB1R", board.fen());
    }

    @Test
    void testEnPassant() throws Exception {
        Board board = new Board();
        board.playMoves("1. e4 Nc6 2. e5 f5 3. exf6");
        assertEquals("r1bqkbnr/ppppp1pp/2n2P2/8/8/8/PPPP1PPP/RNBQKBNR", board.fen());
    }

    @Test
    void testCarlsenVsSoQatarMasters2015() throws Exception {
        Board board = new Board();
        board.playMoves("1. e4 e5 2. Nf3 Nc6 3. Bb5 a6 4. Ba4 Nf6 5. O-O Be7 6. Re1 b5 7. Bb3 O-O 8. c3 d6 9. h3 Na5 10. Bc2 c5 11. d4 Qc7 12. Nbd2 Bd7 13. Nf1 Nc4 14. b3 Nb6 15. Ne3 c4 16. Ba3 Rfe8 17. Qd2 Bf8 18. bxc4 Nxc4 19. Nxc4 bxc4 20. Rab1 Rab8 21. Rxb8 Rxb8 22. Bb4 h6 23. Rd1 a5 24. Ba3 Bc6 25. Qe2 exd4 26. Qxc4 dxc3 27. Qxc3 Rc8 28. Qd4 Bd5 29. Bb1 Be6 30. Qe3 Rb8 31. Nd4 Bd7 32. Rc1 Qb6 33. Qd2 Re8 34. Re1 a4 35. Bc2 Qb7 36. Qd3 Qc7 37. Qd2 Qb7 38. Qd3 Qc7 39. Qd2");
        assertEquals("4rbk1/2qb1pp1/3p1n1p/8/p2NP3/B6P/P1BQ1PP1/4R1K1", board.fen());
    }

    @Test
    void testCarlsenVsAnandWorldChampionship2014Game7() throws Exception {
        Board board = new Board();
        board.playMoves("1. e4 e5 2. Nf3 Nc6 3. Bb5 Nf6 4. O-O Nxe4 5. d4 Nd6 6. Bxc6 dxc6 7. dxe5 Nf5 8. Qxd8+ Kxd8 9. h3 Ke8 10. Nc3 h5 11. Bf4 Be7 12. Rad1 Be6 13. Ng5 Rh6 14. g3 Bxg5 15. Bxg5 Rg6 16. h4 f6 17. exf6 gxf6 18. Bf4 Nxh4 19. f3 Rd8 20. Kf2 Rxd1 21. Nxd1 Nf5 22. Rh1 Bxa2 23. Rxh5 Be6 24. g4 Nd6 25. Rh7 Nf7 26. Ne3 Kd8 27. Nf5 c5 28. Ng3 Ne5 29. Rh8+ Rg8 30. Bxe5 fxe5 31. Rh5 Bxg4 32. fxg4 Rxg4 33. Rxe5 b6 34. Ne4 Rh4 35. Ke2 Rh6 36. b3 Kd7 37. Kd2 Kc6 38. Nc3 a6 39. Re4 Rh2+ 40. Kc1 Rh1+ 41. Kb2 Rh6 42. Nd1 Rg6 43. Ne3 Rh6 44. Re7 Rh2 45. Re6+ Kb7 46. Kc3 Rh4 47. Kb2 Rh2 48. Nd5 Rd2 49. Nf6 Rf2 50. Kc3 Rf4 51. Ne4 Rh4 52. Nf2 Rh2 53. Rf6 Rh7 54. Nd3 Rh3 55. Kd2 Rh2+ 56. Rf2 Rh4 57. c4 Rh3 58. Kc2 Rh7 59. Nb2 Rh5 60. Re2 Rg5 61. Nd1 b5 62. Nc3 c6 63. Ne4 Rh5 64. Nf6 Rg5 65. Re7+ Kb6 66. Nd7+ Ka5 67. Re4 Rg2+ 68. Kc1 Rg1+ 69. Kd2 Rg2+ 70. Ke1 bxc4 71. Rxc4 Rg3 72. Nxc5 Kb5 73. Rc2 a5 74. Kf2 Rh3 75. Rc1 Kb4 76. Ke2 Rc3 77. Nd3+ Kxb3 78. Ra1 Kc4 79. Nf2 Kb5 80. Rb1+ Kc4 81. Ne4 Ra3 82. Nd2+ Kd5 83. Rh1 a4 84. Rh5+ Kd4 85. Rh4+ Kc5 86. Kd1 Kb5 87. Kc2 Rg3 88. Ne4 Rg2+ 89. Kd3 a3 90. Nc3+ Kb6 91. Ra4 a2 92. Nxa2 Rg3+ 93. Kc2 Rg2+ 94. Kb3 Rg3+ 95. Nc3 Rh3 96. Rb4+ Kc7 97. Rg4 Rh7 98. Kc4 Rf7 99. Rg5 Kb6 100. Na4+ Kc7 101. Kc5 Kd7 102. Kb6 Rf1 103. Nc5+ Ke7 104. Kxc6 Rd1 105. Rg6 Kf7 106. Rh6 Rg1 107. Kd5 Rg5+ 108. Kd4 Rg6 109. Rh1 Rg2 110. Ne4 Ra2 111. Rf1+ Ke7 112. Nc3 Rh2 113. Nd5+ Kd6 114. Rf6+ Kd7 115. Nf4 Rh1 116. Rg6 Rd1+ 117. Nd3 Ke7 118. Ra6 Kd7 119. Ke4 Ke7 120. Rc6 Kd7 121. Rc1 Rxc1 122. Nxc1");
        assertEquals("8/3k4/8/8/4K3/8/8/2N5", board.fen());
    }

    @Test
    void testCarlsenVsNepomniachtchiWorldChampionship2021Game6() throws Exception {
        Board board = new Board();
        board.playMoves("1. d4 Nf6 2. Nf3 d5 3. g3 e6 4. Bg2 Be7 5. O-O O-O 6. b3 c5 7. dxc5 Bxc5 8. c4 dxc4 9. Qc2 Qe7 10. Nbd2 Nc6 11. Nxc4 b5 12. Nce5 Nb4 13. Qb2 Bb7 14. a3 Nc6 15. Nd3 Bb6 16. Bg5 Rfd8 17. Bxf6 gxf6 18. Rac1 Nd4 19. Nxd4 Bxd4 20. Qa2 Bxg2 21. Kxg2 Qb7+ 22. Kg1 Qe4 23. Qc2 a5 24. Rfd1 Kg7 25. Rd2 Rac8 26. Qxc8 Rxc8 27. Rxc8 Qd5 28. b4 a4 29. e3 Be5 30. h4 h5 31. Kh2 Bb2 32. Rc5 Qd6 33. Rd1 Bxa3 34. Rxb5 Qd7 35. Rc5 e5 36. Rc2 Qd5 37. Rdd2 Qb3 38. Ra2 e4 39. Nc5 Qxb4 40. Nxe4 Qb3 41. Rac2 Bf8 42. Nc5 Qb5 43. Nd3 a3 44. Nf4 Qa5 45. Ra2 Bb4 46. Rd3 Kh6 47. Rd1 Qa4 48. Rda1 Bd6 49. Kg1 Qb3 50. Ne2 Qd3 51. Nd4 Kh7 52. Kh2 Qe4 53. Rxa3 Qxh4+ 54. Kg1 Qe4 55. Ra4 Be5 56. Ne2 Qc2 57. R1a2 Qb3 58. Kg2 Qd5+ 59. f3 Qd1 60. f4 Bc7 61. Kf2 Bb6 62. Ra1 Qb3 63. Re4 Kg7 64. Re8 f5 65. Raa8 Qb4 66. Rac8 Ba5 67. Rc1 Bb6 68. Re5 Qb3 69. Re8 Qd5 70. Rcc8 Qh1 71. Rc1 Qd5 72. Rb1 Ba7 73. Re7 Bc5 74. Re5 Qd3 75. Rb7 Qc2 76. Rb5 Ba7 77. Ra5 Bb6 78. Rab5 Ba7 79. Rxf5 Qd3 80. Rxf7+ Kxf7 81. Rb7+ Kg6 82. Rxa7 Qd5 83. Ra6+ Kh7 84. Ra1 Kg6 85. Nd4 Qb7 86. Ra2 Qh1 87. Ra6+ Kf7 88. Nf3 Qb1 89. Rd6 Kg7 90. Rd5 Qa2+ 91. Rd2 Qb1 92. Re2 Qb6 93. Rc2 Qb1 94. Nd4 Qh1 95. Rc7+ Kf6 96. Rc6+ Kf7 97. Nf3 Qb1 98. Ng5+ Kg7 99. Ne6+ Kf7 100. Nd4 Qh1 101. Rc7+ Kf6 102. Nf3 Qb1 103. Rd7 Qb2+ 104. Rd2 Qb1 105. Ng1 Qb4 106. Rd1 Qb3 107. Rd6+ Kg7 108. Rd4 Qb2+ 109. Ne2 Qb1 110. e4 Qh1 111. Rd7+ Kg8 112. Rd4 Qh2+ 113. Ke3 h4 114. gxh4 Qh3+ 115. Kd2 Qxh4 116. Rd3 Kf8 117. Rf3 Qd8+ 118. Ke3 Qa5 119. Kf2 Qa7+ 120. Re3 Qd7 121. Ng3 Qd2+ 122. Kf3 Qd1+ 123. Re2 Qb3+ 124. Kg2 Qb7 125. Rd2 Qb3 126. Rd5 Ke7 127. Re5+ Kf7 128. Rf5+ Ke8 129. e5 Qa2+ 130. Kh3 (diagram) Qe6 131. Kh4 Qh6+ 132. Nh5 Qh7 133. e6 Qg6 134. Rf7 Kd8 135. f5 Qg1 136. Ng7");
        assertEquals("3k4/5RN1/4P3/5P2/7K/8/8/6q1", board.fen());
    }
}
