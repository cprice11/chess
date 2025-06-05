package chess;

import java.util.ArrayList;
import java.util.List;

public class ConsolePrinter {

    /**
     * Prints the game to the console
     */
    public static void printGame(ChessGame game) {
        ChessBoard board = game.getBoard();
        ChessColor color = new ChessColor();
        ArrayList<ChessMove> moveHistory = game.moveHistory();
        int numMoves = moveHistory.size();
        List<String> rows = board.prettyRows();
        String gameState;
        if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            gameState = " White wins by checkmate ";
        } else if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
            gameState = " Black wins by checkmate ";
        } else if (game.isInStalemate(ChessGame.TeamColor.WHITE) || game.isInStalemate(ChessGame.TeamColor.BLACK)) {
            gameState = " Draw by stalemate ";
        } else {
            gameState = " Turn: " + game.getTeamTurn() + " ";
        }
        String turn = rows.get(1) + color.secondaryHighlight().darkText() + gameState + color.noHighlight();
        rows.set(1, turn);

        List<ChessMove> blackMovesHistory = new ArrayList<>();
        List<ChessMove> whiteMovesHistory = new ArrayList<>();
        List<String> history = new ArrayList<>();
        history.add(color.secondaryHighlight().darkText() + " History ");
        int longestWhiteHistoryRow = 3;
        int longestBlackHistoryRow = 3;
        int longestTurnNumber = 1;
        int startIndex = Math.max(numMoves - 10, 0);
        startIndex -= startIndex % 2;
        ChessColor boxColor = new ChessColor();
        boxColor.noHighlight().setForeground(color.getColorPalette().lightSecondary());
        int moveNumber = (startIndex / 2) + 1;
        for (int i = 0; i < 5; i++) {
            int moveIndex = startIndex + (i * 2);
            if (moveIndex >= numMoves) {
                break;
            }
            whiteMovesHistory.addLast(moveHistory.get(moveIndex));
            if (moveIndex + 1 >= numMoves) {
                break;
            }
            blackMovesHistory.addLast(moveHistory.get(moveIndex + 1));
        }
        for (int i = 0; i < whiteMovesHistory.size(); i++) {
            int moveLength = whiteMovesHistory.get(i).toString().length();
            longestWhiteHistoryRow = Math.max(moveLength, longestWhiteHistoryRow);
            if (i < blackMovesHistory.size()) {
                longestBlackHistoryRow = Math.max(moveLength, longestBlackHistoryRow);
            }
            longestTurnNumber = String.valueOf(moveNumber + i).length();
        }
        int historyRowSize = longestTurnNumber + longestWhiteHistoryRow + longestBlackHistoryRow + 4;
        history.set(0, history.getFirst() + " ".repeat(historyRowSize - 9) + color.noHighlight());
        for (int i = 0; i < whiteMovesHistory.size(); i++) {
            StringBuilder whiteMove = new StringBuilder(whiteMovesHistory.get(i).toString());
            whiteMove.append(" ".repeat(longestWhiteHistoryRow - whiteMove.length()));
            StringBuilder blackMove = new StringBuilder();
            if (i < blackMovesHistory.size()) {
                blackMove.append(blackMovesHistory.get(i).toString());
            } else {
                blackMove.append(" - ");
            }
            blackMove.append(" ".repeat(longestBlackHistoryRow - blackMove.length()));
            String moveNumberString = String.valueOf(moveNumber + i);
            String historyRow = boxColor + "┃" + moveNumberString +
                    " ".repeat(longestTurnNumber - moveNumberString.length()) + "│" +
                    color.noHighlight().lightText() + whiteMove + boxColor + "│" +
                    color.noHighlight().lightText() + blackMove + boxColor + "┃" +
                    color.noHighlight();
            history.add(historyRow);
        }

        StringBuilder historyFooter = new StringBuilder();
        color.noHighlight().setForeground(color.getColorPalette().lightSecondary());
        historyFooter.append(color);
        historyFooter.append("┗").append("━".repeat(longestTurnNumber)).append("┷")
                .append("━".repeat(longestWhiteHistoryRow)).append("┷").append("━".repeat(longestBlackHistoryRow))
                .append("┛");
        historyFooter.append(color.noHighlight().lightText());
        history.addLast(historyFooter.toString());
        if (history.size() > 2) {
            for (int i = 0; i < history.size(); i++) {
                int rowIndex = i + 3;
                rows.set(rowIndex, rows.get(rowIndex) + history.get(i));
            }
        }
        for (String row : rows) {
            System.out.println(row);
        }
    }
}
