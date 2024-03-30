package chess;

import java.util.Collection;

public interface RuleSet {
    ChessBoard getStartingGameBoard();
    GameState getStartingGameState();
    Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition position);
    boolean isBoardValid(ChessBoard board);
    boolean isInCheck(ChessBoard board, ChessGame.TeamColor color);
    boolean isInCheckmate(ChessBoard board, ChessGame.TeamColor color);
    boolean isInStalemate(ChessBoard board, ChessGame.TeamColor color);
}
