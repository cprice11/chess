package chess;
import java.util.Arrays;


/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    ChessPiece[][] positions = new ChessPiece[8][8];
    private Highlight[][] highlightedPositions = new Highlight[8][8];

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.equals(positions, that.positions);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(positions);
    }

    public enum Highlight {
        PRIMARY,
        SECONDARY,
        TERNARY,
        NEGATIVE,
        NONE
    }

    private static final String RESET_HIGHLIGHT = "\u001B[0m";
    private static final String PRIMARY_START = "\u001B[44m";
    private static final String SECONDARY_START = "\u001B[42m";
    private static final String TERNARY_START = "\u001B[43m";
    private static final String NEGATIVE_HIGHLIGHT = "\u001B[41m";

    public ChessBoard() {
        // resetBoard();
        for (Highlight[] row : highlightedPositions) {
            Arrays.fill(row, Highlight.NONE);
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        positions[position.getRank() - 1][position.getFile() - 1] = piece;
        //printBoard();
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return positions[position.getRank() - 1][position.getFile() - 1];
    }

    /**
     * Gets the color of a piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the color of the piece at the position, or null if no piece is at that position
     */
    public ChessGame.TeamColor getColor(ChessPosition position) {
        if (getPiece(position) == null) return null;
        return positions[position.getRank() - 1][position.getFile() - 1].getTeamColor();
    }

    /**
     * Highlights the desired square on the board when printed.
     *
     * @param position      The position on the board to highlight
     * @param highlightType The color to highlight the square
     */
    public void highlightPosition(ChessPosition position, Highlight highlightType) {
        // Highlight highlighted = highlightedPositions[position.getColumn() - 1][position.getRow() -1];
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

        positions[5] = new ChessPiece[8];
        positions[4] = new ChessPiece[8];
        positions[3] = new ChessPiece[8];
        positions[2] = new ChessPiece[8];

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
    public void printBoard() {
        boolean currentlyHighlighting = false;
        for (int i = 0; i < positions.length; i++) {
            System.out.print(8 - i);
            System.out.print(' ');
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < positions[i].length; j++) {
                if (currentlyHighlighting) row.append(RESET_HIGHLIGHT);
                row.append('|');
                ChessPiece piece = positions[7 - i][j];

                // set color according to highlights
                Highlight highlightColor = highlightedPositions[7 - i][j];
                if (highlightColor == Highlight.PRIMARY) {
                    row.append(PRIMARY_START);
                    currentlyHighlighting = true;
                } else if (highlightColor == Highlight.SECONDARY) {
                    row.append(SECONDARY_START);
                    currentlyHighlighting = true;
                } else if (highlightColor == Highlight.TERNARY) {
                    row.append(TERNARY_START);
                    currentlyHighlighting = true;
                } else if (highlightColor == Highlight.NEGATIVE) {
                    row.append(NEGATIVE_HIGHLIGHT);
                    currentlyHighlighting = true;
                }


                if (piece == null) {
                    row.append(' ');
                    continue;
                }
                ChessGame.TeamColor color = piece.getTeamColor();
                switch (piece.getPieceType()) {
                    case ChessPiece.PieceType.PAWN:
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            row.append('P');
                        } else {
                            row.append('p');
                        }
                        break;
                    case ChessPiece.PieceType.ROOK:
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            row.append('R');
                        } else {
                            row.append('r');
                        }
                        break;
                    case ChessPiece.PieceType.KNIGHT:
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            row.append('N');
                        } else {
                            row.append('n');
                        }
                        break;
                    case ChessPiece.PieceType.BISHOP:
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            row.append('B');
                        } else {
                            row.append('b');
                        }
                        break;
                    case ChessPiece.PieceType.QUEEN:
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            row.append('Q');
                        } else {
                            row.append('q');
                        }
                        break;
                    case ChessPiece.PieceType.KING:
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            row.append('K');
                        } else {
                            row.append('k');
                        }
                        break;
                    default:
                        row.append(' ');

                }
            }
            if (currentlyHighlighting) row.append(RESET_HIGHLIGHT);
            row.append('|');
            System.out.println(row);
        }
        System.out.println("   A B C D E F G H\n");
//
//        for (int i = 0; i < positions.length; i++) {
//            System.out.print(8 - i);
//            System.out.print(' ');
//            StringBuilder row = new StringBuilder();
//            for (int j = 0; j < positions[i].length; j++) {
//                Highlight h = highlightedPositions[7 - i][j];
//                row.append('|');
//                if (h == Highlight.NONE) {
//                    row.append(' ');
//                } else if (h == Highlight.PRIMARY) {
//                    row.append('1');
//                } else if (h == Highlight.SECONDARY) {
//                    row.append('2');
//                } else if (h == Highlight.NEGATIVE){
//                    row.append('X');
//                } else System.out.println(h.toString());
//            }
//            row.append('|');
//            System.out.println(row);
//        }
//        System.out.println("   A B C D E F G H\n");
    }
}

