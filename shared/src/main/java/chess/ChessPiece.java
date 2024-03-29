package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final int BOARD_SIZE = 8;
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
        PAWN
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType pieceType) {
        color = pieceColor;
        type = pieceType;
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

    // Move methods
    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @param   board       The current board state
     * @param   myPosition  The position of the piece to calculate moves for
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        PieceType piece = board.getPiece(myPosition).getPieceType();
        ArrayList <ChessMove> moves = new ArrayList<ChessMove>();
        switch (piece) {
            case KING:
                moves.addAll(getOrthogonalSlideMoves(board, myPosition, 1));
                moves.addAll(getDiagonalSlideMoves(board, myPosition, 1));
                break;
            case QUEEN:
                moves.addAll(getOrthogonalSlideMoves(board, myPosition, BOARD_SIZE - 1));
                moves.addAll(getDiagonalSlideMoves(board, myPosition, BOARD_SIZE - 1));
                break;
            case BISHOP:
                moves.addAll(getDiagonalSlideMoves(board, myPosition, BOARD_SIZE - 1));
                break;
            case KNIGHT:
                moves.addAll(getKnightMoves(board, myPosition));
                break;
            case ROOK:
                moves.addAll(getOrthogonalSlideMoves(board, myPosition, BOARD_SIZE - 1));
                break;
            case PAWN:
                moves.addAll(getPawnMoves(board, myPosition));
                break;
            default:
                throw new RuntimeException("Nonexistent Piece");
        }
        board.printBoard();
        return moves;
    }

    // TODO: en pessant
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
        int advanceDirection = (color == ChessGame.TeamColor.WHITE)? 1 : -1;
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        HashSet<ChessPosition> availablePositions = new HashSet<ChessPosition>();

        // Captures
        availablePositions.addAll(getSlidePositions(board, position, advanceDirection, 1, 1, true));
        availablePositions.addAll(getSlidePositions(board, position, advanceDirection, -1, 1, true));
        // TODO: en pessant
        // en passant logic would replace this V
        availablePositions.removeIf(capturePosition -> board.getPiece(capturePosition) == null);
        board.resetHighlight();
        for (ChessPosition nextPosition : availablePositions) {
            board.highlightPosition(nextPosition, ChessBoard.Highlight.SECONDARY);
        }

        // starting jump
        if (position.getRank() == 1) {
            availablePositions.addAll(getSlidePositions(board, position, advanceDirection, 0, 2, false));
        }
        else {
            availablePositions.addAll(getSlidePositions(board, position, advanceDirection, 0, 1, false));
        }

        // give promotion options
        for (ChessPosition nextPosition : availablePositions) {
            int rank = nextPosition.getRank();
            if ((color == ChessGame.TeamColor.WHITE && rank == BOARD_SIZE) || (color == ChessGame.TeamColor.BLACK && rank == 1)) {
                for (PieceType promotion : promotionOptions) {
                    board.highlightPosition(nextPosition, ChessBoard.Highlight.TERNARY);
                    moves.add(new ChessMove(position, nextPosition, promotion));
                }
            } else {
                moves.add(new ChessMove(position, nextPosition, null));
            }
        }
        return moves;
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
        ArrayList <ChessMove> moves = new ArrayList<ChessMove>();
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
            if (nextPosition.getFile() > BOARD_SIZE || nextPosition.getFile() < 1) continue;
            if (nextPosition.getRank() > BOARD_SIZE || nextPosition.getRank() < 1) continue;
//            board.highlightPosition(nextPosition, ChessBoard.Highlight.PRIMARY);
            if (board.getPieceColor(nextPosition) == color) continue;
            if (board.getPiece(nextPosition) == null) {
                moves.add(new ChessMove(position, nextPosition, null));
                board.highlightPosition(nextPosition, ChessBoard.Highlight.PRIMARY);
                continue;
            }
            moves.add(new ChessMove(position, nextPosition, null));
            board.highlightPosition(nextPosition, ChessBoard.Highlight.SECONDARY);
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
    private Collection<ChessMove> getOrthogonalSlideMoves(ChessBoard board, ChessPosition position, int maxDist) {
        ArrayList <ChessMove> moves = new ArrayList<ChessMove>();
        moves.addAll(getSlideMoves(board, position, 1, 0, maxDist, true));
        moves.addAll(getSlideMoves(board, position, -1, 0, maxDist, true));
        moves.addAll(getSlideMoves(board, position, 0, 1, maxDist, true));
        moves.addAll(getSlideMoves(board, position, 0, -1, maxDist, true));
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
    private Collection<ChessMove> getDiagonalSlideMoves(ChessBoard board, ChessPosition position, int maxDist) {
        ArrayList <ChessMove> moves = new ArrayList<ChessMove>();
        moves.addAll(getSlideMoves(board, position, 1, 1, maxDist, true));
        moves.addAll(getSlideMoves(board, position, -1, 1, maxDist, true));
        moves.addAll(getSlideMoves(board, position, -1, -1, maxDist, true));
        moves.addAll(getSlideMoves(board, position, 1, -1, maxDist, true));
        return moves;
    }

    /**
     * Gets a available sliding moves in one direction
     * <p>
     * to find moves diagonally increasing in rank and decreasing in file, call getSlideMoves(board, position, 1, -1).
     * @param board         The current chess board
     * @param position      The starting position to calculate moves from
     * @param rankIteration How many ranks over to move at a time (-1, 0, or 1)
     * @param fileIteration How many ranks over to move at a time (-1, 0, or 1)
     * @return              A collection of ChessMoves available to the position in the given direction.
     */
    private Collection<ChessMove> getSlideMoves(
            ChessBoard board,
            ChessPosition position,
            int rankIteration,
            int fileIteration,
            int maxDist,
            boolean captures) {
        Collection<ChessPosition> availablePositions = getSlidePositions(board, position, rankIteration, fileIteration, maxDist, captures);
        return getMovesFromPositions(position, availablePositions);
    }

    /**
     * Gets positions available by sliding in one direction
     * <p>
     * to find moves diagonally increasing in rank and decreasing in file, call getSlideMoves(board, position, 1, -1).
     * @param board         The current chess board
     * @param position      The starting position to calculate moves from
     * @param rankIteration How many ranks over to move at a time (-1, 0, or 1)
     * @param fileIteration How many ranks over to move at a time (-1, 0, or 1)
     * @return              A collection of ChessMoves available to the position in the given direction.
     */
    private Collection<ChessPosition> getSlidePositions(
            ChessBoard board,
            ChessPosition position,
            int rankIteration,
            int fileIteration,
            int maxDist,
            boolean captures) {
        int currRank = position.getRank();
        int currFile = position.getFile();
        HashSet<ChessPosition> availablePositions = new HashSet<ChessPosition>();
        int ranksToEdge = maxDist;
        int filesToEdge = maxDist;
        int distToEdge = maxDist;

        if (rankIteration != 0) {
            ranksToEdge = rankIteration == 1 ? (BOARD_SIZE - currRank) : currRank - 1;
        }
        if (fileIteration != 0) {
            filesToEdge = fileIteration == 1 ? (BOARD_SIZE - currFile) : currFile - 1;
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
            else if (captures && board.getPiece(nextPosition).getTeamColor() != color) {
                availablePositions.add(nextPosition);
                board.highlightPosition(nextPosition, ChessBoard.Highlight.SECONDARY);
                break; // Don't search past capture for sliding piece.
            } else {
                break;
            }
            i++;
        }
        return availablePositions;
    }

    /**
     * Returns a collection of ChessMoves from a starting position and a collection of ChessPositions
     * <p>
     * does not handle promotions
     * @param startPosition The position the piece will move frome
     * @param positions     The collection of positions the piece can move to
     * @return              A collection of ChessMove objects
     */
    private Collection<ChessMove> getMovesFromPositions(
            ChessPosition startPosition,
            Collection<ChessPosition> positions) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        for (ChessPosition endPosition : positions) {
            moves.add(new ChessMove(startPosition, endPosition, null));
        }
        return moves;
    }
}
