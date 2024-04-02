package chess;

import java.security.KeyPair;
import java.util.*;

public class GameState {
    private boolean print;
    private RuleSet rules;

    private ChessBoard board;

    private ChessGame.TeamColor turn;

    private int movesSinceCapture = 0;
    private boolean whiteIsInCheck;
    private boolean blackIsInCheck;
    private boolean whiteIsInCheckmate;
    private boolean blackIsInCheckmate;
    private boolean whiteIsInStalemate;
    private boolean blackIsInStalemate;

    private boolean whiteCanCastleShort;
    private boolean whiteCanCastleLong;
    private boolean blackCanCastleShort;
    private boolean blackCanCastleLong;


    private ChessPosition enPassant;

    private int halfMoveClock;
    private int fullMoveClock;

    private Vector<ChessMove> history;
    private HashMap<ChessBoard, Integer> positionHashMap;
    public int getHalfMoveClock() {
        return halfMoveClock;
    }

    public void setHalfMoveClock(int halfMoveClock) {
        this.halfMoveClock = halfMoveClock;
    }

    public int getFullMoveClock() {
        return fullMoveClock;
    }

    public void setFullMoveClock(int fullMoveClock) {
        this.fullMoveClock = fullMoveClock;
    }

    public void incrementFullmoveClock() {
        this.fullMoveClock += 1;
    }

    public void incrementHalfmoveClock() {
        this.halfMoveClock += 1;
    }


    public boolean whiteCanCastleShort() {
        return whiteCanCastleShort;
    }
    public void whiteCanCastleShort(boolean whiteCanCastleShort) {
        this.whiteCanCastleShort = whiteCanCastleShort;
    }
    public boolean whiteCanCastleLong() {
        return whiteCanCastleLong;
    }
    public void whiteCanCastleLong(boolean whiteCanCastleLong) {
        this.whiteCanCastleLong = whiteCanCastleLong;
    }
    public boolean blackCanCastleShort() {
        return blackCanCastleShort;
    }
    public void blackCanCastleShort(boolean blackCanCastleShort) {
        this.blackCanCastleShort = blackCanCastleShort;
    }
    public boolean blackCanCastleLong() {
        return blackCanCastleLong;
    }
    public void blackCanCastleLong(boolean blackCanCastleLong) {
        this.blackCanCastleLong = blackCanCastleLong;
    }



    public ChessPosition getEnPassant() {
        return enPassant;
    }

    public void setEnPassant(ChessPosition enPassant) {
        board.addPiece(enPassant, new ChessPiece(getOtherTeam(turn), ChessPiece.PieceType.EN_PASSANT));
        this.enPassant = enPassant;
    }

    public void setEnPassant(ChessPosition enPassant, ChessGame.TeamColor color) {
        board.addPiece(enPassant, new ChessPiece(color, ChessPiece.PieceType.EN_PASSANT));
        this.enPassant = enPassant;
    }


    public GameState() {
        turn = ChessGame.TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
        history = new Vector<ChessMove>();
        positionHashMap = new HashMap<ChessBoard, Integer>();
        rules = new StandardRules();
        whiteIsInCheck = blackIsInCheck = whiteIsInCheckmate = blackIsInCheckmate = whiteIsInStalemate =
                blackIsInStalemate = false;
        print = blackCanCastleShort = blackCanCastleLong = whiteCanCastleShort = whiteCanCastleLong = true;
    }

    public GameState(GameState state) {
        this.rules = state.rules;

        this.board = state.board;

        this.turn = state.turn;

        this.movesSinceCapture = state.movesSinceCapture;
        this.whiteIsInCheck = state.whiteIsInCheck;
        this.blackIsInCheck = state.blackIsInCheck;
        this.whiteIsInCheckmate = state.whiteIsInCheckmate;
        this.blackIsInCheckmate = state.blackIsInCheckmate;
        this.whiteIsInStalemate = state.whiteIsInStalemate;
        this.blackIsInStalemate = state.blackIsInStalemate;

        this.whiteCanCastleShort = state.whiteCanCastleShort;
        this.whiteCanCastleLong = state.whiteCanCastleLong;
        this.blackCanCastleShort = state.blackCanCastleShort;
        this.blackCanCastleLong = state.blackCanCastleLong;


        this.enPassant = state.enPassant;

        this.halfMoveClock = state.halfMoveClock;
        this.fullMoveClock = state.fullMoveClock;

        this.history = state.history;
        this.positionHashMap = state.positionHashMap;
    }


    public void resetFlags() {
        turn = ChessGame.TeamColor.WHITE;
        history.clear();
        positionHashMap.clear();
        whiteIsInCheck = blackIsInCheck = whiteIsInCheckmate = blackIsInCheckmate = whiteIsInStalemate =
                blackIsInStalemate = false;
        blackCanCastleShort = blackCanCastleLong = whiteCanCastleShort = whiteCanCastleLong = true;
    }
    public GameState(ChessBoard board, ChessGame.TeamColor turn) {
        this.board = board;
        board.resetBoard();
    }

    public Collection<ChessMove> getValidMoves(ChessPosition position) {
        Collection<ChessMove> moves = getPossibleMoves(position);
        Collection<ChessMove> goodMoves = new HashSet<>();
        for(ChessMove m : moves) {
            try {
                goodMoves.add(confirmMove(m, true));
            } catch (InvalidMoveException e) {
                // System.out.println("Caught invalid move: " + e.getMessage());
            }
        }
        return goodMoves;
    }

    public Collection<ChessMove> getPossibleMoves(ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        Collection<ChessMove> moves = piece.getPieceMoves(board, position);
        return moves;
    }

    public Collection<ChessMove> getValidMoves() {
        Dictionary<ChessPosition, ChessPiece> pieces = board.getPieces();
        Enumeration<ChessPosition> piecePositions = pieces.keys();
        HashSet<ChessMove> moves = new HashSet<>();
        while (piecePositions.hasMoreElements()) {
            ChessPosition nextPosition = piecePositions.nextElement();
            moves.addAll(getValidMoves(nextPosition));
        }
        return moves;
    }
    public Collection<ChessMove> getValidForEach (Collection<ChessPosition> positions) {
        Collection<ChessMove> moves = new HashSet<>();
        for(ChessPosition p : positions) {
            moves.addAll(getValidMoves(p));
        }
        return moves;
    }
    private ChessMove decorateMove(ChessMove move) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        int startRank = start.getRank();
        int startFile = start.getFile();
        int endRank = end.getRank();
        int endFile = end.getFile();
        ChessPiece piece = board.getPiece(start);
        ChessPiece.PieceType type = piece.getPieceType();
        ChessGame.TeamColor color = piece.getTeamColor();
        int kingHomeRank = (color == ChessGame.TeamColor.WHITE)? 1 : 8;
        int advanceDirection = (color == ChessGame.TeamColor.WHITE)? 1 : -1;

        ChessMove.MoveBuilder decoratedMove = new ChessMove.MoveBuilder(move);

        decoratedMove.withPiece(piece);
        if (move.enPassant != null) decoratedMove.enPassant(move.enPassant);
        else {
            if (
                type == ChessPiece.PieceType.PAWN &&
                startRank == kingHomeRank + advanceDirection &&
                endRank == startRank + ( 2 * advanceDirection)
            )
            {
                decoratedMove.enPassant(new ChessPosition((startRank + advanceDirection), startFile));
            }
        }

        decoratedMove.isCapture(move.isCapture || board.getPiece(end) != null);

        ChessPiece capturePiece = board.getPiece(move.getEndPosition());
        if (capturePiece != null && board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.EN_PASSANT) {
            if (piece.getPieceType() != ChessPiece.PieceType.PAWN) {
               decoratedMove.isCapture(false);
            }
        }
        // decoratedMove.isCheck; // FIXME
        // boolean isMate = move.isMate; // FIXME
        boolean shortCastle = move.shortCastle;
        boolean longCastle = move.longCastle;

        if (    piece.getPieceType() == ChessPiece.PieceType.KING &&
                start.getFile() == 5 &&
                start.getRank() == kingHomeRank &&
                end.getRank() == kingHomeRank
        ) {
            if (end.getFile() == 7) shortCastle = true;
            if (end.getFile() == 3) longCastle = true;
        }
        decoratedMove.shortCastle(shortCastle);
        decoratedMove.longCastle(longCastle);
        // boolean offerDraw = move.offerDraw; // FIXME maybe

        return decoratedMove.build();
    }

    public ChessMove confirmMove(ChessMove move) throws InvalidMoveException{
        // FIXME loses decoration and is losing en passant cosequently
        ChessMove confirmed = confirmMove(move, false);
        return confirmed;
    }

    public ChessMove confirmMove(ChessMove move, boolean allowOtherTeamMoves) throws InvalidMoveException{
        if (board.getPiece(move.getStartPosition()) == null) throw new InvalidMoveException("No piece at start square");
        if (move.getStartPosition().isOffBoard() || move.getEndPosition().isOffBoard()) throw new InvalidMoveException("Position not on board");
        move = decorateMove(move);
        ChessPiece piece = move.piece;
        if (!allowOtherTeamMoves && piece.getTeamColor() != turn) throw new InvalidMoveException("Cannot move opposing piece");
        if (move.getPromotionPiece() != null && piece.getPieceType() != ChessPiece.PieceType.PAWN)  throw new InvalidMoveException("Promotion from non-pawn");
        ChessGame.TeamColor color = piece.getTeamColor();
        if (move.castle) {
            if (isInCheck(color)) throw new InvalidMoveException("Cannot castle in check");
            ChessPosition[] whiteShortCastleSquares = {new ChessPosition(1, 6), new ChessPosition(1, 7)};
            ChessPosition[] blackShortCastleSquares = {new ChessPosition(8, 6), new ChessPosition(8, 7)};
            ChessPosition[] whiteLongCastleSquares = {new ChessPosition(1, 4), new ChessPosition(1, 3)};
            ChessPosition[] blackLongCastleSquares = {new ChessPosition(8, 4), new ChessPosition(8, 3)};
            ChessPosition[] safeSquares;
            if (color == ChessGame.TeamColor.WHITE) {
                if (move.shortCastle && whiteCanCastleShort) safeSquares = whiteShortCastleSquares;
                else if (move.longCastle && whiteCanCastleLong) safeSquares = whiteLongCastleSquares;
                else throw new InvalidMoveException("White cannot castle that direction");
            } else {
                if (move.shortCastle && blackCanCastleShort) safeSquares = blackShortCastleSquares;
                else if (move.longCastle && blackCanCastleLong) safeSquares = blackLongCastleSquares;
                else throw new InvalidMoveException("Black cannot castle that direction");
            }
            Collection<ChessPosition> threatenedSquares = getThreatenedPositions(color);
            for (ChessPosition p : safeSquares) {
                if (getPiece(p) != null) throw new InvalidMoveException("Cannot castle through piece");
                if (threatenedSquares.contains(p)) throw new InvalidMoveException("Cannot castle through check");
            }
        }

        GameState testState = new GameState();
        testState.setPrint(false);
        testState.board().setBoard(board.getPositions());
        testState.forceMove(move);
        if (testState.isInCheck(color)) {
            throw new InvalidMoveException("Moves into check");
        }
        return move;
    }

    public boolean isInCheck(ChessGame.TeamColor color) {
        ChessPosition kingPosition = getKingPosition(color);
        if (kingPosition == null) return false;
        return getThreatenedPieces(color).contains(getKingPosition(color));
    }

    private ChessPosition getKingPosition(ChessGame.TeamColor color) {
        Collection<ChessPosition> positions = getPositionsByColor(color);
        for (ChessPosition position : positions) {
            ChessPiece piece = board.getPiece(position);
            if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == color) {
                return position;
            }
        }
        return null;
    }
    private Collection<ChessMove> getMovesByColor(ChessGame.TeamColor color) {
        Collection<ChessPosition> piecesOfColor = getPositionsByColor(color);
        Collection<ChessMove> availableMoves = new HashSet<>();
        for (ChessPosition p : piecesOfColor) {
            availableMoves.addAll(getValidMoves(p));
        }
        return availableMoves;
    }
    private Collection<ChessMove> getPossibleMovesByColor(ChessGame.TeamColor color) {
        Collection<ChessPosition> piecesOfColor = getPositionsByColor(color);
        Collection<ChessMove> availableMoves = new HashSet<>();
        for (ChessPosition p : piecesOfColor) {
            availableMoves.addAll(getPossibleMoves(p));
        }
        return availableMoves;
    }
    private Collection<ChessPosition> getThreatenedPieces(ChessGame.TeamColor color) {
        ChessGame.TeamColor otherTeam = getOtherTeam(color);
        Collection<ChessPosition> attackedSquares = new HashSet<>();
        Collection<ChessMove> enemyMoves = getPossibleMovesByColor(otherTeam);
        for (ChessMove m : enemyMoves) {

            if (m.isCapture)  {
                attackedSquares.add(m.getEndPosition());
                board().highlightPosition(m.endPosition, ChessBoard.Highlight.NEGATIVE );
            } else {
                board().highlightPosition(m.endPosition, ChessBoard.Highlight.TERNARY);
            }
        }
        // board().prettyPrint();
        return attackedSquares;
    }

    private Collection<ChessPosition> getThreatenedPositions(ChessGame.TeamColor color) {
        ChessGame.TeamColor otherTeam = getOtherTeam(color);
        Collection<ChessPosition> attackedSquares = new HashSet<>();
        Collection<ChessMove> enemyMoves = getPossibleMovesByColor(otherTeam);
        for (ChessMove m : enemyMoves) {
            attackedSquares.add(m.getEndPosition());
            board().highlightPosition(m.endPosition, ChessBoard.Highlight.TERNARY);
        }
        // board().prettyPrint();
        return attackedSquares;
    }
    private Collection<ChessPosition> getPositionsByColor(ChessGame.TeamColor color) {
        Dictionary<ChessPosition, ChessPiece> positionDictionary = board.getPieces();
        Enumeration<ChessPosition> positions = positionDictionary.keys();
        HashSet<ChessMove> moves = new HashSet<>();
        Collection<ChessPosition> movesWithColor = new HashSet<>();
        while (positions.hasMoreElements()) {
            ChessPosition nextPosition = positions.nextElement();
            if (positionDictionary.get(nextPosition).getTeamColor() == color) movesWithColor.add(nextPosition);
        }
        return movesWithColor;
    }
    private Collection<ChessPosition> getWhitePositions(ChessGame.TeamColor color) {
        return getPositionsByColor(ChessGame.TeamColor.WHITE);
    }
    private Collection<ChessPosition> getBlackPositions(ChessGame.TeamColor color) {
        return getPositionsByColor(ChessGame.TeamColor.BLACK);
    }

    public void makeMove(ChessMove move) throws InvalidMoveException {
        makeMove(move, false);
        // prettyPrint();
    }
    public void makeMove(String move) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }
    private void forceMove(ChessMove move) {
        try {
            makeMove(move, true);
        } catch (InvalidMoveException e) {
            System.out.println("This actually shouldn't happen.");
        }
    }
    private void makeMove(ChessMove move, boolean force) throws InvalidMoveException{
        if (!force) move = confirmMove(move);
        addHistory(move);
        ChessPiece piece = removePiece(move.startPosition);
        if (move.castle) {
            int rank = move.getStartPosition().getRank();
            if (move.longCastle) {
                ChessPiece rook = removePiece(new ChessPosition(rank, 1));
                board.addPiece(new ChessPosition(rank, 4), rook);
            } else {
                ChessPiece rook = removePiece(new ChessPosition(rank, 8));
                board.addPiece(new ChessPosition(rank, 6), rook);
            }
        }
        if (move.enPassant != null) {
            enPassant = move.enPassant;
            addPiece(enPassant, new ChessPiece(piece.getTeamColor(), ChessPiece.PieceType.EN_PASSANT));
        } else enPassant = null;
        if (move.isCapture && board.getPiece(move.endPosition).getPieceType() == ChessPiece.PieceType.EN_PASSANT) {
            int advanceDirection = (move.piece.getTeamColor() == ChessGame.TeamColor.WHITE)? 1 : -1;
            board.removePiece(
                    new ChessPosition(move.endPosition.getRank() - advanceDirection, move.endPosition.getFile())
            );
        }

        piece = (move.promotionPiece == null)? piece: new ChessPiece(piece.getTeamColor(), move.promotionPiece);
        board.addPiece(move.endPosition, piece);
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            ChessGame.TeamColor color = piece.getTeamColor();
            if (color == ChessGame.TeamColor.WHITE) {
                whiteCanCastleLong = whiteCanCastleShort = false;
            }
            else {
                blackCanCastleShort = blackCanCastleLong = false;
            }
        }
        if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            ChessGame.TeamColor color = piece.getTeamColor();
            if (color == ChessGame.TeamColor.WHITE) {
                switch (move.getStartPosition().getFile()) {
                    case 1:
                        whiteCanCastleLong = false;
                        break;
                    case 8:
                        whiteCanCastleShort = false;
                        break;
                }
            } else {
                switch (move.getStartPosition().getFile()) {
                    case 1:
                        whiteCanCastleLong = false;
                        break;
                    case 8:
                        whiteCanCastleShort = false;
                        break;
                }
            }
        }
        cycleTurn();
        board().resetHighlight();
    }

    private void undoMove() {
        if (history().size() < 1) throw new RuntimeException("No history to undo");
        ChessMove move = history.removeLast();

    }
    public ChessGame.TeamColor getOtherTeam(ChessGame.TeamColor current) {
        return (current == ChessGame.TeamColor.WHITE)? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
    }
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board.addPiece(position, piece);
    }
    public ChessPiece removePiece(ChessPosition position) {
        return board.removePiece(position);
    }
    public ChessPiece getPiece(ChessPosition position) {
        return board.getPiece(position);
    }

    public Vector<ChessMove> history() {
        return history;
    }
    private void addHistory(ChessMove move) {
        history.add(move);
    }
    public ChessMove getLastMove() {
        return history.lastElement();
    }
    public int getMaxPositionRepeats() {
        int repeats = 0;
        for (int entry : positionHashMap.values()) {
            repeats = Math.max(repeats, entry);
        }
        return repeats;
    }

    public ChessBoard board() {
        return board;
    }
    public void board(ChessBoard board) {
        this.board = board;
        // prettyPrint();
    }

    public ChessGame.TeamColor turn() {
        return turn;
    }
    public void turn(ChessGame.TeamColor turn) {
        this.turn = turn;
    }
    public void cycleTurn() {
        turn = getOtherTeam(turn);
    }

    public boolean check() {
        return whiteIsInCheck || blackIsInCheck;
    }
    public boolean check(ChessGame.TeamColor team) {
        return (team == ChessGame.TeamColor.WHITE)? whiteIsInCheck : blackIsInCheck;
    }
    public void check(ChessGame.TeamColor color, boolean check) {
        if (color == ChessGame.TeamColor.WHITE) whiteIsInCheck = check;
        else blackIsInCheck = check;
    }
    
    public boolean checkmate() {
        return whiteIsInCheckmate || blackIsInCheckmate;
    }
    public boolean checkmate(ChessGame.TeamColor color) {
        return  (color == ChessGame.TeamColor.WHITE)? whiteIsInCheckmate : blackIsInCheckmate;
    }
    public void checkmate(ChessGame.TeamColor color, boolean checkmate) {
        if (color == ChessGame.TeamColor.WHITE) whiteIsInCheckmate = checkmate;
        else blackIsInCheckmate = checkmate;
    }

    public boolean stalemate() {
        return whiteIsInStalemate || blackIsInStalemate;
    }
    public boolean stalemate(ChessGame.TeamColor color) {
        return  (color == ChessGame.TeamColor.WHITE)? whiteIsInStalemate : blackIsInStalemate;
    }
    public void stalemate(ChessGame.TeamColor color, boolean stalemate) {
        if (color == ChessGame.TeamColor.WHITE) whiteIsInStalemate = stalemate;
        else blackIsInStalemate = stalemate;
    }

    public int movesSinceCapture() {
        return movesSinceCapture;
    }
    public void movesSinceCapture(int movesSinceCapture) {
        this.movesSinceCapture = movesSinceCapture;
    }

    private String historyToString() {
        StringBuilder historyString = new StringBuilder();
        for (ChessMove move : history) {
            historyString.append(move);
            historyString.append(' ');
        }
        return historyString.toString();
    }
    public String toString() {
        FENParser fenParser = new FENParser();
        return fenParser.getFen(this);
    }
    public String prettyToString() {
        String turn = (turn() == ChessGame.TeamColor.WHITE)? "White to move": "Black to Move";
        String out = board.prettyToString() +
                    "   " + turn +
                    "\nFEN: \t\t" + toString() +
                    "\nHISTORY: \t" + historyToString();
        return out;
    }
    public void prettyPrint() {
        if (print) System.out.println(prettyToString());
    }
    public void setPrint(boolean p) {
        print = p;
    }
}