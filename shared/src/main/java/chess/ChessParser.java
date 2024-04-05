package chess;

public class ChessParser {
    protected final int BOARD_SIZE = 8;
    protected final ChessPosition A1 = new ChessPosition(1, 1);
    protected final ChessPosition B1 = new ChessPosition(1, 2);
    protected final ChessPosition C1 = new ChessPosition(1, 3);
    protected final ChessPosition D1 = new ChessPosition(1, 4);
    protected final ChessPosition E1 = new ChessPosition(1, 5);
    protected final ChessPosition F1 = new ChessPosition(1, 6);
    protected final ChessPosition G1 = new ChessPosition(1, 7);
    protected final ChessPosition H1 = new ChessPosition(1, 8);
    protected final ChessPosition A8 = new ChessPosition(8, 1);
    protected final ChessPosition B8 = new ChessPosition(8, 2);
    protected final ChessPosition C8 = new ChessPosition(8, 3);
    protected final ChessPosition D8 = new ChessPosition(8, 4);
    protected final ChessPosition E8 = new ChessPosition(8, 5);
    protected final ChessPosition F8 = new ChessPosition(8, 6);
    protected final ChessPosition G8 = new ChessPosition(8, 7);
    protected final ChessPosition H8 = new ChessPosition(8, 8);

    protected String pareString(String input, String allowed) {
        StringBuilder cleanString = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (allowed.indexOf(c) != -1) {
                cleanString.append(c);
            }
        }
        return cleanString.toString();
    }

    protected ChessPosition parseAlgebraPosition(String algebra) {
        String FILE_CODES = "abcdefgh";
        String RANK_CODES = "12345678";
        algebra = pareString(algebra, FILE_CODES + RANK_CODES);
        if (algebra.length() < 2 ||
                FILE_CODES.indexOf(algebra.charAt(0)) == -1 ||
                RANK_CODES.indexOf(algebra.charAt(1)) == -1) {
            throw new RuntimeException("couldn't parse move notation");
        }
        int rank = Character.getNumericValue(algebra.charAt(1));
        int file = getFileNum(algebra.charAt(0));
        return new ChessPosition(rank, file);
    }

    protected int getFileNum(char file) {
        assert Character.isAlphabetic(file);
        file = Character.toUpperCase(file);
        return (int) file - 64;
    }

    protected ChessPiece.PieceType pieceTypeFromChar(char code) {
        return switch (Character.toUpperCase(code)) {
            case 'K' -> ChessPiece.PieceType.KING;
            case 'Q' -> ChessPiece.PieceType.QUEEN;
            case 'B' -> ChessPiece.PieceType.BISHOP;
            case 'N' -> ChessPiece.PieceType.KNIGHT;
            case 'R' -> ChessPiece.PieceType.ROOK;
            case 'P' -> ChessPiece.PieceType.PAWN;
            default -> null;
        };
    }

    protected ChessPiece pieceFromChar(char code) {
        ChessGame.TeamColor color = (Character.isUpperCase(code)) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        ChessPiece.PieceType type = pieceTypeFromChar(code);
        return new ChessPiece(color, type);
    }
}
