package chess;

import java.util.*;
import java.util.stream.IntStream;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private static final int BOARD_SIZE = 8;
    private static final String RESET_HIGHLIGHT = "\u001B[0m";
    private static final String PRIMARY_START = "\u001B[44m";
    private static final String SECONDARY_START = "\u001B[42m";
    private static final String TERNARY_START = "\u001B[43m";
    private static final String NEGATIVE_HIGHLIGHT = "\u001B[41m";

    private static final String LIGHT_PIECE = "\033[38;5;15m";
    private static final String DARK_PIECE = "\033[38;5;16m";
    private static final String DARK_SQUARE = "\033[48;5;22m";
    private static final String LIGHT_SQUARE = "\033[48;5;65m";
    private static final String PRIMARY_DARK_SQUARE = "\033[48;5;38m";
    private static final String PRIMARY_LIGHT_SQUARE = "\033[48;5;44m";
    private static final String SECONDARY_DARK_SQUARE = "\033[48;5;34m";
    private static final String SECONDARY_LIGHT_SQUARE = "\033[48;5;40m";
    private static final String TERNARY_DARK_SQUARE = "\033[48;5;214m";
    private static final String TERNARY_LIGHT_SQUARE = "\033[48;5;220m";
    private static final String NEGATIVE_DARK_SQUARE = "\033[48;5;124m";
    private static final String NEGATIVE_LIGHT_SQUARE = "\033[48;5;160m";

    public boolean printSymbols = true;

    private final HashMap<ChessPosition, ChessPiece> positions;
    private final HashMap<ChessPosition, Highlight> highlightedPositions;

    private ChessPosition enPassant = null;
    public HashMap<ChessPosition, ChessPiece> getPositions() {
        return positions;
    }
    public enum Highlight {
        PRIMARY,
        SECONDARY,
        TERNARY,
        NEGATIVE,
        NONE
    }


    public ChessBoard() {
        highlightedPositions = new HashMap<>();
        positions = new HashMap<>();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        if (enPassant == null) return positions.equals(that.positions);

        // En Passant is handled by placing an extra "Ghost Pawn" on the board
        // this lets the equivalence work as if it weren't there.
        ChessPiece ep = getPiece(enPassant);
        removePiece(enPassant);
        boolean returnVal = positions.equals(that.positions);
        addPiece(enPassant, ep);
        return returnVal;
    }

    @Override
    public int hashCode() {
        return positions.hashCode();
    }


    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (piece != null && piece.getPieceType() == ChessPiece.PieceType.EN_PASSANT) enPassant = position;
        positions.put(position, piece);
    }

    public ChessPiece removePiece(ChessPosition position) {
        return positions.remove(position);
    }

    public void setBoard(HashMap<ChessPosition, ChessPiece> newPositions) {
        clearBoard();
        positions.putAll(newPositions);
    }


    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return positions.get(position);
    }

    // If I ever refactor this. This should be state's responsibility.
    public HashMap<ChessPosition, ChessPiece> getPieces() {
        return new HashMap<>(positions);
    }

    /**
     * Gets the color of a piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the color of the piece at the position, or null if no piece is at that position
     */
    public ChessGame.TeamColor getPieceColor(ChessPosition position) {
        if (getPiece(position) == null) return null;
        return positions.get(position).getTeamColor();
    }

    /**
     * Gets the color highlighting a position on the chessboard
     *
     * @param position The position to get return the color from
     * @return The Highlight object specifying the color
     */
    private Highlight getHighlight(ChessPosition position) {
        return highlightedPositions.get(position);
    }

    /**
     * Highlights the desired square on the board when printed.
     *
     * @param position      The position on the board to highlight
     * @param highlightType The color to highlight the square
     */
    public void highlightPosition(ChessPosition position, Highlight highlightType) {
        highlightedPositions.put(position, highlightType);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        positions.put(new ChessPosition(8, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        positions.put(new ChessPosition(8, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        positions.put(new ChessPosition(8, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        positions.put(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        positions.put(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        positions.put(new ChessPosition(8, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        positions.put(new ChessPosition(8, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        positions.put(new ChessPosition(8, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));

        for (int i = 1; i <= 8; i++) {
            positions.put(new ChessPosition(7, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }

        for (int i = 1; i <= 8; i++) {
            positions.put(new ChessPosition(2, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }

        positions.put(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        positions.put(new ChessPosition(1, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        positions.put(new ChessPosition(1, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        positions.put(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        positions.put(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        positions.put(new ChessPosition(1, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        positions.put(new ChessPosition(1, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        positions.put(new ChessPosition(1, 8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

        // Reset highlights as well
        resetHighlight();
    }

    public void clearBoard() {
        positions.clear();
    }

    /**
     * Removes all highlight marks from board
     */
    public void resetHighlight() {
        highlightedPositions.clear();
    }

    /**
     * Prints board to console with white pieces in capital letters
     */
    public void print() {
        StringBuilder board = new StringBuilder();
        boolean currentlyHighlighting = false;
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print(BOARD_SIZE - i);
            System.out.print(' ');

            for (int j = 0; j < BOARD_SIZE; j++) {
                ChessPosition position = new ChessPosition(BOARD_SIZE - i, j + 1);
                ChessPiece piece = getPiece(position);
                Highlight highlightColor = getHighlight(position);
                if (currentlyHighlighting) board.append(RESET_HIGHLIGHT);
                board.append('|');

                // set color according to highlights
                if (highlightColor != Highlight.NONE) {
                    String nextHighlightChar = switch (highlightColor) {
                        case PRIMARY -> PRIMARY_START;
                        case SECONDARY -> SECONDARY_START;
                        case TERNARY -> TERNARY_START;
                        case NEGATIVE -> NEGATIVE_HIGHLIGHT;
                        default -> "\0";
                    };
                    currentlyHighlighting = true;
                    board.append(nextHighlightChar);
                }

                char nextChar = (piece == null) ? ' ' : piece.getCode();
                board.append(nextChar);
            }
            if (currentlyHighlighting) board.append(RESET_HIGHLIGHT);
            board.append('|');
            board.append('\n');
        }
        board.append("   A B C D E F G H\n");
        System.out.println(board);
    }

    public String prettyToString() {
        StringBuilder board = new StringBuilder();
        for (int i = 0; i < BOARD_SIZE; i++) {
            board.append(BOARD_SIZE - i);
            board.append(' ');
            for (int j = 0; j < BOARD_SIZE; j++) {
                ChessPosition position = new ChessPosition(BOARD_SIZE - i, j + 1);
                ChessPiece piece = getPiece(position);
                Highlight highlightColor = getHighlight(position);

                // set color according to highlights
                String nextHighlight;
                // Background
                if (highlightColor == null || highlightColor.equals(Highlight.NONE)) {
                    nextHighlight = ((i + j) % 2 == 1) ? DARK_SQUARE : LIGHT_SQUARE;
                } else if ((i + j) % 2 == 1) {
                    nextHighlight = switch (highlightColor) {
                        case PRIMARY -> PRIMARY_DARK_SQUARE;
                        case SECONDARY -> SECONDARY_DARK_SQUARE;
                        case TERNARY -> TERNARY_DARK_SQUARE;
                        case NEGATIVE -> NEGATIVE_DARK_SQUARE;
                        case NONE -> DARK_SQUARE;
                    };
                } else {
                    nextHighlight = switch (highlightColor) {
                        case PRIMARY -> PRIMARY_LIGHT_SQUARE;
                        case SECONDARY -> SECONDARY_LIGHT_SQUARE;
                        case TERNARY -> TERNARY_LIGHT_SQUARE;
                        case NEGATIVE -> NEGATIVE_LIGHT_SQUARE;
                        case NONE -> LIGHT_SQUARE;
                    };
                }
                // Foreground
                if (piece != null) {
                    nextHighlight += (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? LIGHT_PIECE : DARK_PIECE;
                }
                board.append(nextHighlight);

                String pieceSymbol = (piece == null) ? " " : piece.getSymbol(printSymbols, true);

                String nextSquare = " " + pieceSymbol + " ";
                board.append(nextSquare);
                board.append(RESET_HIGHLIGHT);
            }
            board.append('\n');
        }
        board.append("   A  B  C  D  E  F  G  H\n");
        return board.toString();
    }

    public String toString() {
        FENParser fen = new FENParser();
        return fen.getBoardFen(this);
    }
}

