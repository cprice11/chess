package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlgebraParser extends ChessParser {
    private final String PIECE_CODES = "KQBNR";
    private final String FILE_CODES = "abcdefgh";
    private final String RANK_CODES = "12345678";
    private final String CASTLE_CODES = "0oO";
    private final String SEPARATION_CODES = "-xX:";
    private final String CHECK_SIGNIFIERS = "#+";
    private final String PROMOTION_CODES = "QBNR";
    private final String VALID_START_CHARS = PIECE_CODES + FILE_CODES + CASTLE_CODES;


    private final ChessMove.MoveBuilder WHITE_SHORT_CASTLE = new ChessMove.MoveBuilder(E1, H1)
            .shortCastle();
    private final ChessMove.MoveBuilder WHITE_LONG_CASTLE = new ChessMove.MoveBuilder(E1, A1)
            .longCastle();
    private final ChessMove.MoveBuilder BLACK_SHORT_CASTLE = new ChessMove.MoveBuilder(E8, H8)
            .shortCastle();
    private final ChessMove.MoveBuilder BLACK_LONG_CASTLE = new ChessMove.MoveBuilder(E8, A8)
            .longCastle();

    private enum TOKEN_TYPE {
        WHITE,
        TAG,
        DIGIT,
        PERIOD,
        PIECE,
        UNKNOWN,
        OPEN_COMMENT,
        CLOSE_COMMENT,
        DATA,
        COMMENT,
        ROUND,
        FILE,
        SEPERATOR,
        CASTLE,
        CHECK_SIGNIFIER
    };


    // I'm only going to support standard long notation because I can't be bothered.
    GameState parsePGN(String pgnString) {
        Vector<AlgebraToken> tokens = new Vector<AlgebraToken>();
        int parseIndex = 0;
        String data = "\0";
        while(parseIndex < pgnString.length()) {
            char c = pgnString.charAt(parseIndex);
            System.out.println("SYMBOL\t" + c);
            if (tokens.size() > 0) System.out.println("TOKEN\t" + tokens.lastElement().value);
            System.out.println("UPNEXT\t" + pgnString.substring(parseIndex, Math.min(50 + parseIndex, pgnString.length())));
            if (Character.isWhitespace(c)) {
                if (data.length() > 0) {
                    tokens.add(new AlgebraToken(TOKEN_TYPE.UNKNOWN, data));
                    data = "\0";
                }
                tokens.add(new AlgebraToken(TOKEN_TYPE.WHITE, String.valueOf(c)));
                while (parseIndex < pgnString.length()) {
                    if (Character.isWhitespace(pgnString.charAt(parseIndex))) {
                        parseIndex++;
                    } else break;
                }
                continue;
            }
            if (c == '[') {
                if (data.length() > 0) {
                    tokens.add(new AlgebraToken(TOKEN_TYPE.UNKNOWN, data));
                    data = "\0";
                }
                int end = pgnString.substring(parseIndex).indexOf(']');
                if (end == -1) throw new RuntimeException("Could not parse pgn");
                end += parseIndex + 1;
                tokens.add(new AlgebraToken(TOKEN_TYPE.TAG, pgnString.substring(parseIndex, end)));
                parseIndex = end;
                continue;
            }
            if (c == '{') {
                if (data.length() > 0) {
                    tokens.add(new AlgebraToken(TOKEN_TYPE.UNKNOWN, data));
                    data = "\0";
                }
                int end = pgnString.substring(parseIndex).indexOf('}');
                if (end == -1) throw new RuntimeException("Could not parse pgn");
                end += parseIndex + 1;
                tokens.add(new AlgebraToken(TOKEN_TYPE.COMMENT, pgnString.substring(parseIndex, end)));
                parseIndex = end;
                continue;
            }
            if (c == '.') {
                if (data.length() > 0) {
                    tokens.add(new AlgebraToken(TOKEN_TYPE.UNKNOWN, data));
                    data = "\0";
                }
                tokens.add(new AlgebraToken(TOKEN_TYPE.PERIOD, pgnString.substring(parseIndex, parseIndex + 1)));
                parseIndex++;
                continue;
            }

            /*
            if (Character.isDigit(c)) {
                tokens.add(new AlgebraToken(TOKEN_TYPE.DIGIT, pgnString.substring(parseIndex, parseIndex + 1)));
                parseIndex++;
                continue;
            }
            if (FILE_CODES.indexOf(c) != -1) {
                tokens.add(new AlgebraToken(TOKEN_TYPE.FILE, pgnString.substring(parseIndex, parseIndex + 1)));
                parseIndex++;
                continue;
            }
            if (PIECE_CODES.indexOf(c) != -1) {
                tokens.add(new AlgebraToken(TOKEN_TYPE.PIECE, pgnString.substring(parseIndex, parseIndex + 1)));
                parseIndex++;
                continue;
            }

            tokens.add(new AlgebraToken(TOKEN_TYPE.UNKNOWN, pgnString.substring(parseIndex, parseIndex + 1)));
             */
            data += String.valueOf(c);
            parseIndex++;
        }

        for (AlgebraToken datum : tokens) {
            System.out.println(datum.type + "\t" + datum.value);
        }


        GameState state = new GameState();
//        for (ChessMove move : moves) {
//            state.makeMove(move);
//        }
        return new GameState();
    }

    public ChessMove getMoveFromAlgebra(String algebra, ChessGame.TeamColor turn) {
        // Clean input
        algebra = pareString(algebra, "KQBNRabcdefgh123456780Oo-+#xX:");

        // Validate
        if (algebra.length() < 3) {
            throw new RuntimeException("Algebra notations is the wrong length");
        }
        char firstCharacter = algebra.charAt(0);
        if (VALID_START_CHARS.indexOf(firstCharacter) == -1) {
            throw new RuntimeException("Unexpected char in algebra move");
        }

        ChessMove.MoveBuilder possibleMove;
        boolean isCapture = false;
        ChessPiece.PieceType promotion = null;

        // Start Parse
        int parseIndex = 0;
        // Castling
        if (CASTLE_CODES.indexOf(firstCharacter) != -1) {
            if (isLongCastle(algebra)) {
                possibleMove = (turn == ChessGame.TeamColor.WHITE) ? WHITE_LONG_CASTLE : BLACK_LONG_CASTLE;
                parseIndex += 5;
            } else if (algebra.length() == 3){
                possibleMove = (turn == ChessGame.TeamColor.WHITE)? WHITE_SHORT_CASTLE: BLACK_SHORT_CASTLE;
                parseIndex += 3;
            } else throw new RuntimeException("couldn't parse move notation");

            parseIndex++;
            if (algebra.length() > 1 + parseIndex) {
                throw new RuntimeException("couldn't parse move notation");
            }
        } else {
            // get piece
            ChessPiece.PieceType type = pieceTypeFromChar(algebra.charAt(parseIndex));
            type = (type == null) ? ChessPiece.PieceType.PAWN : type;
            if (type != ChessPiece.PieceType.PAWN) parseIndex++;
            ChessPiece piece = new ChessPiece(turn, type);

            ChessPosition firstPosition = parseAlgebraPosition(algebra.substring(parseIndex));
            parseIndex += 2;


            // move seperator
            if (algebra.length() == parseIndex || SEPARATION_CODES.indexOf(algebra.charAt(parseIndex)) == -1) {
                throw new RuntimeException("couldn't parse move notation");
            }

            isCapture = (algebra.charAt(parseIndex) != '-');
            parseIndex += 1;

            // Second position
            ChessPosition secondPosition = parseAlgebraPosition(algebra.substring(parseIndex));
            parseIndex += 2;
            possibleMove = new ChessMove.MoveBuilder(firstPosition, secondPosition);
            if (algebra.length() > parseIndex && PROMOTION_CODES.indexOf(parseIndex) != -1){
                type = pieceTypeFromChar(algebra.charAt(parseIndex));
            }
        }

        if (algebra.length() == parseIndex || CHECK_SIGNIFIERS.indexOf(parseIndex) == -1){
            throw new RuntimeException("couldn't parse move notation");
        }
        char nextChar = algebra.charAt(parseIndex);
        if (nextChar == '+') possibleMove.isCheck();
        if (nextChar == '#') possibleMove.isMate();
        if (isCapture) possibleMove.isCapture();
        throw new RuntimeException("NOT IMPLIMENTED");
    }

    class AlgebraToken {
        TOKEN_TYPE type;
        String value;

        public TOKEN_TYPE getType() {
            return type;
        }
        public String getValue() {
            return value;
        }

        public AlgebraToken(TOKEN_TYPE type, String value) {
            this.type = type;
            this.value = value;
        }
    }

    private Collection<AlgebraToken> getTokens(String input) {
        Collection<AlgebraToken> tokens;
        throw new RuntimeException("NOT IMPLIMENTED");
    }

    private AlgebraToken getToken(String input) {
        throw new RuntimeException("NOT IMPLIMENTED");

    }

    private boolean isLongCastle(String algebra){
        return (algebra.length() >= 5 &&
                algebra.charAt(0) == algebra.charAt(2) &&
                algebra.charAt(0) == algebra.charAt(4) &&
                algebra.charAt(1) == '-' &&
                algebra.charAt(1) == algebra.charAt(3));
    }

    private boolean isMoveCastle(String algebra){
        return (algebra.length() >= 5 &&
                algebra.charAt(0) == algebra.charAt(2) &&
                algebra.charAt(0) == algebra.charAt(4) &&
                algebra.charAt(1) == '-' &&
                algebra.charAt(1) == algebra.charAt(3));
    }
}
