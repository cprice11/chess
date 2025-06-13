package ui;

import chess.*;

import java.util.Arrays;
import java.util.Hashtable;

public class ConsolePrinter {
    private String messageString = "";
    private ChessGame game;
    private ChessBoard board;
    private String gameState;
    private final String[] boardRows = new String[10];
    private ChessGame.TeamColor teamOrientation = ChessGame.TeamColor.WHITE;
    private final ChessColor theme = new ChessColor();
    private Hashtable<ChessPiece, String> pieceCharacters = PieceCharacters.SOLID_SYMBOLS;
    private final String[] rows = new String[14];

    public ConsolePrinter(ChessGame game) {
        this.game = game;
        board = game.getBoard();
        board.resetHighlights();
    }

    public void setMessageString(String message) {
        messageString = message;
    }

    public void setGame(ChessGame game) {
        this.game = game;
        board = this.game.getBoard();
        board.resetHighlights();
    }

    //Customization methods
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
        System.out.println(ChessColor.ERASE_SCREEN);
        for (String row : rows) {
            System.out.println(row);
        }
    }

    private void setGameStateStrings() {
        ChessColor titleColor = theme.darkText().lightSquare();
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
        } else if (game.isGameOver()) {
            titleText = " Resignation ";
        } else {
            titleColor.darkText().secondaryHighlight().lightSquare();
            titleText = " Turn: " + game.getTeamTurn() + " ";
            if (game.isInCheck(teamOrientation)) {
                titleColor.ternaryHighlight();
                titleText += "IN CHECK ";
            }
        }
        game.getBoard().resetHighlights();
        gameState = titleColor + titleText;
    }

    private void setRows() {
        String backgroundColor = theme.noSquare().noHighlight().lightText().toString();
        Arrays.fill(rows, backgroundColor + "   ");
        rows[1] = rows[1] + gameState + backgroundColor + "\t\t" + messageString + backgroundColor;
        for (int i = 0; i < 10; i++) {
            rows[i + 3] += boardRows[i] + backgroundColor;
        }
        rows[rows.length - 1] += '\n' + ChessColor.RESET;
    }

    private void setBoardRows() {
        int topRight = teamOrientation == ChessGame.TeamColor.WHITE ? 8 : 1;
        int bottomLeft = teamOrientation == ChessGame.TeamColor.WHITE ? 1 : 8;
        int rankIteration = teamOrientation == ChessGame.TeamColor.WHITE ? -1 : 1;
        int fileIteration = -rankIteration;
        String topBottomLabel;
            topBottomLabel = rankIteration == -1 ?
                    "    a  b  c  d  e  f  g  h   " :
                    "    h  g  f  e  d  c  b  a   ";
        boardRows[0] = topBottomLabel;
        for (int row = 0; row < 8; row++) {
            int rank = topRight + (row * rankIteration);
            StringBuilder rowString = new StringBuilder();
            String rowLabel = " " + rank + " ";
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
