package chess;

import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final Hashtable<ChessPosition, ChessPiece> pieces = new Hashtable<>();
    private static final boolean useSymbols = true;

    public static final int BOARD_SIZE = 8;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.equals(pieces, that.pieces);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pieces);
    }

    public ChessBoard() {}

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (piece == null || position == null) return;
        pieces.put(position, piece);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return pieces.get(position);
    }

    public ChessPiece removePiece(ChessPosition position) {
        return pieces.remove(position);
    }

    public Map<ChessPosition, ChessPiece> getPieces() {
        return pieces;
    }

    /**
     * Returns the first position of a king of the given color on the board.
     * returns null if no matching king is on the board.
     *
     * @param color The color of the king to search for.
     * @return The position of the king or null
     */
    public ChessPosition getKingSquare(ChessGame.TeamColor color) {
        ChessPiece king = new ChessPiece(color, ChessPiece.PieceType.KING);
        for (Map.Entry<ChessPosition, ChessPiece> entry : pieces.entrySet()) {
            ChessPiece piece = entry.getValue();
            if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == color) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Returns all spaces the given team could capture on.
     * @param color the color of the team
     * @return a collection of positions showing all squares
     * that could be captured on
     */
    public Collection<ChessPosition> getPositionsThreatenedByColor(ChessGame.TeamColor color) {
        HashSet<ChessMove> opponentMoves = new HashSet<>(getMovesForColor(color));
        HashSet<ChessPosition> threatenedPositions = new HashSet<>();
        for (ChessMove move : opponentMoves) {
            if (move.cannotCapture()) {
                threatenedPositions.add(move.getEndPosition());
            }
        }
        return threatenedPositions;
    }

    public Collection<ChessMove> getCaptures(ChessGame.TeamColor color) {
        HashSet<ChessMove> opponentMoves = new HashSet<>(getMovesForColor(color));
        opponentMoves.removeIf(move -> !move.isCapture());
        return opponentMoves;
    }

    public Collection<ChessPosition> getPositionsByColor(ChessGame.TeamColor color) {
        HashSet<ChessPosition> teamPositions = new HashSet<>();
        for (Map.Entry<ChessPosition, ChessPiece> entry : pieces.entrySet()) {
            if (entry.getValue().getTeamColor() == color) {
                teamPositions.add(entry.getKey());
            }
        }
        return teamPositions;
    }

    public Collection<ChessMove> getMovesForColor(ChessGame.TeamColor color) {
        HashSet<ChessMove> moves = new HashSet<>();
        for (ChessPosition position : getPositionsByColor(color)) {
            ChessPiece piece = getPiece(position);
            moves.addAll(piece.pieceMoves(this, position));
        }
        return moves;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        pieces.clear();
        addPiece(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(1, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1, 8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

        for (int i = 1; i <= BOARD_SIZE; i++) {
            addPiece(new ChessPosition(2, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }

        addPiece(new ChessPosition(8, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
    }

    public boolean isOnBoard(ChessPosition position) {
        int rank = position.getRank();
        int file = position.getFile();
        return rank >= 1 && rank <= BOARD_SIZE && file >= 1 && file <= BOARD_SIZE;
    }

    public String positionFenString() {
        StringBuilder board = new StringBuilder(fenRow(BOARD_SIZE));
        for (int rank = BOARD_SIZE - 1; rank > 0; rank--) {
            board.append('/');
            board.append(fenRow(rank));
        }
        return board.toString();
    }

    private String fenRow(int rank) {
        StringBuilder row = new StringBuilder();
        int numBlanks = 0;
        for (int file = 1; file <= BOARD_SIZE; file++) {
            ChessPiece piece = pieces.get(new ChessPosition(rank, file));
            if (piece == null) {
                numBlanks += 1;
                if (numBlanks == BOARD_SIZE) row.append(numBlanks);
            } else {
                if (numBlanks > 0) {
                    row.append(numBlanks);
                    numBlanks = 0;
                }
                row.append(piece);
            }
        }
        return row.toString();
    }

    public String prettyBoard() {
        ChessColor color = new ChessColor();
        String fileLabels = "   a  b  c  d  e  f  g  h \n";
        StringBuilder board = new StringBuilder();
        board.append(color).append(fileLabels);
        for (int rank = BOARD_SIZE; rank > 0; rank--) {
            board.append(color.noHighlight().noSquare());
            board.append(" ").append(rank).append(" ");
            for (int file = 1; file <= BOARD_SIZE; file++) {
                color = (rank + file) % 2 == 1 ? color.lightSquare() : color.darkSquare();
                ChessPosition square = new ChessPosition(rank, file);
                ChessPiece piece = pieces.get(square);
                String pieceString = " ";
                if (piece != null) {
                    pieceString = useSymbols ? piece.prettyString() : piece.toString();
                    color = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? color.lightPiece() : color.darkPiece();
                }
                board.append(color);
                board.append(" ").append(pieceString).append(" ");
            }
            board.append(color.noHighlight().noSquare().lightText());
            board.append(" ").append(rank).append(" ").append('\n');
        }
        board.append(fileLabels).append('\n');
        board.append("FEN: ").append(positionFenString()).append('\n').append(color.getResetString());
        return board.toString();
    }

    public void printBoard() {
        System.out.println(prettyBoard());
    }

    @Override
    public String toString() {
        return positionFenString();
    }
}
