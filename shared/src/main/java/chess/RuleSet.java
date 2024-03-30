package chess;

import java.util.Collection;

public interface RuleSet {
    GameState getStartingGameState();
    Collection<ChessMove> getValidMoves(GameState state, ChessPosition position);
    boolean isBoardValid(GameState state);
    boolean isInCheck(GameState state, ChessGame.TeamColor color);
    boolean isInCheckmate(GameState state, ChessGame.TeamColor color);
    boolean isInStalemate(GameState state, ChessGame.TeamColor color);
}
