package io.chess;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.chess.Constants.*;

@Getter
@Setter
public class Board {

    // 1. file
    // 2. rank
    private final Square[][] squares = new Square[8][8];

    // 1. Color (0 - white, 1 - black)
    // 2. Piece type (0 - pawn, 1 - rook, 2 - knight, 3 - bishop, 4 - queen, 5 - king)
    // 3. Piece index for given color and type (max 10 pieces of the same color + type)
    private final Piece[][][] pieces = new Piece[2][6][10];

    private int pawnDoubleMovedFile = -1;

    public static final String LEGAL = "LEGAL";

    public Piece getPiece(boolean color, PieceType type, int internalIdx) {
        int colorIdx = color ? 0 : 1;
        int typeIdx = Piece.typeToIdx(type);
        return pieces[colorIdx][typeIdx][internalIdx];
    }

    public Piece getPiece(boolean color, PieceType type, String startFrom, Square s2) throws Exception {
        int colorIdx = color ? 0 : 1;
        int typeIdx = Piece.typeToIdx(type);
        Piece[] candidates = pieces[colorIdx][typeIdx];

        // optimisation: diagonal pawn moves always use disambiguator, so lack of disambiguator implies straight move
        // (no perf improvement seen for a 136-move game, though)
        if (type == PieceType.PAWN && startFrom.isEmpty()) {
            startFrom = "" + (char)('a' + s2.getFile());
        }

        String finalStartFrom = startFrom;
        List<Piece> candidatePieces = Arrays.stream(candidates)
                // filter out non-existent pieces
                .map(Optional::ofNullable)
                .filter(Optional::isPresent)
                .map(Optional::get)

                // filter out captured pieces
                .filter(p -> !p.isCaptured())

                // filter out pieces not on specified start rank or file
                .filter(p -> {
                    switch (finalStartFrom.length()) {
                        case 0:
                            return true;
                        case 1:
                            char start = finalStartFrom.charAt(0);
                            if (start >= 'a' && start <= 'h') { // it's a file
                                int startFile = start - 'a';
                                return p.getSquare().getFile() == startFile;
                            } else { // if start >= '1' && start <= '8'
                                int startRank = start - '1';
                                return p.getSquare().getRank() == startRank;
                            }
                        case 2:
                            return p.getSquare() == getSquare(finalStartFrom);
                        default:
                            throw new RuntimeException("Unexpected startFrom: " + finalStartFrom);
                    }
                })
                .collect(Collectors.toList());

        // If there are multiple candidate pieces,
        // select the one that can reach s2
        return candidatePieces.stream().filter(p -> {
            Move move = new Move();
            move.setPiece(p);
            move.setS1(p.getSquare());
            move.setS2(s2);
            return checkMoveLegality(move, color).equals(LEGAL);
        }).findFirst().orElseThrow(() -> new Exception("no candidate moves"));
    }

    public Square getSquare(int file, int rank) {
        return squares[file][rank];
    }

    public Square getSquare(String notation) {
        char file = notation.charAt(0);
        char rank = notation.charAt(1);
        return getSquare(file - 'a', rank - '1');
    }

    public Piece getKing(boolean color) {
        return getPiece(color, PieceType.KING, 0);
    }

    public void addPiece(Piece piece, Square square) {
        piece.setSquare(square);
        square.setPiece(piece);
        int colorIdx = piece.isColor() ? 0 : 1;
        int typeIdx = Piece.typeToIdx(piece.getType());
        Piece[] piecesOfTypeAndColor = pieces[colorIdx][typeIdx];
        for (int i = 0; i < 10; i++) {
            if (piecesOfTypeAndColor[i] == null) {
                piece.setInternalIdx(i);
                pieces[colorIdx][typeIdx][i] = piece;
                break;
            }
        }
    }

    public void removePiece(Piece piece) {
        piece.setCaptured(true);
        piece.setSquare(null);
    }

    private PgnParser pgnParser = new PgnParser(this);

    // true -> white, false -> black. White starts first.
    @Getter
    private boolean moverColor = true;

    public void move(String notation) throws Exception {
        if (notation.contains("-") && !notation.startsWith("O-O")) {
            Move move = new Move();
            String[] moveParts = notation.split("-");
            move.setS1(getSquare(moveParts[0]));
            move.setS2(getSquare(moveParts[1]));
            move.setPiece(getSquare(moveParts[0]).getPiece());
            move(move);
        } else {
            move(pgnParser.parseNotation(notation, moverColor));
        }
    }

    public void playMoves(String moves) throws Exception {
        // strip move numbers
        moves = moves.replaceAll("\\d+\\.", "");
        // strip comments
        moves = moves.replaceAll("\\{.*?\\}", "");
        moves = moves.replaceAll("\\(.*?\\)", "");

        // alternative castle notation
        moves = moves.replaceAll("0-0", "O-O");
        moves = moves.replaceAll("0-0-0", "O-O-O");

        String[] pgns = moves.split("\\s+");
        for (String pgn : pgns) {
            if (pgn.isEmpty()) continue;
            try {
                move(pgn);
            } catch (Exception e) {
                System.out.println("Failed moving " + pgn);
                throw e;
            }
            // System.out.println(visualize());
        }
    }

    public void move(Move move) throws Exception {
        Piece piece = move.getPiece();
        Square s1 = move.getS1();
        Square s2 = move.getS2();
        String moveLegalityCheckResult = checkMoveLegality(move, moverColor);
        if (moveLegalityCheckResult.equals(LEGAL)) {
            new PieceMove(this, move).play();
        } else {
            throw new Exception(moveLegalityCheckResult);
        }

        // For en passant validation
        pawnDoubleMovedFile = (piece.getType() == PieceType.PAWN && Math.abs(s2.getRank() - s1.getRank()) == 2)
                ? s1.getFile() : -1;

        // Next player's turn
        moverColor = !moverColor;
    }
    
    public Movement computePath(Square s1, Square s2) {
        Movement movement = new Movement();
        int startFile = s1.getFile();
        int endFile = s2.getFile();
        int diffFiles = Math.abs(startFile - endFile);
        int startRank = s1.getRank();
        int endRank = s2.getRank();
        int diffRanks = Math.abs(startRank - endRank);

        if (diffFiles == 0 && diffRanks == 0) {
            return null;

        } else if (diffFiles == 0 || diffRanks == 0) {
            movement.setStraightMove(true);
            int diff = diffFiles == 0 ? diffRanks : diffFiles;
            movement.setNumSteps(diff);
            Square[] path = new Square[diff + 1];
            if (diffFiles == 0) {
                boolean movingUp = endRank > startRank;
                for (int rank = startRank; movingUp ? rank <= endRank : rank >= endRank; rank += (movingUp ? 1 : -1)) {
                    path[Math.abs(rank - startRank)] = squares[startFile][rank];
                }
            }
            if (diffRanks == 0) {
                boolean movingRight = endFile > startFile;
                for (int file = startFile; movingRight ? file <= endFile : file >= endFile; file += (movingRight ? 1 : -1)) {
                    path[Math.abs(file - startFile)] = squares[file][startRank];
                }
            }
            movement.setPath(path);

        } else if (diffFiles == diffRanks) {
            movement.setDiagonalMove(true);
            movement.setNumSteps(diffFiles);
            Square[] path = new Square[diffFiles + 1];
            boolean movingUp = endRank > startRank;
            boolean movingRight = endFile > startFile;
            for (int i = 0; i <= diffFiles; i++) {
                path[i] = squares[movingRight ? startFile + i : startFile - i][movingUp ? startRank + i : startRank - i];
            }
            movement.setPath(path);

        } else if ((diffFiles == 1 && diffRanks == 2) || (diffFiles == 2 && diffRanks == 1)) {
            movement.setKnightMove(true);
            Square[] path = new Square[2];
            path[0] = s1;
            path[1] = s2;
            movement.setPath(path);

        } else {
            return null;
        }

        return movement;
    }

    public boolean isPathBlocked(Square[] path) {
        Square s1 = path[0];
        Square s2 = path[path.length - 1];
        for (Square sq : path) {
            if (sq == s1) continue;
            if (sq == s2) break;
            if (sq.isOccupied()) {
                return true;
            }
        }
        return false;
    }

    public String checkMoveLegality(Move move, boolean mover) {
        if (move.isLegal()) return LEGAL; // was already checked
        String initialCheckResult = checkMoveLegalityInternal(move, mover);
        if (!initialCheckResult.equals(LEGAL)) return initialCheckResult;

        PieceMove fakeMove = new PieceMove(this, move);
        fakeMove.play();
        if (isKingChecked(mover, getKing(mover).getSquare())) {
            fakeMove.revert();
            return "king is in check";
        } else {
            fakeMove.revert();
            return LEGAL;
        }
    }

    private String checkMoveLegalityInternal(Move move, boolean mover) {
        Piece p = move.getPiece();
        Square s1 = move.getS1();
        Square s2 = move.getS2();

        // Check if piece belongs to player
        if (p.isColor() != mover) {
            return "other player's turn";
        }
        // Check if piece is on the square
        if (!p.getSquare().equals(s1)) {
            return "piece not on source square";
        }
        // Check that not trying to capture own piece
        if (s2.isOccupied() && s2.getPiece().isColor() == mover) {
            return "can't capture own piece";
        }
        // Find path to the square
        Movement movement = computePath(s1, s2);
        if (movement == null) return "forbidden move";
        if (p.getType() == PieceType.KNIGHT && !movement.isKnightMove()) return "invalid knight move";
        if (p.getType() != PieceType.KNIGHT && movement.isKnightMove()) return "invalid piece move";
        if (p.getType() == PieceType.KING) {
            if (movement.getNumSteps() > 2) return "invalid king move";
            if (movement.getNumSteps() == 2) {
                int castleRank = mover ? RANK_1 : RANK_8;
                if (s1.getRank() != castleRank || s2.getRank() != castleRank) return "invalid king move";
                if (s1.getFile() != FILE_E) return "invalid king move";
                if ((s2.getFile() != FILE_C || !isCastlePossible(mover, PieceType.QUEEN)) &&
                        (s2.getFile() != FILE_G || !isCastlePossible(mover, PieceType.KING))) {
                    return "invalid king move";
                }
            }
        }
        if (p.getType() == PieceType.PAWN) {
            if (movement.isStraightMove() && s2.isOccupied()) return "invalid pawn move";
            if (movement.isStraightMove() && s1.getFile() != s2.getFile()) return "pawn can't move horizontally";
            if (movement.isDiagonalMove() && !s2.isOccupied()) {
                if (pawnDoubleMovedFile != s2.getFile() || s2.getRank() != (moverColor ? RANK_6 : RANK_3)) {
                    return "invalid pawn move";
                }
            }
            if (p.isMoved()) {
                if (movement.getNumSteps() > 1) return "invalid pawn move";
            } else {
                if (movement.getNumSteps() > 2) return "invalid pawn move";
                if (movement.getNumSteps() == 2 && movement.isDiagonalMove()) return "invalid pawn move";
            }
        }
        if (p.getType() == PieceType.BISHOP && !movement.isDiagonalMove()) return "invalid bishop move";
        if (p.getType() == PieceType.ROOK && !movement.isStraightMove()) return "invalid rook move";

        Square[] path = movement.getPath();
        if (path == null) {
            return "no valid path to destination square";
        }
        if (isPathBlocked(path)) {
            return "path blocked";
        }

        return LEGAL;
    }

    public boolean isKingChecked(boolean kingColor, Square sq) {
        boolean enemyColor = !kingColor;
        Piece[][] enemyPieces = getPieces()[enemyColor ? 0 : 1];
        for (Piece[] pieces : enemyPieces) {
            for (Piece piece : pieces) {
                if (piece == null || piece.isCaptured()) {
                    continue;
                }
                // Can this piece reach square?
                Move move = new Move();
                move.setPiece(piece);
                move.setS1(piece.getSquare());
                move.setS2(sq);
                if (checkMoveLegalityInternal(move, !kingColor).equals(LEGAL)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCastlePossible(boolean color, PieceType side) {
        Piece king = getKing(color);
        if (king.isMoved()) return false;
        Piece rook = getPiece(color, PieceType.ROOK, side == PieceType.QUEEN ? 0 : 1);
        if (rook.isCaptured()) return false;
        if (rook.isMoved()) return false;
        if (isKingChecked(color, king.getSquare())) return false;

        int startFile = side == PieceType.QUEEN ? FILE_B : FILE_F;
        int endFile = side == PieceType.QUEEN ? FILE_D : FILE_G;

        // Check if path is clear
        for (int file = startFile; file <= endFile; file++) {
            Square sq = getSquare(file, color ? RANK_1 : RANK_8);
            if (sq.isOccupied() || isKingChecked(color, sq)) {
                return false;
            }
        }
        return true;
    }


    public Board() {
        // Initialize squares
        for (int rank = 0; rank <= 7; rank++) {
            for (int file = 0; file <= 7; file++) {
                squares[file][rank] = new Square(file, rank);
            }
        }

        // Rooks
        addPiece(new Piece(true, PieceType.ROOK), getSquare(FILE_A, RANK_1));
        addPiece(new Piece(true, PieceType.ROOK), getSquare(FILE_H, RANK_1));
        addPiece(new Piece(false, PieceType.ROOK), getSquare(FILE_A, RANK_8));
        addPiece(new Piece(false, PieceType.ROOK), getSquare(FILE_H, RANK_8));

        // Knights
        addPiece(new Piece(true, PieceType.KNIGHT), getSquare(FILE_B, RANK_1));
        addPiece(new Piece(true, PieceType.KNIGHT), getSquare(FILE_G, RANK_1));
        addPiece(new Piece(false, PieceType.KNIGHT), getSquare(FILE_G, RANK_8));
        addPiece(new Piece(false, PieceType.KNIGHT), getSquare(FILE_B, RANK_8));

        // Bishops
        addPiece(new Piece(true, PieceType.BISHOP), getSquare(FILE_C, RANK_1));
        addPiece(new Piece(true, PieceType.BISHOP), getSquare(FILE_F, RANK_1));
        addPiece(new Piece(false, PieceType.BISHOP), getSquare(FILE_F, RANK_8));
        addPiece(new Piece(false, PieceType.BISHOP), getSquare(FILE_C, RANK_8));

        // Queens
        addPiece(new Piece(true, PieceType.QUEEN), getSquare(FILE_D, RANK_1));
        addPiece(new Piece(false, PieceType.QUEEN), getSquare(FILE_D, RANK_8));

        // Kings
        addPiece(new Piece(true, PieceType.KING), getSquare(FILE_E, RANK_1));
        addPiece(new Piece(false, PieceType.KING), getSquare(FILE_E, RANK_8));

        // Pawns
        addPiece(new Piece(true, PieceType.PAWN), getSquare(FILE_A, RANK_2));
        addPiece(new Piece(true, PieceType.PAWN), getSquare(FILE_B, RANK_2));
        addPiece(new Piece(true, PieceType.PAWN), getSquare(FILE_C, RANK_2));
        addPiece(new Piece(true, PieceType.PAWN), getSquare(FILE_D, RANK_2));
        addPiece(new Piece(true, PieceType.PAWN), getSquare(FILE_E, RANK_2));
        addPiece(new Piece(true, PieceType.PAWN), getSquare(FILE_F, RANK_2));
        addPiece(new Piece(true, PieceType.PAWN), getSquare(FILE_G, RANK_2));
        addPiece(new Piece(true, PieceType.PAWN), getSquare(FILE_H, RANK_2));

        addPiece(new Piece(false, PieceType.PAWN), getSquare(FILE_A, RANK_7));
        addPiece(new Piece(false, PieceType.PAWN), getSquare(FILE_B, RANK_7));
        addPiece(new Piece(false, PieceType.PAWN), getSquare(FILE_C, RANK_7));
        addPiece(new Piece(false, PieceType.PAWN), getSquare(FILE_D, RANK_7));
        addPiece(new Piece(false, PieceType.PAWN), getSquare(FILE_E, RANK_7));
        addPiece(new Piece(false, PieceType.PAWN), getSquare(FILE_F, RANK_7));
        addPiece(new Piece(false, PieceType.PAWN), getSquare(FILE_G, RANK_7));
        addPiece(new Piece(false, PieceType.PAWN), getSquare(FILE_H, RANK_7));
    }


    public String visualize() {
        StringBuilder out = new StringBuilder();
        String rankSeparator = "\n  +" + "--+".repeat(8) + "\n";
        out.append(rankSeparator);
        for (int rank = 7; rank >= 0; rank--) {
            out.append((rank + RANK_2)).append(" |");
            for (int file = 0; file <= 7; file++) {
                Square sq = squares[file][rank];
                out.append(sq.isOccupied() ? sq.getPiece().fenString() : " ").append(" |");
            }
            out.append(rankSeparator);
        }
        out.append("   a  b  c  d  e  f  g  h  ");
        return out.toString();
    }

    public String fen() {
        StringBuilder str = new StringBuilder();
        for (int rank = 7; rank >= 0; rank--) {
            int blanks = 0;
            for (int file = 0; file <= 7; file++) {
                Piece piece = getSquare(file, rank).getPiece();
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
