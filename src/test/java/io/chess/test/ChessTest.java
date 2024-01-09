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
        board.playMoves("""
            1. e4 e5 2. Nf3 Nc6 3. Bb5 a6 4. Ba4 Nf6 5. O-O Be7 6. Re1 b5 7. Bb3 O-O 8. c3 d6 9. h3 Na5 10. Bc2 c5 
            11. d4 Qc7 12. Nbd2 Bd7 13. Nf1 Nc4 14. b3 Nb6 15. Ne3 c4 16. Ba3 Rfe8 17. Qd2 Bf8 18. bxc4 Nxc4 19. Nxc4 
            bxc4 20. Rab1 Rab8 21. Rxb8 Rxb8 22. Bb4 h6 23. Rd1 a5 24. Ba3 Bc6 25. Qe2 exd4 26. Qxc4 dxc3 27. Qxc3 Rc8 
            28. Qd4 Bd5 29. Bb1 Be6 30. Qe3 Rb8 31. Nd4 Bd7 32. Rc1 Qb6 33. Qd2 Re8 34. Re1 a4 35. Bc2 Qb7 36. Qd3 Qc7 
            37. Qd2 Qb7 38. Qd3 Qc7 39. Qd2
        """);

        assertEquals("4rbk1/2qb1pp1/3p1n1p/8/p2NP3/B6P/P1BQ1PP1/4R1K1", board.fen());
    }

    @Test
    void testCarlsenVsAnandWorldChampionship2014Game7() throws Exception {
        Board board = new Board();
        board.playMoves("""
            1. e4 e5 2. Nf3 Nc6 3. Bb5 Nf6 4. O-O Nxe4 5. d4 Nd6 6. Bxc6 dxc6 7. dxe5 Nf5 8. Qxd8+ Kxd8 9. h3 Ke8
            10. Nc3 h5 11. Bf4 Be7 12. Rad1 Be6 13. Ng5 Rh6 14. g3 Bxg5 15. Bxg5 Rg6 16. h4 f6 17. exf6 gxf6 18. Bf4
            Nxh4 19. f3 Rd8 20. Kf2 Rxd1 21. Nxd1 Nf5 22. Rh1 Bxa2 23. Rxh5 Be6 24. g4 Nd6 25. Rh7 Nf7 26. Ne3 Kd8
            27. Nf5 c5 28. Ng3 Ne5 29. Rh8+ Rg8 30. Bxe5 fxe5 31. Rh5 Bxg4 32. fxg4 Rxg4 33. Rxe5 b6 34. Ne4 Rh4 35. Ke2
            Rh6 36. b3 Kd7 37. Kd2 Kc6 38. Nc3 a6 39. Re4 Rh2+ 40. Kc1 Rh1+ 41. Kb2 Rh6 42. Nd1 Rg6 43. Ne3 Rh6 44. Re7
            Rh2 45. Re6+ Kb7 46. Kc3 Rh4 47. Kb2 Rh2 48. Nd5 Rd2 49. Nf6 Rf2 50. Kc3 Rf4 51. Ne4 Rh4 52. Nf2 Rh2
            53. Rf6 Rh7 54. Nd3 Rh3 55. Kd2 Rh2+ 56. Rf2 Rh4 57. c4 Rh3 58. Kc2 Rh7 59. Nb2 Rh5 60. Re2 Rg5 61. Nd1 b5
            62. Nc3 c6 63. Ne4 Rh5 64. Nf6 Rg5 65. Re7+ Kb6 66. Nd7+ Ka5 67. Re4 Rg2+ 68. Kc1 Rg1+ 69. Kd2 Rg2+
            70. Ke1 bxc4 71. Rxc4 Rg3 72. Nxc5 Kb5 73. Rc2 a5 74. Kf2 Rh3 75. Rc1 Kb4 76. Ke2 Rc3 77. Nd3+ Kxb3
            78. Ra1 Kc4 79. Nf2 Kb5 80. Rb1+ Kc4 81. Ne4 Ra3 82. Nd2+ Kd5 83. Rh1 a4 84. Rh5+ Kd4 85. Rh4+ Kc5
            86. Kd1 Kb5 87. Kc2 Rg3 88. Ne4 Rg2+ 89. Kd3 a3 90. Nc3+ Kb6 91. Ra4 a2 92. Nxa2 Rg3+ 93. Kc2 Rg2+
            94. Kb3 Rg3+ 95. Nc3 Rh3 96. Rb4+ Kc7 97. Rg4 Rh7 98. Kc4 Rf7 99. Rg5 Kb6 100. Na4+ Kc7 101. Kc5 Kd7
            102. Kb6 Rf1 103. Nc5+ Ke7 104. Kxc6 Rd1 105. Rg6 Kf7 106. Rh6 Rg1 107. Kd5 Rg5+ 108. Kd4 Rg6 109. Rh1
            Rg2 110. Ne4 Ra2 111. Rf1+ Ke7 112. Nc3 Rh2 113. Nd5+ Kd6 114. Rf6+ Kd7 115. Nf4 Rh1 116. Rg6 Rd1+
            117. Nd3 Ke7 118. Ra6 Kd7 119. Ke4 Ke7 120. Rc6 Kd7 121. Rc1 Rxc1 122. Nxc1
        """);
        assertEquals("8/3k4/8/8/4K3/8/8/2N5", board.fen());
    }

    @Test
    void testCarlsenVsNepomniachtchiWorldChampionship2021Game6() throws Exception {
        Board board = new Board();
        board.playMoves("""
            1. d4 Nf6 2. Nf3 d5 3. g3 e6 4. Bg2 Be7 5. O-O O-O 6. b3 c5 7. dxc5 Bxc5 8. c4 dxc4 9. Qc2 Qe7 10. Nbd2
                    Nc6 11. Nxc4 b5 12. Nce5 Nb4 13. Qb2 Bb7 14. a3 Nc6 15. Nd3 Bb6 16. Bg5 Rfd8 17. Bxf6 gxf6 18. Rac1 Nd4
            19. Nxd4 Bxd4 20. Qa2 Bxg2 21. Kxg2 Qb7+ 22. Kg1 Qe4 23. Qc2 a5 24. Rfd1 Kg7 25. Rd2 Rac8 26. Qxc8 Rxc8
            27. Rxc8 Qd5 28. b4 a4 29. e3 Be5 30. h4 h5 31. Kh2 Bb2 32. Rc5 Qd6 33. Rd1 Bxa3 34. Rxb5 Qd7 35. Rc5 e5
            36. Rc2 Qd5 37. Rdd2 Qb3 38. Ra2 e4 39. Nc5 Qxb4 40. Nxe4 Qb3 41. Rac2 Bf8 42. Nc5 Qb5 43. Nd3 a3 44. Nf4
            Qa5 45. Ra2 Bb4 46. Rd3 Kh6 47. Rd1 Qa4 48. Rda1 Bd6 49. Kg1 Qb3 50. Ne2 Qd3 51. Nd4 Kh7 52. Kh2 Qe4
            53. Rxa3 Qxh4+ 54. Kg1 Qe4 55. Ra4 Be5 56. Ne2 Qc2 57. R1a2 Qb3 58. Kg2 Qd5+ 59. f3 Qd1 60. f4 Bc7 61. Kf2
            Bb6 62. Ra1 Qb3 63. Re4 Kg7 64. Re8 f5 65. Raa8 Qb4 66. Rac8 Ba5 67. Rc1 Bb6 68. Re5 Qb3 69. Re8 Qd5
            70. Rcc8 Qh1 71. Rc1 Qd5 72. Rb1 Ba7 73. Re7 Bc5 74. Re5 Qd3 75. Rb7 Qc2 76. Rb5 Ba7 77. Ra5 Bb6 78. Rab5
            Ba7 79. Rxf5 Qd3 80. Rxf7+ Kxf7 81. Rb7+ Kg6 82. Rxa7 Qd5 83. Ra6+ Kh7 84. Ra1 Kg6 85. Nd4 Qb7 86. Ra2 Qh1
            87. Ra6+ Kf7 88. Nf3 Qb1 89. Rd6 Kg7 90. Rd5 Qa2+ 91. Rd2 Qb1 92. Re2 Qb6 93. Rc2 Qb1 94. Nd4 Qh1 95. Rc7+
            Kf6 96. Rc6+ Kf7 97. Nf3 Qb1 98. Ng5+ Kg7 99. Ne6+ Kf7 100. Nd4 Qh1 101. Rc7+ Kf6 102. Nf3 Qb1 103. Rd7 Qb2+
            104. Rd2 Qb1 105. Ng1 Qb4 106. Rd1 Qb3 107. Rd6+ Kg7 108. Rd4 Qb2+ 109. Ne2 Qb1 110. e4 Qh1 111. Rd7+ Kg8
            112. Rd4 Qh2+ 113. Ke3 h4 114. gxh4 Qh3+ 115. Kd2 Qxh4 116. Rd3 Kf8 117. Rf3 Qd8+ 118. Ke3 Qa5 119. Kf2 Qa7+
            120. Re3 Qd7 121. Ng3 Qd2+ 122. Kf3 Qd1+ 123. Re2 Qb3+ 124. Kg2 Qb7 125. Rd2 Qb3 126. Rd5 Ke7 127. Re5+ Kf7
            128. Rf5+ Ke8 129. e5 Qa2+ 130. Kh3 (diagram) Qe6 131. Kh4 Qh6+ 132. Nh5 Qh7 133. e6 Qg6 134. Rf7 Kd8
            135. f5 Qg1 136. Ng7
        """);
        assertEquals("3k4/5RN1/4P3/5P2/7K/8/8/6q1", board.fen());
    }

    @Test
    void testCarlsenVsAnandWorldChampionship2014Game6() throws Exception {
        Board board = new Board();
        board.playMoves("""
            1. e4 e5 2. Nf3 Nc6 3. Bb5 Nf6 4. d3 Bc5 5. c3 0-0 6. 0-0 Re8 7. Re1 a6 8. Ba4 b5 9. Bb3 d6 10. Bg5 Be6
            11. Nbd2 h6 12. Bh4 Bxb3 13. axb3 Nb8 14. h3 Nbd7 15. Nh2 Qe7 16. Ndf1 Bb6 17. Ne3 Qe6 18. b4 a5 19. bxa5
            Bxa5 20. Nhg4 Bb6 21. Bxf6 Nxf6 22. Nxf6+ Qxf6 23. Qg4 Bxe3 24. fxe3 Qe7 25. Rf1 c5 26. Kh2 c4 27. d4 Rxa1
            28. Rxa1 Qb7 29. Rd1 Qc6 30. Qf5 exd4 31. Rxd4 Re5 32. Qf3 Qc7 33. Kh1 Qe7 34. Qg4 Kh7 35. Qf4 g6 36. Kh2
            Kg7 37. Qf3 Re6 38. Qg3 Rxe4 39. Qxd6 Rxe3 40. Qxe7 Rxe7 41. Rd5 Rb7 42. Rd6 f6 43. h4 Kf7 44. h5 gxh5
            45. Rd5 Kg6 46. Kg3 Rb6 47. Rc5 f5 48. Kh4 Re6 49. Rxb5 Re4+ 50. Kh3 Kg5 51. Rb8 h4 52. Rg8+ Kh5 53. Rf8 Rf4
             54. Rc8 Rg4 55. Rf8 Rg3+ 56. Kh2 Kg5 57. Rg8+ Kf4 58. Rc8 Ke3 59. Rxc4 f4 (diagram) 60. Ra4 h3 61. gxh3 Rg6
             62. c4 f3 63. Ra3+ Ke2 64. b4 f2 65. Ra2+ Kf3 66. Ra3+ Kf4 67. Ra8 Rg1
        """);
        assertEquals("R7/8/7p/8/1PP2k2/7P/5p1K/6r1", board.fen());
    }

    @Test
    void testReshkoVsKaminsky1972() throws Exception {
        // Features underpromotion to bishop
        assertPgnResultsInFen("""
            1.c4 Nf6 2.Nc3 e5 3.Nf3 Nc6 4.g3 d5 5.cxd5 Nxd5 6.Bg2 Nb6 7.O-O Be7 8.a4 a5 9.b3 O-O 10.Bb2 Be6 11.Nb5 Bf6
            12.Rc1 Qc8 13.d4 Rd8 14.e4 exd4 15.e5 Be7 16.Nfxd4 Nxd4 17.Bxd4 c6 18.Qe2 Nd5 19.Bb2 Nc7 20.Nd4 Bd5 21.f4
            Bxg2 22.Kxg2 Bb4 23.f5 c5 24.e6 cxd4 25.Qe5 Bc3 26.Bxc3 f6 27.Qe2 dxc3 28.Rxc3 Rd6 29.Rd1 Rc6 30.Rcd3 Ne8
            31.Qh5 Rc7 32.Rd7 Rxd7 33.exd7 Qd8 34.Qf3 Nc7 35.Qxb7 Kf8 36.Rc1 Qxd7 37.Rxc7 Qd2+ 38.Kf3 Qd1+ 39.Kf4 Qd4+
            40.Qe4 Qd6+ 41.Kg4 Re8 42.Qc4 Re4+ 43.Qxe4 Qxc7 44.b4 axb4 45.Qxb4+ Kg8 46.a5 h6 47.a6 Kh7 48.Qd4 Qa5 49.a7
            Qa6 50.Kh4 Qa2 51.h3 Qa5 52.Qd7 Qb4+ 53.Kh5 Qa3 54.Kg4 Qe3 55.h4 Qe4+ 56.Kh5 Qf3+ 57.g4 Qe4 58.Qf7 Qc6
            59.Qe7 Qd5 60.Qe8 Qb7 61.a8=B Qb3 62.Qd7 Qg8 63.Bd5 Qf8 64.Bf7 Kh8 65.Qe8 Qxe8 66.Bxe8 Kh7 67.Bf7 Kh8 68.Kg6
            h5 69.Kxh5 Kh7 70.Be8 Kg8 71.Kg6
        """, "4B1k1/6p1/5pK1/5P2/6PP/8/8/8");
    }

    @Test
    void testLaskerVsAlekhine1913() throws Exception {
        assertPgnResultsInFen("""
            1. d4 f5 2. e4 fxe4 3. Nc3 Nf6 4. f3 d5 5. fxe4 dxe4 6. Bg5 Bf5 7. Qe2 Nc6 8. Bxf6 exf6 9. O-O-O Bd6
            10. Nxe4 O-O 11. Nxd6 cxd6 12. Qf2 Qa5 13. Bc4+ Kh8 14. Ne2 Nb4 15. Bb3 Rac8 16. Nc3 Bg6 17. Rhf1 b5 18. Rd2
             Nd3+ 19. Rxd3 Bxd3 20. Rd1 b4 21. Rxd3 bxc3 22. Kb1 Rfe8 23. bxc3 Rxc3 24. Qd2 Rxb3+ 25. cxb3 Qf5 26. Kb2
             Qf1 27. Re3 Rxe3 28. Qxe3 Qxg2+ 29. Ka3 h6 30. Qe6 Qc6 31. h4 h5 32. Qf7 Qe4 33. Qf8+ Kh7 34. Qxd6 Qxh4
             35. d5 Qe4 36. Qc5 Qe5 37. b4 h4 38. d6 h3 39. Qc2+ f5 40. d7 h2 41. d8=Q h1=Q 42. Qc4 Qhe4 43. Qdg8+ Kh6
             44. Qa6+ Kg5 45. Qxa7 Qc3+ 46. Qb3 Qexb4#
        """, "8/Q5p1/8/5pk1/1q6/KQq5/P7/8");
    }

    @Test
    void testSeirawanVsJun1988() throws Exception {
        assertPgnResultsInFen("""
            1. d4 d5 2. c4 e6 3. Nf3 c5 4. cxd5 exd5 5. Bg5 Be7 6. Bxe7 Qxe7 7. dxc5 Qxc5 8. Nbd2 Nc6 9. Nb3 Qd6 10. g3
            Nf6 11. Bg2 O-O 12. O-O Be6 13. Rc1 Rac8 14. Nbd4 Nxd4 15. Nxd4 Qb4 16. Rxc8 Rxc8 17. b3 h6 18. e3 Bg4
            19. Qe1 Qa3 20. f3 Bd7 21. Qe2 a5 22. Rb1 h5 23. Bf1 g6 24. Rb2 b6 25. Rc2 Rxc2 26. Qxc2 Qd6 27. Qc3 Qe5
            28. Qe1 Ne8 29. Qd2 Ng7 30. f4 Qd6 31. Bg2 Ne6 32. Ne2 Nc7 33. h3 Qc5 34. Kh2 Bf5 35. Nd4 Be4 36. g4 hxg4
            37. hxg4 Nb5 38. Nxb5 Qxb5 39. g5 Bxg2 40. Kxg2 Qc5 41. Kf3 Qb5 42. Kf2 Qc5 43. Ke2 Kf8 44. Qb2 Qb5+ 45. Kd2
            Qb4+ 46. Kd1 Qe4 47. Qh8+ Ke7 48. Qf6+ Kf8 49. Qxb6 Qb1+ 50. Kd2 Qxa2+ 51. Ke1 Qa1+ 52. Ke2 Qb2+ 53. Kd3
            Qb1+ 54. Kd4 Qe4+ 55. Kc3 Qh1 56. Kb2 Qd1 57. Qc5+ Ke8 58. Qc3 Qe2+ 59. Kc1 Qb5 60. Kd2 Kd7 61. Ke1 Ke8 62.
            Kd2 Kd7 63. Qf6 Ke8 64. Qe5+ Kd7 65. Qc3 Ke8 66. Kc2 Kd7 67. Qd4 Ke8 68. Qc3 Ke7 69. Qf6+ Kf8 70. Qd8+ Kg7
            71. Qd6 Kg8 72. Qa3 Qb6 73. Kd3 Kg7 74. Qa1+ Kg8 75. Qc3 Qa6+ 76. Kd4 a4 77. bxa4 Qxa4+ 78. Kxd5 Qd7+
            79. Kc4 Qe6+ 80. Kd3 Qd5+ 81. Qd4 Qb3+ 82. Ke2 Qc2+ 83. Kf3 Qb1 84. Ke2 Qc2+ 85. Qd2 Qb1 86. Qd8+ Kg7
            87. Qd3 Qb7 88. Qd4+ Kg8 89. e4 Qb5+ 90. Kf2 Qb8 91. Ke3 Qb3+ 92. Qd3 Qb6+ 93. Kf3 Qg1 94. Qe3 Qf1+ 95. Kg3
            Kh7 96. Qf2 Qd3+ 97. Qf3 Qd2 98. Kg4 Qd4 99. Qe2 Kg8 100. Kf3 Qc3+ 101. Qe3 Qc2 102. Qd4 Qc1 103. Qe3 Qd1+
            104. Kf2 Qc2+ 105. Qe2 Qc1 106. Kg3 Qg1+ 107. Kf3 Qh1+ 108. Ke3 Qc1+ 109. Qd2 Qc5+ 110. Kf3 Qb5 111. Qd1 Qc6
            112. Qd8+ Kg7 113. Qd2 Kg8 114. Qd8+ Kg7 115. Ke3 Qc1+ 116. Qd2 Qg1+ 117. Kd3 Qb1+ 118. Ke3 Qg1+ 119. Ke2
            Qb1 120. Qc3+ Kg8 121. Qc8+ Kg7 122. Qc4 Kg8 123. e5 Qb2+ 124. Ke3 Qb6+ 125. Ke4 Qb7+ 126. Kd4 Qa7+ 127. Kd3
            Qa3+ 128. Ke4 Qa8+ 129. Ke3 Qa3+ 130. Kf2 Qb2+ 131. Kf3 Qa3+ 132. Ke2 Qb2+ 133. Kd3 Qa3+ 134. Ke4 Qa8+
            135. Qd5 Qa4+ 136. Qd4 Qa8+ 137. Ke3 Qa3+ 138. Qd3 Qc1+ 139. Qd2 Qa3+ 140. Kf2 Qb3 141. Qe3 Qc4 142. Kg3
            Qd5 143. Qf3 Qc4 144. f5 gxf5 145. Qxf5 Qc1 146. Qf4 Qg1+ 147. Kh3 Qh1+ 148. Kg4 Qd1+ 149. Qf3 Qd4+
            150. Kf5 Qd7+ 151. Ke4 Qc6+ 152. Kf4 Qc4+ 153. Qe4 Qf1+ 154. Qf3 Qc4+ 155. Kg3 Qd4 156. Qf4 Qg1+ 157. Kf3
            Qf1+ 158. Kg4 Qd1+ 159. Qf3 Qd4+ 160. Kf5 Qd7+ 161. Ke4 Qc6+ 162. Kf4 Qc4+ 163. Kg3 Qd4 164. Qe2 Qg1+
            165. Kh4 Qh1+ 166. Kg4 Qg1+ 167. Kf5 Qb1+ 168. Kf4 Qc1+ 169. Qe3 Qc4+ 170. Kg3 Qd5 171. g6 Qe6 172. gxf7+
            Kxf7 173. Qc5 Qa6 174. Qf2+ Ke7 175. Qf5 Qh6 176. Kg2 Qg7+ 177. Kh3 Qh6+ 178. Kg3 Qg7+ 179. Kh4 Qh6+
            180. Kg4 Qe6 181. Qxe6+ Kxe6 182. Kf4 Ke7 183. Kf5 Kf7 184. e6+ Ke7 185. Ke5 Ke8 186. Kf6 Kf8 187. Kf5 Ke7
            188. Ke5 Ke8 189. Kd6 Kd8 190. e7+ Ke8 191. Ke6
        """, "4k3/4P3/4K3/8/8/8/8/8");
    }

    void assertPgnResultsInFen(String pgn, String fen) throws Exception {
        Board board = new Board();
        board.playMoves(pgn);
        assertEquals(fen, board.fen());
    }
}
