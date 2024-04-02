package chess;
import java.util.*;

public class StandardRules implements RuleSet{
    private final int BOARD_SIZE = 8;
    private static final String STARTING_LAYOUT = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    
    public StandardRules() {
        
    }

    public Collection<ChessMove> getAllValidMoves(GameState state) {
        state.board().print();
        HashSet<ChessMove> validMoves = new HashSet<>();
        Dictionary<ChessPosition, ChessPiece> piecePositions = state.board().getPieces();
        Enumeration<ChessPosition> positions = piecePositions.keys();
        while (positions.hasMoreElements()) {
            ChessPosition position = positions.nextElement();
            ChessPiece piece = piecePositions.get(position);
            if (piece.getTeamColor() == state.turn()) {
                validMoves.addAll(getValidMoves(state, position));
            }
        }
        return validMoves;
    }

    public Collection<ChessMove> getValidMoves(GameState state, ChessPosition position) {
        state.board().print();
        ChessPiece piece = state.board().getPiece(position);
        Collection<ChessMove> possibleMoves = piece.pieceMoves(state.board(), position);

        // TODO: add checks for check and such
        return possibleMoves;
    }
    public final String getStartingGameState() {
        return STARTING_LAYOUT;
    }
    public boolean isBoardValid(GameState state) {
        throw new RuntimeException("Not implemented");
    }
    public boolean isInCheck(GameState state, ChessGame.TeamColor color) {
        ChessPosition kingPosition = getKingPosition(state, color);
        ChessGame.TeamColor otherTeam  = (color == ChessGame.TeamColor.WHITE)? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
        if (getSquaresThreatenedBy(state, otherTeam).contains(kingPosition)) return true;
        return false;
    }

    private Collection<ChessPosition> getSquaresThreatenedBy(GameState state, ChessGame.TeamColor otherTeam) {
        throw new RuntimeException("Not Implimented");
//        ChessPosition[] pieces = getPiecePositions(state, otherTeam).toArray(new ChessPosition[0]);
//        HashSet<ChessPosition> squares = new HashSet<>();
//        for (ChessPosition piece : pieces) {
//            squares.addAll(state.board().getPiece(piece).pieceMoves(state.board(), piece));
//        }
    }

    private ChessPosition getAvailableCaptures(GameState state, ChessGame.TeamColor color) {
        throw new RuntimeException("Not Implimented");
    }

    private ChessPosition getAvailableCaptures(GameState state) {
        return getAvailableCaptures(state, state.turn());
    }

    public boolean isInCheckmate(GameState state, ChessGame.TeamColor color) {
        return isInCheck(state, color);
    }
    public boolean isInStalemate(GameState state, ChessGame.TeamColor color) {
        throw new RuntimeException("Not implemented");
    }

    private ChessPosition getKingPosition(GameState state, ChessGame.TeamColor color) {
        for (ChessPosition position: getPiecePositions(state, color)) {
            ChessPiece piece = state.getPiece(position);
            if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                return position;
            }
        }
        throw new RuntimeException("NO KING ON BOARD");
    }

    private Collection<ChessPosition> getPiecePositions(GameState state, ChessGame.TeamColor color) {
        ArrayList<ChessPosition> piecePositions = new ArrayList<>();
        Dictionary<ChessPosition, ChessPiece> allPieces =  state.board().getPieces();
        Enumeration<ChessPosition> positions = allPieces.keys();

        while (positions.hasMoreElements()) {
            ChessPosition position = positions.nextElement();
            if (allPieces.get(position).getTeamColor() == color) {
                piecePositions.add(position);
            }
        }
        return (piecePositions);
    }
    private Collection<ChessPosition> getThreatenedSquares(GameState state, ChessGame.TeamColor color) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        HashSet<ChessPosition> squares = new HashSet<ChessPosition>();
        Dictionary<ChessPosition, ChessPiece> allPieces =  state.board().getPieces();
        Enumeration<ChessPosition> positions = allPieces.keys();

        while (positions.hasMoreElements()) {
            ChessPosition position = positions.nextElement();
            ChessPiece piece = allPieces.get(position);
            if (piece.getTeamColor() == color) {
                moves.addAll(piece.pieceMoves(state.board(), position));
            }
        }
        for (ChessMove move: moves) {
            ChessPosition threat = move.getEndPosition();
            state.board().highlightPosition(threat, ChessBoard.Highlight.PRIMARY);
            squares.add(threat);
        }
        state.board().print();
        return squares;
    }

    /*
    private Collection<ChessMove> getKingMoves(GameState state, ChessPosition position, ChessPiece piece) {
        HashSet <ChessMove> moves = new HashSet<ChessMove>();
        moves.addAll(getDiagonalSlidePositions(state, position, 1));
        moves.addAll(getOrthogonalSlidePositions(state, position, 1));
        return moves;
    }
    private Collection<ChessMove> getQueenMoves(GameState state, ChessPosition position, ChessPiece piece) {
        HashSet <ChessMove> moves = new HashSet<ChessMove>();
        moves.addAll(getDiagonalSlidePositions(state, position, 7));
        moves.addAll(getOrthogonalSlidePositions(state, position, 7));
        return moves;
    }
    private Collection<ChessMove> getBishopMoves(GameState state, ChessPosition position, ChessPiece piece) {
        return getDiagonalSlidePositions(state, position, 7);
    }
    private Collection<ChessMove> getKnightMoves(GameState state, ChessPosition position, ChessPiece piece) {
        int currRank = position.getRank();
        int currFile = position.getFile();
        ChessGame.TeamColor color = state.board().getPieceColor(position);
        HashSet <ChessMove> moves = new HashSet<ChessMove>();
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
            if (state.board().getPieceColor(nextPosition) == color) continue;
            if (state.getPiece(nextPosition) == null) {
                moves.add(new ChessMove(position, nextPosition, null));
                state.board().highlightPosition(nextPosition, ChessBoard.Highlight.PRIMARY);
                continue;
            }
            moves.add(new ChessMove(position, nextPosition, null));
            state.board().highlightPosition(nextPosition, ChessBoard.Highlight.SECONDARY);
        }
        return moves;
    }
    private Collection<ChessMove> getRookMoves(GameState state, ChessPosition position, ChessPiece piece) {
        return getOrthogonalSlidePositions(state, position, 7);
    }
    private Collection<ChessMove> getPawnMoves(GameState state, ChessPosition position, ChessPiece piece) {
        int currRank = position.getRank();
        int currFile = position.getFile();
        ChessPiece.PieceType [] promotionOptions = {
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN};
        ChessGame.TeamColor color = state.board().getPieceColor(position);
        int advanceDirection = (color == ChessGame.TeamColor.WHITE)? 1 : -1;
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        HashSet<ChessPosition> availablePositions = new HashSet<ChessPosition>();

        ChessPosition advOne = new ChessPosition(currRank + advanceDirection, currFile);
        ChessPosition advTwo = new ChessPosition(currRank + (2 * advanceDirection), currFile);
        ChessPosition capRight = new ChessPosition(currRank + advanceDirection, currFile + 1);
        ChessPosition capLeft  = new ChessPosition(currRank + advanceDirection, currFile - 1);
        ChessPosition epRight = new ChessPosition(currRank, currFile + 1);
        ChessPosition epLeft  = new ChessPosition(currRank, currFile - 1);
        ChessPosition[] captures = {capLeft, capRight};
        ChessPosition[] epcaptures = {epLeft, epRight};

        // pushing
        if (advOne.isOnBoard() && state.getPiece(advOne) == null) {
            availablePositions.add(advOne);
            if ((advanceDirection == 1 && currRank == 2 || advanceDirection == -1 && currRank == 7) &&
                    state.getPiece(advTwo) == null) {
                availablePositions.add(advTwo);
            }
        }

        for (ChessPosition captureSquare : captures) {
            if (state.getPiece(captureSquare) != null && state.getPiece(captureSquare).getTeamColor() != color) {
                availablePositions.add(captureSquare);
            }
        }

        for (ChessPosition passedSquare : epcaptures) {
            if (state.getPiece(passedSquare) == null || state.getPiece(passedSquare).getTeamColor() == color) {
                continue;
            }
            ChessMove lastMove = state.getLastMove();
            if (lastMove.endPosition != passedSquare ||
                    lastMove.piece == null ||
                    lastMove.piece.getPieceType() != ChessPiece.PieceType.PAWN) {
                continue;
            }
            ChessPosition epPrev = new ChessPosition(currRank + (2 * advanceDirection), passedSquare.getFile());
            if (lastMove.startPosition.equals(epPrev)) {
                continue;
            }
            ChessMove nextMove = new ChessMove.MoveBuilder(position, passedSquare)
                    .withPiece(piece)
                    .isCapture()
                    .enPassant(advOne)
                    .build();
            moves.add(nextMove);
        }


        // give promotion options
        for (ChessPosition nextPosition : availablePositions) {
            int rank = nextPosition.getRank();
            if ((color == ChessGame.TeamColor.WHITE && rank == BOARD_SIZE) || (color == ChessGame.TeamColor.BLACK && rank == 1)) {
                for (ChessPiece.PieceType promotion : promotionOptions) {
                    state.board().highlightPosition(nextPosition, ChessBoard.Highlight.TERNARY);
                    moves.add(new ChessMove(position, nextPosition, promotion));
                }
            } else {
                moves.add(new ChessMove(position, nextPosition, null));
            }
        }
        return moves;
    }
     */

//
//    /**
//     * Gets available sliding moves in horizontal and vertical directions
//     *
//     * @param state         The current chess board
//     * @param position      The starting position to calculate moves from
//     * @param maxDist       Max number of squares the piece is allowed to move
//     * @return              A collection of ChessMoves available to the position in the given direction.
//     */
//    private Collection<ChessMove> getOrthogonalSlidePositions(GameState state, ChessPosition position, int maxDist) {
//        HashSet <ChessMove> moves = new HashSet<ChessMove>();
//        moves.addAll(getSlideMoves(state, position, 1, 0, maxDist));
//        moves.addAll(getSlideMoves(state, position, -1, 0, maxDist));
//        moves.addAll(getSlideMoves(state, position, 0, 1, maxDist));
//        moves.addAll(getSlideMoves(state, position, 0, -1, maxDist));
//        return moves;
//    }
//
//    /**
//     * Gets available sliding moves in diagonal directions
//     *
//     * @param state         The current chess board
//     * @param position      The starting position to calculate moves from
//     * @param maxDist       Max number of squares the piece is allowed to move
//     * @return              A collection of ChessMoves available to the position in the given direction.
//     */
//    private Collection<ChessMove> getDiagonalSlidePositions(GameState state, ChessPosition position, int maxDist) {
//        HashSet<ChessMove> moves = new HashSet<ChessMove>();
//        moves.addAll(getSlideMoves(state, position, 1, 1, maxDist));
//        moves.addAll(getSlideMoves(state, position, -1, 1, maxDist));
//        moves.addAll(getSlideMoves(state, position, -1, -1, maxDist));
//        moves.addAll(getSlideMoves(state, position, 1, -1, maxDist));
//        return moves;
//    }
//
//
//    /**
//     * Gets positions available by sliding in one direction
//     * <p>
//     * to find moves diagonally increasing in rank and decreasing in file, call getSlideMoves(board, position, 1, -1).
//     * @param state         The current chess board
//     * @param position      The starting position to calculate moves from
//     * @param rankIteration How many ranks over to move at a time (-1, 0, or 1)
//     * @param fileIteration How many ranks over to move at a time (-1, 0, or 1)
//     * @return              A collection of ChessMoves available to the position in the given direction.
//     */
//    private Collection<ChessMove> getSlideMoves(
//            GameState state,
//            ChessPosition position,
//            int rankIteration,
//            int fileIteration,
//            int maxDist) {
//        int currRank = position.getRank();
//        int currFile = position.getFile();
//        HashSet<ChessPosition> availablePositions = new HashSet<ChessPosition>();
//        HashSet<ChessMove> moves = new HashSet<ChessMove>();
//
//        int i = 1;
//        while (i <= maxDist) {
//            ChessPosition nextPosition = new ChessPosition(currRank + (i * rankIteration), currFile + (i * fileIteration));
//            if (nextPosition.isOffBoard()) break;
//            // empty squares
//            if (state.getPiece(nextPosition) == null){
//                availablePositions.add(nextPosition);
//                state.board().highlightPosition(nextPosition, ChessBoard.Highlight.PRIMARY);
//            }
//            // captures
//            else if (state.getPiece(nextPosition).getTeamColor() != state.getPiece(position).getTeamColor()) {
//                availablePositions.add(nextPosition);
//                state.board().highlightPosition(nextPosition, ChessBoard.Highlight.SECONDARY);
//                break; // Don't search past capture for sliding piece.
//            } else {
//                break;
//            }
//            i++;
//        }
//        return moves;
//    }
//
//
//
//    /**
//     * Returns a collection of ChessMoves with appropriate tags
//     * from a starting position and a collection of ChessPositions
//     * <p>
//     * @param startPosition The position the piece will move from
//     * @param positions     The collection of positions the piece can move to
//     * @return              A collection of ChessMove objects
//     */
//    private Collection<ChessMove> getMovesFromPositions(
//            GameState state,
//            ChessPosition startPosition,
//            Collection<ChessPosition> positions) {
//
//        HashSet<ChessMove> moves = new HashSet<ChessMove>();
//        for (ChessPosition endPosition : positions) {
//            moves.add(new ChessMove(startPosition, endPosition, null));
//        }
//        return moves;
//    }
}

