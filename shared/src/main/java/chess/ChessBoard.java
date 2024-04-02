package chess;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;

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

    private final ChessPiece[][] positions;
    private final Highlight[][] highlightedPositions;


    private ChessPosition enPassant = null;

//    public boolean isPrintSymbols() {
//        return printSymbols;
//    }

//    public void setPrintSymbols(boolean printSymbols) {
//        this.printSymbols = printSymbols;
//    }


    public ChessPiece[][] getPositions() {
        return positions;
    }

//    public Highlight[][] getHighlightedPositions() {
//        return highlightedPositions;
//    }



    public enum Highlight {
        PRIMARY,
        SECONDARY,
        TERNARY,
        NEGATIVE,
        NONE
    }


    public ChessBoard() {
        highlightedPositions = new Highlight[BOARD_SIZE][BOARD_SIZE];
        for (Highlight[] row : highlightedPositions) {
            Arrays.fill(row, Highlight.NONE);
        }
        positions = new ChessPiece[BOARD_SIZE][BOARD_SIZE];
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        if (enPassant == null) return Arrays.deepEquals(positions, that.positions);

        // En Passant is handled by placing an extra "Ghost Pawn" on the board
        // this lets the equivalence work as if it weren't there.
        ChessPiece ep = getPiece(enPassant);
        removePiece(enPassant);
        boolean returnVal = Arrays.deepEquals(positions, that.positions);
        addPiece(enPassant, ep);
        return returnVal;
    }

    @Override
    public int hashCode() {

        return Arrays.deepHashCode(positions);
    }


    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (piece != null && piece.getPieceType() == ChessPiece.PieceType.EN_PASSANT) enPassant = position;
        positions[position.getRank() - 1][position.getFile() - 1] = piece;
    }
    public ChessPiece removePiece(ChessPosition position) {
        ChessPiece piece = getPiece(position);
        positions[position.getRank() - 1][position.getFile() - 1] = null;
        return piece;
    }

    public void setBoard(ChessPiece[][] newPositions) {
        clearBoard();
        if (newPositions.length != positions.length) throw new RuntimeException("Wrong size");
        for (int i = 0; i < positions.length; i++) {
            if (newPositions[i].length != positions[i].length) throw new RuntimeException("Wrong size");
            for (int j = 0; j < positions[i].length; j++) {
                if (newPositions[i][j] == null) continue;
                positions[i][j] = new ChessPiece(newPositions[i][j].getTeamColor(), newPositions[i][j].getPieceType());
            }
        }
    }


    /**
     * Gets a chess piece on the chessboard
     *
     * @param position  The position to get the piece from
     * @return          Either the piece at the position, or null if no piece is at that position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return positions[position.getRank() - 1][position.getFile() - 1];
    }

    // FIXME this should be State's responsibility
    public Dictionary<ChessPosition, ChessPiece> getPieces() {
        Dictionary<ChessPosition, ChessPiece> pieces = new Hashtable<>() {};
        for (int i = 0; i < positions.length; i++) {
            for (int j = 0; j < positions[i].length; j++) {
                ChessPiece square = positions[i][j];
                if (square == null) continue;
                pieces.put(new ChessPosition(i + 1, j + 1), square);
            }
        }
        return pieces;
    }

    /**
     * Gets the color of a piece on the chessboard
     *
     * @param position  The position to get the piece from
     * @return          Either the color of the piece at the position, or null if no piece is at that position
     */
    public ChessGame.TeamColor getPieceColor(ChessPosition position) {
        if (getPiece(position) == null) return null;
        return positions[position.getRank() - 1][position.getFile() - 1].getTeamColor();
    }

    /**
     * Gets the color highlighting a position on the chessboard
     *
     * @param position  The position to get return the color from
     * @return          The Highlight object specifying the color
     */
    private Highlight getHighlight(ChessPosition position) {
        return highlightedPositions[position.getRank() - 1][position.getFile() - 1];
    }

    /**
     * Highlights the desired square on the board when printed.
     *
     * @param position      The position on the board to highlight
     * @param highlightType The color to highlight the square
     */
    public void highlightPosition(ChessPosition position, Highlight highlightType) {
        highlightedPositions[position.getRank() - 1][position.getFile() - 1] = highlightType;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        positions[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        positions[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        positions[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        positions[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        positions[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        positions[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        positions[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        positions[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        Arrays.fill(positions[6], new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));

        positions[5] = new ChessPiece[BOARD_SIZE];
        positions[4] = new ChessPiece[BOARD_SIZE];
        positions[3] = new ChessPiece[BOARD_SIZE];
        positions[2] = new ChessPiece[BOARD_SIZE];

        Arrays.fill(positions[1], new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        positions[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        positions[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        positions[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        positions[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        positions[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        positions[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        positions[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        positions[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);

        // Reset highlights as well
        resetHighlight();
    }

    public void clearBoard() {
        for (int i = 0; i < positions.length; i++) {
            positions[i] = new ChessPiece[BOARD_SIZE];
        }
    }

    /**
     * Removes all highlight marks from board
     */
    public void resetHighlight() {
        for (Highlight[] highlightedPosition : highlightedPositions) {
            Arrays.fill(highlightedPosition, Highlight.NONE);
        }
    }

    /**
     * Prints board to console with white pieces in capital letters
     */
    public void print() {
        StringBuilder board = new StringBuilder();
        boolean currentlyHighlighting = false;
        for (int i = 0; i < positions.length; i++) {
            System.out.print(BOARD_SIZE - i);
            System.out.print(' ');

            for (int j = 0; j < positions[i].length; j++) {
                ChessPosition position = new ChessPosition(BOARD_SIZE - i, j + 1);
                ChessPiece piece = getPiece(position);
                Highlight highlightColor = getHighlight(position);
                if (currentlyHighlighting) board.append(RESET_HIGHLIGHT);
                board.append('|');

                // set color according to highlights
                if (highlightColor != Highlight.NONE) {
                    String nextHighlightChar = switch (highlightColor) {
                        case PRIMARY   -> PRIMARY_START;
                        case SECONDARY -> SECONDARY_START;
                        case TERNARY   -> TERNARY_START;
                        case NEGATIVE  -> NEGATIVE_HIGHLIGHT;
                        default -> "\0";
                    };
                    currentlyHighlighting = true;
                    board.append(nextHighlightChar);
                }

                char nextChar = (piece == null)? ' ' : piece.getCode();
                board.append(nextChar);
            }
            if (currentlyHighlighting) board.append(RESET_HIGHLIGHT);
            board.append('|');
            board.append('\n');
        }
        board.append("   A B C D E F G H\n");
        System.out.println(board);
    }

    public void prettyPrint() {
        System.out.println(prettyToString());
    }

    public String prettyToString() {
        StringBuilder board = new StringBuilder();
        for (int i = 0; i < positions.length; i++) {
            board.append(BOARD_SIZE - i);
            board.append(' ');
            for (int j = 0; j < positions[i].length; j++) {
                ChessPosition position = new ChessPosition(BOARD_SIZE - i, j + 1);
                ChessPiece piece = getPiece(position);
                Highlight highlightColor = getHighlight(position);

                // set color according to highlights
                String nextHighlight;
                // Background
                if ((i + j) % 2 == 1) {
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

                String pieceSymbol = (piece == null)? " " : piece.getSymbol(printSymbols, true);

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
        StringBuilder board = new StringBuilder();
        for (int i = 0; i < positions.length; i++) {
            for (int j = 0; j < positions[i].length; j++) {
                ChessPosition position = new ChessPosition(BOARD_SIZE - i, j + 1);
                ChessPiece piece = getPiece(position);
                board.append('|');
                char nextChar = (piece == null)? ' ' : piece.getCode();
                board.append(nextChar);
            }
            board.append('|');
            board.append('\n');
        }
        board.append("   A B C D E F G H\n");
        return board.toString();
    }

}

