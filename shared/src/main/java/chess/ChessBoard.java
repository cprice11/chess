package chess;

import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private static final String DARK_SQUARE = "\u001b[48;2;119;73;54m";
    private static final String WARN = "\u001b[48;2;208;87;0m";
    private static final String PRIMARY = "\u001b[48;2;100;208;140m";
    private static final String SECONDARY = "\u001b[48;2;100;208;209m";
    private static final String TERNARY = "\u001b[48;2;100;140;209m";
    private static final String ERROR = "\u001b[48;2;255;105;105m";
    private static final String LIGHT_SQUARE = "\033[48;2;214;159;126m";
    private static final String DARK_PIECE = "\033[38;2;5;6;9m";
    private static final String LIGHT_PIECE = "\033[38;2;245;208;197m";
    private static final String RESET_CODE = "\033[0m";

    public static final int BOARD_SIZE = 8; // no magic numbers, If adding alternate rule sets, refactor to be set.
    private final HashMap<ChessPosition, ChessPiece> pieces = new HashMap<>();
    private final HashMap<ChessPosition, PAINT_COLOR> paintedSquares = new HashMap<>();

    private ChessGame.TeamColor turn = ChessGame.TeamColor.WHITE;

    private boolean whiteCanCastleShort;
    private boolean whiteCanCastleLong;
    private boolean blackCanCastleShort;
    private boolean blackCanCastleLong;

    private ChessPosition enPassantTarget; // The square jumped over
    private ChessPosition passedPawn; // The pawn that can be captured

    private int halfMoveClock = 0;
    private int fullMoveNumber = 1;

    public enum PAINT_COLOR {
        DARK_SQUARE,
        LIGHT_SQUARE,
        WARN,
        PRIMARY,
        SECONDARY,
        TERNARY,
        ERROR,

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.equals(pieces, that.pieces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieces);
    }


    public ChessBoard() {
        whiteCanCastleShort = whiteCanCastleLong = blackCanCastleShort = blackCanCastleLong = true;
    }

    public ChessBoard(ChessBoard board) {
        board.pieces.forEach(this::addPiece);
        this.turn = board.turn;
        this.whiteCanCastleShort = board.whiteCanCastleShort;
        this.whiteCanCastleLong = board.whiteCanCastleLong;
        this.blackCanCastleShort = board.blackCanCastleShort;
        this.blackCanCastleLong = board.blackCanCastleLong;
        this.enPassantTarget = board.enPassantTarget;
        this.passedPawn = board.passedPawn;
        this.halfMoveClock = board.halfMoveClock;
        this.fullMoveNumber = board.fullMoveNumber;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        pieces.put(position, piece);
    }

    /**
     * Removes a chess piece from the chessboard
     *
     * @param position which position to clear
     */
    public ChessPiece removePiece(ChessPosition position) {
        ChessPiece piece = getPiece(position);
        pieces.remove(position);
        return piece;
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
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() { // For alternate rules this might need to be overridden.
        pieces.clear();
        clearPaint();
        turn = ChessGame.TeamColor.WHITE;
        whiteCanCastleShort = whiteCanCastleLong = blackCanCastleShort = blackCanCastleLong = true;
        enPassantTarget = null;
        halfMoveClock = 0;
        fullMoveNumber = 1;

        addPiece(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(1, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1, 8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

        addPiece(new ChessPosition(8, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        for (int i = 1; i <= BOARD_SIZE; i++) {
            addPiece(new ChessPosition(2, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
    }

    public HashMap<ChessPosition, ChessPiece> getPieces(ChessGame.TeamColor color) {
        HashMap<ChessPosition, ChessPiece> teamPositions = new HashMap<>();
        pieces.forEach((position, piece) -> {
            if (piece.getTeamColor() == color) teamPositions.put(position, piece);
        });
        return teamPositions;
    }

    public Collection<ChessMove> getMovesFor(ChessGame.TeamColor color) {
        HashSet<ChessMove> moves = new HashSet<>();
        getPieces(color).forEach(((position, piece) ->
                moves.addAll(piece.pieceMoves(this, position))
        ));
        return moves;
    }

    public Collection<ChessPosition> getPositionsFor(ChessGame.TeamColor color) {
        HashSet<ChessPosition> positions = new HashSet<>();
        getPieces(color).forEach(((position, piece) ->
                positions.add(position)
        ));
        return positions;
    }

    public Collection<ChessMove> otherTeamsMoves(ChessGame.TeamColor color) {
        return (color == ChessGame.TeamColor.WHITE) ?
                getMovesFor(ChessGame.TeamColor.BLACK) :
                getMovesFor(ChessGame.TeamColor.WHITE);
    }

    public Collection<ChessPosition> positionsThreatenedBy(ChessGame.TeamColor color) {
        HashSet<ChessPosition> threatenedSpaces = new HashSet<>();
        HashMap<ChessPosition, ChessPiece> otherTeam = getPieces(color);
        otherTeam.forEach((position, piece) ->
                threatenedSpaces.addAll(piece.pieceThreats(this, position))
        );
//        paintSquares(threatenedSpaces, PAINT_COLOR.WARN);
        return threatenedSpaces;
    }

    public boolean isInCheck(ChessGame.TeamColor color) {
        Collection<ChessMove> otherTeamsMoves = otherTeamsMoves(color);
        return otherTeamsMoves.stream()
                .anyMatch(move -> move.isCapture() && move.getCapturedPiece().getPieceType() == ChessPiece.PieceType.KING);
    }

    public void clearEnPassant() {
        pieces.remove(enPassantTarget);
        enPassantTarget = null;
        passedPawn = null;
    }

    // Output methods
    public void printBoard() {
        StringBuilder boardString = new StringBuilder();
        boardString.append("    A  B  C  D  E  F  G  H\n");
        for (int rank = BOARD_SIZE; rank > 0; rank--) {
            boardString.append(' ').append(rank).append(' ');
            for (int file = 1; file <= BOARD_SIZE; file++) {
                ChessPosition position = new ChessPosition(rank, file);
                PAINT_COLOR squareColor = paintedSquares.get(position);
                if (squareColor == null) {
                    if ((rank + file) % 2 == 0) squareColor = PAINT_COLOR.DARK_SQUARE;
                    else squareColor = PAINT_COLOR.LIGHT_SQUARE;
                }
                boardString.append(getColorString(squareColor));
                ChessPiece piece = pieces.get(position);
                if (piece == null) boardString.append("   ");
                else {
                    String textColor = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? LIGHT_PIECE : DARK_PIECE;
                    boardString.append(" ").append(textColor).append(piece.symbol()).append(" ");
                }
            }
            boardString.append(RESET_CODE).append(' ').append(rank).append('\n');
        }
        boardString.append("    A  B  C  D  E  F  G  H");
        System.out.println(boardString);
//        System.out.println(getFEN());
    }

    public void paintMoves(Collection<ChessMove> moves) {
        for (ChessMove move : moves) {
            paintMove(move);
        }
    }

    public void paintMove(ChessMove move) {
        paintSquare(move.getStartPosition(), PAINT_COLOR.PRIMARY);
        ChessPosition end = move.getEndPosition();
        paintSquare(end, PAINT_COLOR.SECONDARY);
        if (move.isCapture()) paintSquare(end, PAINT_COLOR.WARN);
        if (move.isLeap()) paintSquare(move.getPositionSkippedByLeap(), PAINT_COLOR.TERNARY);
        if (move.isCastle()) paintSquare(move.getCastlingRookPosition(), PAINT_COLOR.TERNARY);
    }

    public void paintSquares(Collection<ChessPosition> squares, PAINT_COLOR color) {
        for (ChessPosition square : squares) {
            paintSquare(square, color);
        }
    }

    public void paintSquare(ChessPosition square, PAINT_COLOR color) {
        paintedSquares.put(square, color);
    }

    public void clearPaint() {
        paintedSquares.clear();
    }

    private String getColorString(PAINT_COLOR color) {
        return switch (color) {
            case DARK_SQUARE -> DARK_SQUARE;
            case LIGHT_SQUARE -> LIGHT_SQUARE;
            case WARN -> WARN;
            case PRIMARY -> PRIMARY;
            case SECONDARY -> SECONDARY;
            case TERNARY -> TERNARY;
            case ERROR -> ERROR;
        };
    }

    public String getFEN() {
        StringBuilder FEN = new StringBuilder(getFENBoard()).append(" ");
        FEN.append((turn == ChessGame.TeamColor.WHITE) ? "w" : "b").append(" ");
        StringBuilder castling = new StringBuilder();
        castling.append(whiteCanCastleShort ? "K" : "");
        castling.append(whiteCanCastleLong ? "Q" : "");
        castling.append(blackCanCastleShort ? "k" : "");
        castling.append(blackCanCastleLong ? "q" : "");
        if (castling.isEmpty()) castling.append("-");
        FEN.append(castling).append(" ");
        FEN.append((enPassantTarget == null) ? "-" : enPassantTarget).append(" ");
        FEN.append(halfMoveClock).append(" ");
        FEN.append(fullMoveNumber);
        return FEN.toString();
    }

    public String getFENBoard() {
        // Join each row with a '/'
        List<String> rows = new ArrayList<>(8);
        for (int i = BOARD_SIZE; i > 0; i--) {
            rows.add(getFENRow(i));
        }
        return String.join("/", rows);
    }

    public String getFENRow(int rank) {
        int numBlanks = 0;
        StringBuilder row = new StringBuilder();
        for (int i = 0; i < BOARD_SIZE; i++) {
            ChessPiece piece = getPiece(new ChessPosition(rank, i + 1));
            if (piece == null) numBlanks++;
            else {
                if (numBlanks != 0) row.append(numBlanks);
                numBlanks = 0;
                row.append(piece.getChar());
            }
        }
        if (numBlanks != 0) row.append(numBlanks);
        return row.toString();
    }

    // Getters
    public ChessGame.TeamColor getTurn() {
        return turn;
    }

    public void setTurn(ChessGame.TeamColor team) {
        this.turn = team;
    }

    public boolean whiteCanCastleShort() {
        return whiteCanCastleShort;
    }

    public void whiteCanCastleShort(boolean canCastle) {
        whiteCanCastleShort = canCastle;
    }

    public boolean whiteCanCastleLong() {
        return whiteCanCastleLong;
    }

    public void whiteCanCastleLong(boolean canCastle) {
        whiteCanCastleLong = canCastle;
    }

    public boolean blackCanCastleShort() {
        return blackCanCastleShort;
    }

    public void blackCanCastleShort(boolean canCastle) {
        blackCanCastleShort = canCastle;
    }

    public boolean blackCanCastleLong() {
        return blackCanCastleLong;
    }

    public void blackCanCastleLong(boolean canCastle) {
        blackCanCastleLong = canCastle;
    }

    public boolean canCastleShort(ChessGame.TeamColor color) {
        return (color == ChessGame.TeamColor.WHITE) ? whiteCanCastleShort : blackCanCastleShort;
    }

    public boolean canCastleLong(ChessGame.TeamColor color) {
        return (color == ChessGame.TeamColor.WHITE) ? whiteCanCastleLong : blackCanCastleLong;
    }

    public ChessPosition getEnPassantTarget() {
        return enPassantTarget;
    }

    public void setEnPassantTarget(ChessPosition passedSquare) {
        enPassantTarget = passedSquare;
    }

    public ChessPosition getPassedPawn() {
        return passedPawn;
    }

    public void setPassedPawn(ChessPosition passedPawn) {
        this.passedPawn = passedPawn;
    }

    public int getHalfMoveClock() {
        return halfMoveClock;
    }

    public int getFullMoveNumber() {
        return fullMoveNumber;
    }
}
