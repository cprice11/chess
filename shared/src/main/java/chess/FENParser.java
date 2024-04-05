package chess;

import java.util.Vector;

public class FENParser extends ChessParser {
    public GameState parseFEN(String fen) {
        GameState state = new GameState();
        String[] parts = fen.split(" ");
        if (parts.length < 6) {
            return state;
        }

        String[] rows = parts[0].split("/");
        if (rows.length != 8) {
            return state;
        }

        state.board().clearBoard();
        for (int i = 0; i < 8; i++) {
            int index = 0;
            for (char c : rows[i].toCharArray()) {
                if (Character.isDigit(c)) {
                    int emptySquares = Character.getNumericValue(c);
                    index += emptySquares;
                } else {
                    state.board().addPiece(new ChessPosition(8 - i, index + 1), pieceFromChar(c));
                    index += 1;
                }
            }
        }

        ChessGame.TeamColor teamToMove = (parts[1].equals("w")) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        state.turn(teamToMove);

        for (char c : parts[2].toCharArray()) {
            switch (c) {
                case 'K' -> state.whiteCanCastleShort(true);
                case 'Q' -> state.whiteCanCastleLong(true);
                case 'k' -> state.blackCanCastleShort(true);
                case 'q' -> state.blackCanCastleLong(true);
            }
        }

        ChessPosition enPassant = (parts[3].equals("-")) ? null : parseAlgebraPosition(parts[3]);
        if (enPassant != null) state.setEnPassant(enPassant);

        state.setHalfMoveClock(Integer.parseInt(parts[4]));
        state.setFullMoveClock(Integer.parseInt(parts[5]));
        return state;
    }

    public String getFen(GameState state) {
        String[] fenData = new String[6];
        ChessPiece[][] pieces = state.board().getPositions();

        // Piece placement
        Vector<String> rowStrings = getRowStrings(pieces);
        fenData[0] = String.join("/", rowStrings);

        fenData[1] = (state.turn() == ChessGame.TeamColor.WHITE) ? "w" : "b";

        StringBuilder castlingInfo = new StringBuilder();
        // FIXME FEN parser doesn't give castle options
//        if (state.whiteCanCastleShort()) castlingInfo.append('K');
//        if (state.whiteCanCastleLong()) castlingInfo.append('Q');
//        if (state.blackCanCastleShort()) castlingInfo.append('k');
//        if (state.blackCanCastleLong()) castlingInfo.append('q');
        if (state.board().equals(new ChessBoard())) castlingInfo.append("KQkq");
        if (castlingInfo.isEmpty()) castlingInfo.append('-');
        fenData[2] = castlingInfo.toString();

        ChessPosition enPassant = state.getEnPassant();
        fenData[3] = (enPassant == null) ? "-" : enPassant.toString();

        fenData[4] = String.valueOf(state.getHalfMoveClock());

        fenData[5] = String.valueOf(state.getFullMoveClock());

        return String.join(" ", fenData);
    }

    private static Vector<String> getRowStrings(ChessPiece[][] pieces) {
        Vector<String> rowStrings = new Vector<>();
        for (int i = pieces.length; i-- > 0; ) {
            int blanks = 0;
            StringBuilder rowString = new StringBuilder();
            for (ChessPiece piece : pieces[i]) {
                if (piece == null) {
                    blanks++;
                    continue;
                } else if (blanks > 0) {
                    rowString.append(blanks);
                    blanks = 0;
                }
                rowString.append(piece.getCode());
            }
            if (blanks > 0) rowString.append(blanks);
            rowStrings.add(rowString.toString());
        }
        return rowStrings;
    }
}

