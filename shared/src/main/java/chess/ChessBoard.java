package chess;

import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 */
public class ChessBoard {
    private Map<ChessPosition, ChessPiece> pieces = new Hashtable<>();
    private transient final Hashtable<ChessPosition, ChessColor.Highlight> highlights = new Hashtable<>();

    public static final int BOARD_SIZE = 8;

    public ChessBoard() {
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (piece == null || position == null) {
            return;
        }
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

    /**
     * Removes a chess piece from the chessboard
     *
     * @param position where to add the piece to remove from
     * @return The removed piece or null if no piece is at that position
     */
    public ChessPiece removePiece(ChessPosition position) {
        return pieces.remove(position);
    }

    /**
     * Gets a Map of all the occupied positions on the board
     *
     * @return a map from positions to pieces
     */
    public Map<ChessPosition, ChessPiece> getPieces() {
        return pieces;
    }

    /**
     * Sets all positions on the board using a Map from positions to pieces
     * Useful for copying a board state
     *
     * @param pieces a map of all occupied board positions and their pieces
     */
    public void setPieces(Map<ChessPosition, ChessPiece> pieces) {
        this.pieces = new Hashtable<>(pieces);
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
            if (piece.equals(king)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Returns all spaces the given team could capture on.
     *
     * @param color the color of the team
     * @return a collection of positions showing all squares
     * that could be captured on
     */
    public Collection<ChessPosition> getPositionsThreatenedByColor(ChessGame.TeamColor color) {
        HashSet<ChessMove> opponentMoves = new HashSet<>(getMovesForColor(color));
        HashSet<ChessPosition> threatenedPositions = new HashSet<>();
        for (ChessMove move : opponentMoves) {
            if (move.cannotCapture()) {
                continue;
            }
            threatenedPositions.add(move.getEndPosition());
        }
        return threatenedPositions;
    }

    /**
     * Returns the positions of all pieces of a given color.
     *
     * @param color the color of pieces to have their positions returned
     * @return all positions with pieces matching color
     */
    public Collection<ChessPosition> getPositionsByColor(ChessGame.TeamColor color) {
        HashSet<ChessPosition> teamPositions = new HashSet<>();
        for (Map.Entry<ChessPosition, ChessPiece> entry : pieces.entrySet()) {
            ChessPiece piece = entry.getValue();
            if (piece.getTeamColor() == color && piece.getPieceType() != ChessPiece.PieceType.EN_PASSANT) {
                teamPositions.add(entry.getKey());
            }
        }
        return teamPositions;
    }

    /**
     * All moves the pieces of a given side can potentially make.
     * The moves are not checked for legality but are decorated.
     *
     * @param color the color of the side to find moves for.
     * @return the moves color's side could make.
     */
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
        highlights.clear();
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

    /**
     * Checks if a position would be on the game board.
     *
     * @param position a position to check. ChessPositions aren't limited to the board.
     * @return whether the position is on or off of the board.
     */
    public boolean isOnBoard(ChessPosition position) {
        int rank = position.getRank();
        int file = position.getFile();
        return rank >= 1 && rank <= BOARD_SIZE && file >= 1 && file <= BOARD_SIZE;
    }

    /**
     * The placement data of a <a href="https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_NotationFEN">FEN</a> string.
     *
     * @return the placement data portion of a FEN string
     */
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
            if (piece == null || piece.getPieceType() == ChessPiece.PieceType.EN_PASSANT) {
                numBlanks += 1;
            } else {
                if (numBlanks > 0) {
                    row.append(numBlanks);
                    numBlanks = 0;
                }
                row.append(piece);
            }
        }
        if (numBlanks > 0) {
            row.append(numBlanks);
        }
        return row.toString();
    }

    /**
     * Changes the display color of a given position when output with prettyRows()
     *
     * @param position the position to update
     * @param color    the new color to use
     */
    public void setHighlight(ChessPosition position, ChessColor.Highlight color) {
        highlights.put(position, color);
    }

    /**
     * Removes all highlight information that would be shown with prettyRows()
     */
    public void resetHighlights() {
        highlights.clear();
    }

    public ChessColor.Highlight getHighlight(ChessPosition position) {
        return highlights.get(position);
    }

    @Override
    public String toString() {
        return positionFenString();
    }

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
}
