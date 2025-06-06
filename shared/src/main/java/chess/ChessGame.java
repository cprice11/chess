package chess;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 */
public class ChessGame {
    private ChessBoard board = new ChessBoard();
    private ArrayList<ChessMove> moveHistory = new ArrayList<>();
    private TeamColor turn = TeamColor.WHITE;
    private boolean whiteCanShortCastle = true;
    private boolean whiteCanLongCastle = true;
    private boolean blackCanShortCastle = true;
    private boolean blackCanLongCastle = true;
    private ChessPosition enPassant = null;
    private transient ChessPosition positionVulnerableToEnPassant = null;
    private int halfMoveClock = 0;
    private int fullMoveNumber = 1;
    private final static Gson GSON = new Gson();

    public ChessGame() {
        board.resetBoard();
//        ConsolePrinter.printGame(this);
    }

    public ChessGame(ChessBoard board, ArrayList<ChessMove> moveHistory, TeamColor turn,
                     boolean whiteShort, boolean whiteLong, boolean blackShort, boolean blackLong,
                     ChessPosition enPassant, int halfMoveClock, int fullMoveNumber) {
        this.board = board;
        this.moveHistory = moveHistory;
        this.turn = turn;
        this.whiteCanShortCastle = whiteShort;
        this.whiteCanLongCastle = whiteLong;
        this.blackCanShortCastle = blackShort;
        this.blackCanLongCastle = blackLong;
        this.enPassant = enPassant;
        this.halfMoveClock = halfMoveClock;
        this.fullMoveNumber = fullMoveNumber;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        board.resetHighlights();
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        moves.removeIf(move -> {
            try {
                validateMove(move);
                return false;
            } catch (InvalidMoveException e) {
                return true;
            }
        });
        for (ChessMove move : moves) {
            board.setHighlight(move.getEndPosition(), ChessColor.Highlight.PRIMARY);
        }
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        board.resetHighlights();
        validateMove(move);
        TeamColor color = board.getPiece(move.getStartPosition()).getTeamColor();
        TeamColor otherSide = getOtherTeam(color);
        if (color != getTeamTurn()) {
            throw new InvalidMoveException("It is not this piece's turn.");
        }
        updateFlags(move);
        movePieces(move, board);

        if (isInCheck(otherSide)) {
            board.setHighlight(board.getKingSquare(otherSide), ChessColor.Highlight.TERNARY);
        }

        if (move.capturesByEnPassant()) {
            enPassant = null;
            positionVulnerableToEnPassant = null;
            return;
        }
        if (enPassant != null) {
            board.removePiece(enPassant);
            enPassant = null;
            positionVulnerableToEnPassant = null;
        }
        if (move.createsEnPassant()) {
            enPassant = move.passedPosition();
            positionVulnerableToEnPassant = move.getEndPosition();
        }
        board.setHighlight(move.getStartPosition(), ChessColor.Highlight.PRIMARY);
        board.setHighlight(move.getEndPosition(), ChessColor.Highlight.PRIMARY);
        ConsolePrinter.printGame(this);
    }

    /**
     * Moves the pieces necessary for a given move on a given board.
     * No other attributes are changed.
     *
     * @param move  the move to execute on the board.
     * @param board the board the move is executed on.
     */
    public void movePieces(ChessMove move, ChessBoard board) {
        /* TODO: Some edge cases here might throw a exception if the wrong board is passed
         * TODO:    It should either not do anything or throw an exception
         */
        // use special case methods if applicable
        if (move.capturesByEnPassant()) {
            makeEnPassantCaptureMove(move, board);
            return;
        }
        ChessPosition enPassantPosition = null;
        for (Map.Entry<ChessPosition, ChessPiece> entry : board.getPieces().entrySet()) {
            if (entry.getValue().getPieceType() == ChessPiece.PieceType.EN_PASSANT) {
                enPassantPosition = entry.getKey();
                break;
            }
        }
        if (enPassantPosition != null) {
            board.removePiece(enPassantPosition);
        }
        if (move.createsEnPassant()) {
            makeEnPassantMove(move, board);
            return;
        }
        if (move.isCastle()) {
            makeCastleMove(move, board);
            return;
        }
        makeStandardMove(move, board);
    }

    /**
     * Implicitly confirms a move is legal or throws an InvalidMoveException if it isn't.
     * Doesn't check for turn so moves can be checked while waiting for opponent.
     *
     * @param move a ChessMove to validate
     * @throws InvalidMoveException if move is illegal.
     */
    private void validateMove(ChessMove move) throws InvalidMoveException {
        // TODO: Might be worth checking for checkmate.
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = board.getPiece(start);
        // Fail fast if move isn't valid
        if (piece == null) {
            throw new InvalidMoveException("There is no piece at this move's start.");
        }
        TeamColor color = piece.getTeamColor();
        if (!piece.pieceMoves(board, start).contains(move)) {
            throw new InvalidMoveException("This piece can't move to that position.");
        }
        if (!move.decorated()) {
            move.decorate(board);
        }
        if (move.isCastle()) {
            validateCastleMove(move, color);
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            Collection<ChessPosition> threatenedPositions = board.getPositionsThreatenedByColor(getOtherTeam(color));
            if (threatenedPositions.contains(end)) {
                throw new InvalidMoveException("The king cannot move into check");
            }
        }
        ChessBoard testBoard = new ChessBoard();
        testBoard.setPieces(board.getPieces());
        movePieces(move, testBoard);
        ChessPosition kingPosition = testBoard.getKingSquare(color);
        Collection<ChessPosition> threatenedPositions = testBoard.getPositionsThreatenedByColor(getOtherTeam(color));
        if (threatenedPositions.contains(kingPosition)) {
            throw new InvalidMoveException("The king cannot end in check");
        }
    }

    private void validateCastleMove(ChessMove move, TeamColor color) throws InvalidMoveException {
        if (isInCheck(color)) {
            throw new InvalidMoveException("Cannot castle while in check");
        }
        if (color == TeamColor.WHITE) { // Throw if king or relevant rook has moved.
            if (move.isLongCastle() && !whiteCanLongCastle) {
                throw new InvalidMoveException("White cannot long castle");
            } else if (move.isShortCastle() && !whiteCanShortCastle) {
                throw new InvalidMoveException("White cannot short castle");
            }
        } else {
            if (move.isLongCastle() && !blackCanLongCastle) {
                throw new InvalidMoveException("Black cannot long castle");
            } else if (move.isShortCastle() && !blackCanShortCastle) {
                throw new InvalidMoveException("Black cannot short castle");
            }
        }
        int homeRank = color == TeamColor.WHITE ? 1 : 8;

        Collection<ChessPosition> inBetweenPositions = new ArrayList<>(); // These must be clear in order to castle
        if (move.isShortCastle()) {
            inBetweenPositions.add(new ChessPosition(homeRank, 6));
            inBetweenPositions.add(new ChessPosition(homeRank, 7));
        } else {
            inBetweenPositions.add(new ChessPosition(homeRank, 2));
            inBetweenPositions.add(new ChessPosition(homeRank, 3));
            inBetweenPositions.add(new ChessPosition(homeRank, 4));
        }
        for (ChessPosition position : inBetweenPositions) {
            ChessPiece piece = board.getPiece(position);
            if (piece != null) {
                throw new InvalidMoveException("The piece at " + position + " blocks the castle");
            }
        }

        Collection<ChessPosition> threatenedPositions = board.getPositionsThreatenedByColor(getOtherTeam(color));
        ChessPosition safePosition = move.isShortCastle() ? new ChessPosition(homeRank, 6) : new ChessPosition(homeRank, 4);
        if (threatenedPositions.contains(safePosition)) {
            throw new InvalidMoveException("Cannot castle through check");
        }
    }

    private void makeStandardMove(ChessMove move, ChessBoard board) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
        ChessPiece piece = board.getPiece(start);
        TeamColor color = piece.getTeamColor();
        board.removePiece(start);
        board.addPiece(end, promotionPiece == null ? piece : new ChessPiece(color, promotionPiece));
    }

    private void makeCastleMove(ChessMove move, ChessBoard board) {
        ChessPiece king = board.removePiece(move.getStartPosition());
        ChessPiece rook = board.removePiece(move.getCastlingRookStart());
        board.addPiece(move.getEndPosition(), king);
        board.addPiece(move.getCastlingRookEnd(), rook);
    }

    private void makeEnPassantMove(ChessMove move, ChessBoard board) {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        TeamColor color = piece.getTeamColor();
        board.removePiece(move.getStartPosition());
        board.addPiece(move.getEndPosition(), piece);
        board.addPiece(move.passedPosition(), new ChessPiece(color, ChessPiece.PieceType.EN_PASSANT));
    }

    private void makeEnPassantCaptureMove(ChessMove move, ChessBoard board) {
        ChessPiece piece = board.removePiece(move.getStartPosition());
        board.removePiece(positionVulnerableToEnPassant);
        board.addPiece(move.getEndPosition(), piece);
    }

    /**
     * Sets the games state flags following the execution of the given move.
     *
     * @param move the most recently executed move.
     */
    private void updateFlags(ChessMove move) {
        moveHistory.add(move);
        ChessPiece piece = board.getPiece(move.getStartPosition());
        ChessPiece.PieceType type = piece.getPieceType();
        TeamColor color = piece.getTeamColor();
        // Castling flags
        if (type == ChessPiece.PieceType.KING) {
            if (color == TeamColor.WHITE) {
                whiteCanLongCastle = whiteCanShortCastle = false;
            } else {
                blackCanLongCastle = blackCanShortCastle = false;
            }
        } else if (type == ChessPiece.PieceType.ROOK) {
            ChessPosition start = move.getStartPosition();
            if (start.equals(new ChessPosition(1, 1))) {
                whiteCanLongCastle = false;
            }
            if (start.equals(new ChessPosition(1, 8))) {
                whiteCanShortCastle = false;
            }
            if (start.equals(new ChessPosition(8, 1))) {
                blackCanLongCastle = false;
            }
            if (start.equals(new ChessPosition(8, 8))) {
                blackCanShortCastle = false;
            }
        } else if (type == ChessPiece.PieceType.PAWN) {
            halfMoveClock = 0; // resets on pawn advance
        }
        if (move.isCapture()) {
            halfMoveClock = 0; // or captures
        }
        fullMoveNumber += color == TeamColor.BLACK ? 1 : 0; // Increment clock on black turns

        turn = getOtherTeam(turn);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check, false otherwise.
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingSquare = board.getKingSquare(teamColor);
        Collection<ChessPosition> threatenedPositions = board.getPositionsThreatenedByColor(getOtherTeam(teamColor));
        return threatenedPositions.contains(kingSquare);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        Collection<ChessMove> moves = new HashSet<>();
        for (ChessPosition position : board.getPositionsByColor(teamColor)) {
            moves.addAll(validMoves(position));
        }
        return moves.isEmpty();
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves but not being in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        Collection<ChessMove> moves = new HashSet<>();
        for (ChessPosition position : board.getPositionsByColor(teamColor)) {
            moves.addAll(validMoves(position));
        }
        return moves.isEmpty();
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    public ArrayList<ChessMove> moveHistory() {
        return moveHistory;
    }

    public TeamColor getOtherTeam(TeamColor color) {
        return color == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * The state of the game as expressed in
     * <a href="https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation">Forsyth–Edwards Notation (FEN)</a>
     *
     * @return a FEN string
     */
    public String fenString() {
        StringBuilder fenString = new StringBuilder(board.positionFenString());
        fenString.append(' ').append(turn == TeamColor.WHITE ? 'w' : 'b').append(' ');
        if (whiteCanShortCastle || whiteCanLongCastle || blackCanShortCastle || blackCanLongCastle) {
            fenString.append(whiteCanShortCastle ? 'K' : "");
            fenString.append(whiteCanLongCastle ? 'Q' : "");
            fenString.append(blackCanShortCastle ? 'k' : "");
            fenString.append(blackCanLongCastle ? 'q' : "");
        } else {
            fenString.append('-');
        }
        fenString.append(' ');
        if (enPassant == null) {
            fenString.append('-');
        } else {
            fenString.append(enPassant);
        }
        fenString.append(' ');
        fenString.append(halfMoveClock).append(' ');
        fenString.append(fullMoveNumber);
        return fenString.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return turn == chessGame.turn && Objects.equals(getBoard(), chessGame.getBoard());
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, getBoard());
    }

    @Override
    public String toString() {
        return fenString();
    }

    public static class ChessGameDeserializer implements JsonDeserializer<ChessGame> {

        @Override
        public ChessGame deserialize
                (JsonElement jElement, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jObject = jElement.getAsJsonObject();
            // Board
            JsonElement boardJson = jObject.get("board");
            JsonElement piecesJson = boardJson.getAsJsonObject().get("pieces");
            Hashtable<ChessPosition, ChessPiece> pieces = new Hashtable<>();
            var a = piecesJson.getAsJsonObject().asMap();
            for (Map.Entry<String, JsonElement> entry : a.entrySet()) {
                ChessPosition position = new ChessPosition(entry.getKey());
                ChessPiece piece = GSON.fromJson(entry.getValue(), ChessPiece.class);
                pieces.put(position, piece);
            }
            ChessBoard board = new ChessBoard();
            board.setPieces(pieces);
            // Move History
            List<JsonElement> movesJson = jObject.get("moveHistory").getAsJsonArray().asList();
            ArrayList<ChessMove> moveHistory = new ArrayList<>();
            for (JsonElement element : movesJson) {
                JsonObject elementObject = element.getAsJsonObject();
                JsonElement startJson = elementObject.get("start");
                JsonElement endJson = elementObject.get("end");
                JsonElement promotion = elementObject.get("promotionPiece");
                ChessPosition start = GSON.fromJson(startJson, ChessPosition.class);
                ChessPosition end = GSON.fromJson(endJson, ChessPosition.class);
                ChessPiece.PieceType promotionPiece = GSON.fromJson(promotion, ChessPiece.PieceType.class);
                ChessMove move = new ChessMove(start, end, promotionPiece);
                moveHistory.add(move);
            }
            String turnString = jObject.get("turn").getAsString();
            ChessGame.TeamColor turn = Objects.equals(turnString, "WHITE") ?
                    ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
            boolean whiteShort = jObject.get("whiteCanShortCastle").getAsBoolean();
            boolean whiteLong = jObject.get("whiteCanLongCastle").getAsBoolean();
            boolean blackShort = jObject.get("blackCanShortCastle").getAsBoolean();
            boolean blackLong = jObject.get("blackCanLongCastle").getAsBoolean();
            ChessPosition enPassantSquare = GSON.fromJson(jObject.get("enPassant"), ChessPosition.class);
            int halfMove = jObject.get("halfMoveClock").getAsInt();
            int fullMove = jObject.get("fullMoveNumber").getAsInt();
            return new ChessGame(board, moveHistory, turn, whiteShort, whiteLong, blackShort, blackLong, enPassantSquare, halfMove, fullMove);
        }
    }
}
