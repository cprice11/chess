package chess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class StandardRules implements RuleSet{
    private final int BOARD_SIZE = 8;
    GameState startingState = new GameState();
    
    public StandardRules() {
        
    }

    public Collection<ChessMove> getValidMoves(GameState state, ChessPosition position) {
        ChessPiece piece= state.board().getPiece(position);
        Collection<ChessMove> possibleMoves =  switch (piece.getPieceType()) {
            case KING   -> getKingMoves(state, position, piece);
            case QUEEN  -> getQueenMoves(state, position, piece);
            case BISHOP -> getBishopMoves(state, position, piece);
            case KNIGHT -> getKnightMoves(state, position, piece);
            case ROOK   -> getRookMoves(state, position, piece);
            case PAWN   -> getPawnMoves(state, position, piece);
            default -> null;
        };
        // TODO: add checks for check and such
        return possibleMoves;
    }
    public GameState getStartingGameState() {
        return startingState;
    }
    public boolean isBoardValid(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }
    public boolean isInCheck(ChessBoard board, ChessGame.TeamColor color) {
        throw new RuntimeException("Not implemented");
    }
    public boolean isInCheckmate(ChessBoard board, ChessGame.TeamColor color) {
        throw new RuntimeException("Not implemented");
    }
    public boolean isInStalemate(ChessBoard board, ChessGame.TeamColor color) {
        throw new RuntimeException("Not implemented");
    }
    
    private Collection<ChessMove> getKingMoves(GameState state, ChessPosition position, ChessPiece piece) {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> getQueenMoves(GameState state, ChessPosition position, ChessPiece piece) {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> getBishopMoves(GameState state, ChessPosition position, ChessPiece piece) {
        throw new RuntimeException("Not implemented");
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
        throw new RuntimeException("Not implemented");
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
        ChessPosition epPrevRight = new ChessPosition(currRank + (2 * advanceDirection), currFile + 1);
        ChessPosition epPrevLeft  = new ChessPosition(currRank + (2 * advanceDirection), currFile - 1);

        ChessPosition[] captures = {capLeft, capRight};
        ChessPosition[] epcaptures = {epLeft, epRight};


        // pushing
        if (isOnBoard(advOne) && state.getPiece(advOne) == null) {
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
            if (state.getPiece(passedSquare) != null && state.getPiece(passedSquare).getTeamColor() != color) {
                ChessMove lastMove = state.getLastMove();
                if (lastMove.endPosition == passedSquare &&
                        lastMove.piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                    ChessPosition epPrev = new ChessPosition(currRank + (2 * advanceDirection), passedSquare.getFile());
                    if (lastMove.startPosition == epPrev) {
                        ChessMove nextMove = new ChessMove.MoveBuilder(piece, position, passedSquare)
                                .isCapture().enPassant().build();
                        moves.add(nextMove);
                    }
                }
            }
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

    /**
     * Gets available sliding moves in horizontal and vertical directions
     *
     * @param state         The current chess board
     * @param position      The starting position to calculate moves from
     * @param maxDist       Max number of squares the piece is allowed to move
     * @return              A collection of ChessMoves available to the position in the given direction.
     */
    private Collection<ChessMove> getOrthogonalSlidePositions(GameState state, ChessPosition position, int maxDist) {
        HashSet <ChessMove> moves = new HashSet<ChessMove>();
        moves.addAll(getSlideMoves(state, position, 1, 0, maxDist));
        moves.addAll(getSlideMoves(state, position, -1, 0, maxDist));
        moves.addAll(getSlideMoves(state, position, 0, 1, maxDist));
        moves.addAll(getSlideMoves(state, position, 0, -1, maxDist));
        return moves;
    }

    /**
     * Gets available sliding moves in diagonal directions
     *
     * @param state         The current chess board
     * @param position      The starting position to calculate moves from
     * @param maxDist       Max number of squares the piece is allowed to move
     * @return              A collection of ChessMoves available to the position in the given direction.
     */
    private Collection<ChessMove> getDiagonalSlidePositions(GameState state, ChessPosition position, int maxDist) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        moves.addAll(getSlideMoves(state, position, 1, 1, maxDist));
        moves.addAll(getSlideMoves(state, position, -1, 1, maxDist));
        moves.addAll(getSlideMoves(state, position, -1, -1, maxDist));
        moves.addAll(getSlideMoves(state, position, 1, -1, maxDist));
        return moves;
    }

    /**
     * Gets positions available by sliding in one direction
     * <p>
     * to find moves diagonally increasing in rank and decreasing in file, call getSlideMoves(board, position, 1, -1).
     * @param state         The current chess board
     * @param position      The starting position to calculate moves from
     * @param rankIteration How many ranks over to move at a time (-1, 0, or 1)
     * @param fileIteration How many ranks over to move at a time (-1, 0, or 1)
     * @return              A collection of ChessMoves available to the position in the given direction.
     */
    private Collection<ChessMove> getSlideMoves(
            GameState state,
            ChessPosition position,
            int rankIteration,
            int fileIteration,
            int maxDist) {
        int currRank = position.getRank();
        int currFile = position.getFile();
        HashSet<ChessPosition> availablePositions = new HashSet<ChessPosition>();
        HashSet<ChessMove> moves = new HashSet<ChessMove>();

        int i = 1;
        while (i <= maxDist) {
            ChessPosition nextPosition = new ChessPosition(currRank + (i * rankIteration), currFile + (i * fileIteration));
            if (!isOnBoard(nextPosition)) break;
            // empty squares
            if (state.getPiece(nextPosition) == null){
                availablePositions.add(nextPosition);
                state.board().highlightPosition(nextPosition, ChessBoard.Highlight.PRIMARY);
            }
            // captures
            else if (state.getPiece(nextPosition).getTeamColor() != state.getPiece(position).getTeamColor()) {
                availablePositions.add(nextPosition);
                state.board().highlightPosition(nextPosition, ChessBoard.Highlight.SECONDARY);
                break; // Don't search past capture for sliding piece.
            } else {
                break;
            }
            i++;
        }
        return moves;
    }

    private boolean isOnBoard(ChessPosition position) {
        return position.getRank() > BOARD_SIZE || position.getRank() < 1 ||
                position.getFile() > BOARD_SIZE || position.getFile() < 1;
    }

    /**
     * Returns a collection of ChessMoves with appropriate tags
     * from a starting position and a collection of ChessPositions
     * <p>
     * @param startPosition The position the piece will move from
     * @param positions     The collection of positions the piece can move to
     * @return              A collection of ChessMove objects
     */
    private Collection<ChessMove> getMovesFromPositions(
            GameState state,
            ChessPosition startPosition,
            Collection<ChessPosition> positions) {

        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        for (ChessPosition endPosition : positions) {
            moves.add(new ChessMove(startPosition, endPosition, null));
        }
        return moves;
    }
}

