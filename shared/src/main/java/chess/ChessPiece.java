package chess;

import java.util.*;

// moving logic out of here to make this class as straight forward as possible.

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    // private Collection<ChessMove> moves; // If I want to have the test cases pass and use "dumb" pieces this would
                                            // be useful
    private final ChessGame.TeamColor color;
    private final ChessPiece.PieceType type;


    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN,
        EN_PASSANT
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType pieceType) {
        color = pieceColor;
        type = pieceType;
    }

    public ChessPiece(char symbol) {
        color = (Character.isUpperCase(symbol))? ChessGame.TeamColor.WHITE: ChessGame.TeamColor.BLACK;
        type = switch (Character.toUpperCase(symbol)) {
            case 'K' -> PieceType.KING;
            case 'Q' -> PieceType.QUEEN;
            case 'B' -> PieceType.BISHOP;
            case 'N' -> PieceType.KNIGHT;
            case 'R' -> PieceType.ROOK;
            case 'P' -> PieceType.PAWN;
            default -> throw new RuntimeException("Unexpected symbol for piece");
        };
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ChessPiece that = (ChessPiece) obj;
        return (color == that.getTeamColor() && type == that.getPieceType());
    }

//    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) { // redundant method for tests
//        return moves;
//    }

//    public Collection<ChessMove> pieceMoves() {
//        return moves;
//    }

    public Collection<ChessMove> getPieceMoves(ChessBoard board, ChessPosition position) {
        return pieceMoves(board, position);
    }

//    public void setPieceMoves(Collection<ChessMove> moves) {
//        this.moves = moves;
//    }
//
//    public void addPieceMoves(Collection<ChessMove> moves) {
//        this.moves.addAll(moves);
//    }
//
//    public void addPieceMove(ChessMove move) {
//        this.moves.add(move);
//    }
//
//    public void resetPieceMoves() {
//        moves.clear();
//    }

    // Basic access methods
    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * @return The one-character code to represent this piece
     */
    public char getCode() {
        return switch (type) {
            case PieceType.PAWN   -> (color == ChessGame.TeamColor.WHITE) ? 'P' : 'p';
            case PieceType.ROOK   -> (color == ChessGame.TeamColor.WHITE) ? 'R' : 'r';
            case PieceType.KNIGHT -> (color == ChessGame.TeamColor.WHITE) ? 'N' : 'n';
            case PieceType.BISHOP -> (color == ChessGame.TeamColor.WHITE) ? 'B' : 'b';
            case PieceType.QUEEN  -> (color == ChessGame.TeamColor.WHITE) ? 'Q' : 'q';
            case PieceType.KING   -> (color == ChessGame.TeamColor.WHITE) ? 'K' : 'k';
            default -> ' ';
        };
    }

//    public String getSymbol() {
//        return switch (type) {
//            case PieceType.PAWN   -> (color == ChessGame.TeamColor.WHITE) ? "♙" : "♟";
//            case PieceType.ROOK   -> (color == ChessGame.TeamColor.WHITE) ? "♖" : "♜";
//            case PieceType.KNIGHT -> (color == ChessGame.TeamColor.WHITE) ? "♘" : "♞";
//            case PieceType.BISHOP -> (color == ChessGame.TeamColor.WHITE) ? "♗" : "♝";
//            case PieceType.QUEEN  -> (color == ChessGame.TeamColor.WHITE) ? "♕" : "♛";
//            case PieceType.KING   -> (color == ChessGame.TeamColor.WHITE) ? "♔" : "♚";
//            default -> " ";
//        };
//    }
//
//    public String getSymbol(boolean symbol) {
//        if (!symbol) return String.valueOf(getCode());
//        return switch (type) {
//            case PieceType.PAWN   -> (color == ChessGame.TeamColor.WHITE) ? "♟" : "♙";
//            case PieceType.ROOK   -> (color == ChessGame.TeamColor.WHITE) ? "♜" : "♖";
//            case PieceType.KNIGHT -> (color == ChessGame.TeamColor.WHITE) ? "♞" : "♘";
//            case PieceType.BISHOP -> (color == ChessGame.TeamColor.WHITE) ? "♝" : "♗";
//            case PieceType.QUEEN  -> (color == ChessGame.TeamColor.WHITE) ? "♛" : "♕";
//            case PieceType.KING   -> (color == ChessGame.TeamColor.WHITE) ? "♚" : "♔";
//            default -> " ";
//        };
//    }

    public String getSymbol(boolean symbol, boolean solid) {
        if (!symbol) return String.valueOf(getCode());
        return switch (type) {
            case PieceType.PAWN   -> (solid) ? "♟" : "♙";
            case PieceType.ROOK   -> (solid) ? "♜" : "♖";
            case PieceType.KNIGHT -> (solid) ? "♞" : "♘";
            case PieceType.BISHOP -> (solid) ? "♝" : "♗";
            case PieceType.QUEEN  -> (solid) ? "♛" : "♕";
            case PieceType.KING   -> (solid) ? "♚" : "♔";
            default -> " ";
        };
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        board.highlightPosition(position, ChessBoard.Highlight.SECONDARY);
        PieceType piece = board.getPiece(position).getPieceType();
        ArrayList <ChessMove> moves = new ArrayList<>();
        switch (piece) {
            case KING:
                moves.addAll(getKingMoves(board, position));
                break;
            case QUEEN:
                moves.addAll(getOrthogonalSlidePositions(board, position, 7));
                moves.addAll(getDiagonalSlidePositions(board, position, 7));
                break;
            case BISHOP:
                moves.addAll(getDiagonalSlidePositions(board, position, 7));
                break;
            case KNIGHT:
                moves.addAll(getKnightMoves(board, position));
                break;
            case ROOK:
                moves.addAll(getOrthogonalSlidePositions(board, position, 7));
                break;
            case PAWN:
                moves.addAll(getPawnMoves(board, position));
                break;
            case EN_PASSANT:
                break;
            default:
                throw new RuntimeException("Nonexistent Piece");
        }
        // board.prettyPrint();
        return moves;
    }

//    public Collection<ChessMove> pieceCaptures(ChessBoard board, ChessPosition position) {
//        Collection<ChessMove> moves = getPieceMoves(board, position);
//        moves.removeIf(move -> !move.isCapture);
//        return  moves;
//    }

    private Collection<ChessMove> getKingMoves(ChessBoard board, ChessPosition position) {
        int currRank = position.getRank();
        int currFile = position.getFile();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        boolean onHomeTile = currFile == 5 &&
                (
                        (color == ChessGame.TeamColor.WHITE && currRank == 1) ||
                        (color == ChessGame.TeamColor.BLACK && currRank == 8)
                );
        ArrayList <ChessMove> moves = new ArrayList<>();
        moves.addAll(getOrthogonalSlidePositions(board, position, 1));
        moves.addAll(getDiagonalSlidePositions(board, position, 1));
        if (onHomeTile) {
            boolean leftClear = true;
            boolean rightClear = true;
            for (int i = 2; i < 4; i++) {
                if (board.getPiece(new ChessPosition(currRank, i)) != null) leftClear = false;
            }
            for (int i = 6; i < 8; i++) {
                if (board.getPiece(new ChessPosition(currRank, i)) != null) rightClear = false;
            }
            ChessPiece leftCorner = board.getPiece(new ChessPosition(currRank, 1));
            ChessPiece rightCorner = board.getPiece(new ChessPosition(currRank, 1));
            if (leftCorner != null && leftClear && leftCorner.getPieceType()==PieceType.ROOK && leftCorner.getTeamColor()==color) {
                moves.add(
                        new ChessMove.MoveBuilder(position, new ChessPosition(currRank, 7))
                                .shortCastle()
                                .build()
                );
            }
            if (rightCorner != null && rightClear && rightCorner.getPieceType()==PieceType.ROOK && rightCorner.getTeamColor()==color) {
                moves.add(
                        new ChessMove.MoveBuilder(position, new ChessPosition(currRank, 3))
                                .longCastle()
                                .build()
                );
            }
        }
        return moves;
    }

    /**
     * Gets available pawn moves for a position.
     *
     * @param board         The current chess board
     * @param position      The starting position to calculate moves from
     * @return              A collection of ChessMoves available to a pawn in the position.
     */
    private Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition position) {
        int currRank = position.getRank();
        int currFile = position.getFile();
        PieceType [] promotionOptions = { PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.QUEEN};
        ChessGame.TeamColor color = board.getPieceColor(position);
        Vector<ChessPosition> availableCaptures = new Vector<>();
        int advanceDirection = (color == ChessGame.TeamColor.WHITE)? 1 : -1;
        Collection<ChessMove> moves = new HashSet<>();
        Collection<ChessMove> movesWithPromotion = new HashSet<>();

        ChessPosition adv1 = new ChessPosition(currRank + advanceDirection, currFile);
        ChessPosition adv2 = new ChessPosition(currRank + (2 * advanceDirection), currFile);
        availableCaptures.add( new ChessPosition(currRank + advanceDirection, currFile - 1));
        availableCaptures.add( new ChessPosition(currRank + advanceDirection, currFile + 1));

        if (adv1.isOnBoard() && board.getPiece(adv1) == null) {
            moves.add(new ChessMove(position, adv1, null));
            board.highlightPosition(adv1, ChessBoard.Highlight.PRIMARY);
            if (
                    (
                        (currRank == 2 && advanceDirection == 1) ||
                        (currRank == 7 && advanceDirection == -1)
                    )   &&
                    board.getPiece(adv1) == null &&
                    board.getPiece(adv2) == null
            ) {
                moves.add(new ChessMove.MoveBuilder(position, adv2).enPassant(adv1).build());
                board.highlightPosition(adv2, ChessBoard.Highlight.PRIMARY);
                board.highlightPosition(adv1, ChessBoard.Highlight.TERNARY);
            }
        }

        availableCaptures.removeIf(
                p -> p.isOffBoard() || board.getPiece(p) == null || board.getPiece(p).getTeamColor() == color
        );

        for (ChessPosition capture : availableCaptures) {
            board.highlightPosition(capture, ChessBoard.Highlight.NEGATIVE);
            moves.add(new ChessMove.MoveBuilder(position, capture).isCapture().build());
        }


        for (ChessMove move : moves) {
            int rank = move.getEndPosition().getRank();
            if ((color == ChessGame.TeamColor.WHITE && rank == 8) ||
                    (color == ChessGame.TeamColor.BLACK && rank == 1)) {
                for (PieceType promotion : promotionOptions) {
                    movesWithPromotion.add(
                            new ChessMove.MoveBuilder(move).withPromotion(promotion).build()
                    );
                }
            } else {
                movesWithPromotion.add(move);
            }
        }

        return movesWithPromotion;
    }

    /**
     * Gets available knight moves for a position.
     *
     * @param board         The current chess board
     * @param position      The starting position to calculate moves from
     * @return              A collection of ChessMoves available to a pawn in the position.
     */
    private Collection<ChessMove> getKnightMoves(ChessBoard board, ChessPosition position) {
        int currRank = position.getRank();
        int currFile = position.getFile();
        ChessGame.TeamColor color = board.getPieceColor(position);
        ArrayList <ChessMove> moves = new ArrayList<>();
        ChessPosition[] possPositions = {
                new ChessPosition(currRank + 2, currFile - 1),
                new ChessPosition(currRank + 2, currFile + 1),
                new ChessPosition(currRank + 1, currFile + 2),
                new ChessPosition(currRank - 1, currFile + 2),
                new ChessPosition(currRank - 2, currFile + 1),
                new ChessPosition(currRank - 2, currFile - 1),
                new ChessPosition(currRank - 1, currFile - 2),
                new ChessPosition(currRank + 1, currFile - 2)
        };
        for (ChessPosition nextPosition : possPositions) {
            if (nextPosition.getFile() > 8 || nextPosition.getFile() < 1) continue;
            if (nextPosition.getRank() > 8 || nextPosition.getRank() < 1) continue;
//            board.highlightPosition(nextPosition, ChessBoard.Highlight.PRIMARY);
            if (board.getPieceColor(nextPosition) == color) continue;
            if (board.getPiece(nextPosition) == null) {
                moves.add(new ChessMove(position, nextPosition, null));
                board.highlightPosition(nextPosition, ChessBoard.Highlight.PRIMARY);
                continue;
            }
            moves.add(new ChessMove(position, nextPosition, null));
            board.highlightPosition(nextPosition, ChessBoard.Highlight.NEGATIVE);
        }
        return moves;
    }

    /**
     * Gets available sliding moves in horizontal and vertical directions
     *
     * @param board         The current chess board
     * @param position      The starting position to calculate moves from
     * @param maxDist       Max number of squares the piece is allowed to move
     * @return              A collection of ChessMoves available to the position in the given direction.
     */
    private Collection<ChessMove> getOrthogonalSlidePositions(ChessBoard board, ChessPosition position, int maxDist) {
        ArrayList <ChessMove> moves = new ArrayList<>();
        moves.addAll(getSlideMoves(board, position, 1, 0, maxDist));
        moves.addAll(getSlideMoves(board, position, -1, 0, maxDist));
        moves.addAll(getSlideMoves(board, position, 0, 1, maxDist));
        moves.addAll(getSlideMoves(board, position, 0, -1, maxDist));
        return moves;
    }

    /**
     * Gets available sliding moves in diagonal directions
     *
     * @param board         The current chess board
     * @param position      The starting position to calculate moves from
     * @param maxDist       Max number of squares the piece is allowed to move
     * @return              A collection of ChessMoves available to the position in the given direction.
     */
    private Collection<ChessMove> getDiagonalSlidePositions(ChessBoard board, ChessPosition position, int maxDist) {
        ArrayList <ChessMove> moves = new ArrayList<>();
        moves.addAll(getSlideMoves(board, position, 1, 1, maxDist));
        moves.addAll(getSlideMoves(board, position, -1, 1, maxDist));
        moves.addAll(getSlideMoves(board, position, -1, -1, maxDist));
        moves.addAll(getSlideMoves(board, position, 1, -1, maxDist));
        return moves;
    }

//    /**
//     * Gets the available sliding moves in one direction
//     * <p>
//     * to find moves diagonally increasing in rank and decreasing in file, call getSlideMoves(board, position, 1, -1).
//     * @param board         The current chess board
//     * @param position      The starting position to calculate moves from
//     * @param rankIteration How many ranks over to move at a time (-1, 0, or 1)
//     * @param fileIteration How many ranks over to move at a time (-1, 0, or 1)
//     * @return              A collection of ChessMoves available to the position in the given direction.
//     */
//    private Collection<ChessMove> getSlideMoves(ChessBoard board, ChessPosition position, int rankIteration, int fileIteration) {
//        return getSlideMoves(board, position, rankIteration, fileIteration, 7, true);
//    }
//
//    /**
//     * Gets the available sliding moves in one direction
//     * <p>
//     * to find moves diagonally increasing in rank and decreasing in file, call getSlideMoves(board, position, 1, -1).
//     * @param board         The current chess board
//     * @param position      The starting position to calculate moves from
//     * @param rankIteration How many ranks over to move at a time (-1, 0, or 1)
//     * @param fileIteration How many ranks over to move at a time (-1, 0, or 1)
//     * @return              A collection of ChessMoves available to the position in the given direction.
//     */
//    private Collection<ChessMove> getSlideMoves(ChessBoard board, ChessPosition position, int rankIteration, int fileIteration, int maxDist) {
//        return getSlideMoves(board, position, rankIteration, fileIteration, maxDist, true);
//    }

    /**
     * Gets the available sliding moves in one direction
     * <p>
     * to find moves diagonally increasing in rank and decreasing in file, call getSlideMoves(board, position, 1, -1).
     *
     * @param board         The current chess board
     * @param position      The starting position to calculate moves from
     * @param rankIteration How many ranks over to move at a time (-1, 0, or 1)
     * @param fileIteration How many ranks over to move at a time (-1, 0, or 1)
     * @return A collection of ChessMoves available to the position in the given direction.
     */
    private Collection<ChessMove> getSlideMoves(ChessBoard board, ChessPosition position, int rankIteration, int fileIteration, int maxDist) {
        Collection<ChessPosition> availablePositions = getSlidePositions(board, position, rankIteration, fileIteration, maxDist);
        return getMovesFromPositions(position, availablePositions);
    }

    /**
     * Gets positions available by sliding in one direction
     * <p>
     * to find moves diagonally increasing in rank and decreasing in file, call getSlideMoves(board, position, 1, -1).
     *
     * @param board         The current chess board
     * @param position      The starting position to calculate moves from
     * @param rankIteration How many ranks over to move at a time (-1, 0, or 1)
     * @param fileIteration How many ranks over to move at a time (-1, 0, or 1)
     * @return A collection of ChessMoves available to the position in the given direction.
     */
    private Collection<ChessPosition> getSlidePositions(ChessBoard board, ChessPosition position, int rankIteration, int fileIteration, int maxDist) {
        int currRank = position.getRank();
        int currFile = position.getFile();
        HashSet<ChessPosition> availablePositions = new HashSet<>();
        int ranksToEdge = maxDist;
        int filesToEdge = maxDist;
        int distToEdge = maxDist;

        if (rankIteration != 0) {
            ranksToEdge = rankIteration == 1 ? (8 - currRank) : currRank - 1;
        }
        if (fileIteration != 0) {
            filesToEdge = fileIteration == 1 ? (8 - currFile) : currFile - 1;
        }
        distToEdge = Math.min(maxDist, Math.min(ranksToEdge, filesToEdge));

        int i = 1;
        while (i <= distToEdge) {
            ChessPosition nextPosition = new ChessPosition(currRank + (i * rankIteration), currFile + (i * fileIteration));
            // empty squares
            if (board.getPiece(nextPosition) == null){
                availablePositions.add(nextPosition);
                board.highlightPosition(nextPosition, ChessBoard.Highlight.PRIMARY);
            }
            // captures
            else if (board.getPiece(nextPosition).getTeamColor() != color) {
                availablePositions.add(nextPosition);
                board.highlightPosition(nextPosition, ChessBoard.Highlight.NEGATIVE);
                break; // Don't search past capture for sliding piece.
            } else {
                break;
            }
            i++;
        }
        return availablePositions;
    }

    private Collection<ChessMove> getMovesFromPositions(ChessPosition startPosition, Collection<ChessPosition> positions) {
        HashSet<ChessMove> moves = new HashSet<>();
        for (ChessPosition endPosition : positions) {
            moves.add(new ChessMove(startPosition, endPosition, null));
        }
        return moves;
    }

    public String toString() {
        return String.valueOf(getCode());
    }
}
