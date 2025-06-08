package ui;

import chess.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class ConsolePrinter {
    private ChessGame game;
    private ChessBoard board;
    private ChessGame.TeamColor turn;
    private ArrayList<ChessMove> moveList;
    private String gameState;
    private final String[] boardRows = new String[10];
    private ChessGame.TeamColor teamOrientation = ChessGame.TeamColor.WHITE;
    private ChessColor theme = new ChessColor();
    private Hashtable<ChessPiece, String> pieceCharacters = PieceCharacters.solidSymbols;
    private boolean showLabels = true;
    private final String[] rows = new String[14];

    public ConsolePrinter(ChessGame game) {
        this.game = game;
        board = game.getBoard();
    }

    /*
     * Customization methods
     */
    public ConsolePrinter setTheme(ChessColor.ColorPalette palette) {
        this.theme.setColorPalette(palette);
        return this;
    }

    public ConsolePrinter fromBlack() {
        teamOrientation = ChessGame.TeamColor.BLACK;
        return this;
    }

    public ConsolePrinter fromWhite() {
        teamOrientation = ChessGame.TeamColor.WHITE;
        return this;
    }

    public ConsolePrinter setPieceCharacters(Hashtable<ChessPiece, String> characterMap) {
        this.pieceCharacters = characterMap;
        return this;
    }

    public ConsolePrinter showLabels() {
        showLabels = true;
        return this;
    }

    public ConsolePrinter hideLabels() {
        showLabels = false;
        return this;
    }

    /**
     * Prints the game to the console
     * It won't necessarily be square depending on zoom level, which terminal it is run in and what font is used
     * This works right now with a nerdfont downloaded within the intellij powershell terminal.
     * Your mileage may vary
     */
    public void print() {
        setBoardRows();
        setGameStateStrings();
        setRows();
        for (String row : rows) {
            System.out.println(row);
        }
        System.out.println("UI");
    }

    private void setGameStateStrings() {
        ChessColor titleColor = theme.lightText();
        String titleText;
        if (game.isInCheckmate()) {
            titleColor.primaryHighlight();
            if (game.isInCheckmate(teamOrientation)) {
                titleColor.errorHighlight();
            }
            if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                titleText = " White wins by checkmate ";
            } else {
                titleText = " Black wins by checkmate ";
            }
        } else if (game.isInStalemate(ChessGame.TeamColor.WHITE) || game.isInStalemate(ChessGame.TeamColor.BLACK)) {
            titleColor.errorHighlight();
            titleText = " Draw by stalemate ";
        } else {
            titleColor.secondaryHighlight();
            titleText = " Turn: " + game.getTeamTurn() + " ";
            if (game.isInCheck(teamOrientation)) {
                titleColor.ternaryHighlight();
                titleText += "IN CHECK ";
            }
        }
        gameState = titleColor + titleText;
//
//        List<ChessMove> blackMovesHistory = new ArrayList<>();
//        List<ChessMove> whiteMovesHistory = new ArrayList<>();
//        List<String> history = new ArrayList<>();
//        history.add(color.secondaryHighlight().darkText() + " History ");
//        int longestWhiteHistoryRow = 3;
//        int longestBlackHistoryRow = 3;
//        int longestTurnNumber = 1;
//        int startIndex = Math.max(numMoves - 10, 0);
//        startIndex -= startIndex % 2;
//        ChessColor boxColor = new ChessColor();
//        boxColor.noHighlight().setForeground(color.getColorPalette().lightSecondary());
//        int moveNumber = (startIndex / 2) + 1;
//        for (int i = 0; i < 5; i++) {
//            int moveIndex = startIndex + (i * 2);
//            if (moveIndex >= numMoves) {
//                break;
//            }
//            whiteMovesHistory.addLast(moveHistory.get(moveIndex));
//            if (moveIndex + 1 >= numMoves) {
//                break;
//            }
//            blackMovesHistory.addLast(moveHistory.get(moveIndex + 1));
//        }
//        for (int i = 0; i < whiteMovesHistory.size(); i++) {
//            int moveLength = whiteMovesHistory.get(i).toString().length();
//            longestWhiteHistoryRow = Math.max(moveLength, longestWhiteHistoryRow);
//            if (i < blackMovesHistory.size()) {
//                longestBlackHistoryRow = Math.max(moveLength, longestBlackHistoryRow);
//            }
//            longestTurnNumber = String.valueOf(moveNumber + i).length();
//        }
//        int historyRowSize = longestTurnNumber + longestWhiteHistoryRow + longestBlackHistoryRow + 4;
//        history.set(0, history.getFirst() + " ".repeat(historyRowSize - 9) + color.noHighlight());
//        for (int i = 0; i < whiteMovesHistory.size(); i++) {
//            StringBuilder whiteMove = new StringBuilder(whiteMovesHistory.get(i).toString());
//            whiteMove.append(" ".repeat(longestWhiteHistoryRow - whiteMove.length()));
//            StringBuilder blackMove = new StringBuilder();
//            if (i < blackMovesHistory.size()) {
//                blackMove.append(blackMovesHistory.get(i).toString());
//            } else {
//                blackMove.append(" - ");
//            }
//            blackMove.append(" ".repeat(longestBlackHistoryRow - blackMove.length()));
//            String moveNumberString = String.valueOf(moveNumber + i);
//            String historyRow = boxColor + "┃" + moveNumberString +
//                    " ".repeat(longestTurnNumber - moveNumberString.length()) + "│" +
//                    color.noHighlight().lightText() + whiteMove + boxColor + "│" +
//                    color.noHighlight().lightText() + blackMove + boxColor + "┃" +
//                    color.noHighlight();
//            history.add(historyRow);
//        }
//
//        StringBuilder historyFooter = new StringBuilder();
//        color.noHighlight().setForeground(color.getColorPalette().lightSecondary());
//        historyFooter.append(color);
//        historyFooter.append("┗").append("━".repeat(longestTurnNumber)).append("┷")
//                .append("━".repeat(longestWhiteHistoryRow)).append("┷").append("━".repeat(longestBlackHistoryRow))
//                .append("┛");
//        historyFooter.append(color.noHighlight().lightText());
//        history.addLast(historyFooter.toString());
//        if (history.size() > 2) {
//            for (int i = 0; i < history.size(); i++) {
//                int rowIndex = i + 3;
//                rows.set(rowIndex, rows.get(rowIndex) + history.get(i));
//            }
//        }
    }

    private void setRows() {
        String backgroundColor = theme.noSquare().noHighlight().lightText().toString();
        Arrays.fill(rows, backgroundColor + "   ");
        rows[1] = rows[1] + gameState + backgroundColor;
        for (int i = 0; i < 10; i++) {
            rows[i + 3] += boardRows[i] + backgroundColor;
        }
        rows[rows.length - 1] += '\n' + theme.getResetString();
    }

    private void setBoardRows() {
        int topRight = teamOrientation == ChessGame.TeamColor.WHITE ? 8 : 1;
        int bottomLeft = teamOrientation == ChessGame.TeamColor.WHITE ? 1 : 8;
        int rankIteration = teamOrientation == ChessGame.TeamColor.WHITE ? -1 : 1;
        int fileIteration = -rankIteration;
        String topBottomLabel;
        if (showLabels) {
            topBottomLabel = rankIteration == -1 ?
                    "    a  b  c  d  e  f  g  h   " :
                    "    h  g  f  e  d  c  b  a   ";
        } else {
            topBottomLabel = "                             ";
        }
        boardRows[0] = topBottomLabel;
        for (int row = 0; row < 8; row++) {
            int rank = topRight + (row * rankIteration);
            StringBuilder rowString = new StringBuilder();
            String rowLabel = showLabels ? " " + rank + " " : "   ";
            rowString.append(rowLabel);
            for (int file = bottomLeft; file <= 8 && file >= 1; file += fileIteration) {
                rowString.append(getBoardSquare(rank, file));
            }
            rowString.append(theme.noHighlight().noSquare().lightText());
            rowString.append(rowLabel);
            boardRows[row + 1] = rowString.toString();
        }
        boardRows[9] = topBottomLabel;
    }

    private String getBoardSquare(int rank, int file) {
        StringBuilder square = new StringBuilder();
        ChessPosition position = new ChessPosition(rank, file);
        ChessPiece piece = board.getPiece(position);
        ChessColor squareColor = new ChessColor().darkSquare();
        if ((rank + file) % 2 == 1) {
            squareColor.lightSquare();
        }
        squareColor.lightPiece();
        if (piece != null && piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            squareColor.darkPiece();
        }
        ChessColor.Highlight highlight = board.getHighlight(position);
        squareColor = switch (highlight) {
            case NONE -> squareColor.noHighlight();
            case PRIMARY -> squareColor.primaryHighlight();
            case SECONDARY -> squareColor.secondaryHighlight();
            case TERNARY -> squareColor.ternaryHighlight();
            case ERROR -> squareColor.errorHighlight();
            case null -> squareColor;
        };
        square.append(squareColor);
        String pieceCharacter = piece == null ? "   " : " " + pieceCharacters.get(piece) + " ";
        square.append(pieceCharacter);
        return square.toString();
    }
}
