package chess;

public class ChessParser {
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
        String fileCodes = "abcdefgh";
        String rankCodes = "12345678";
        algebra = pareString(algebra, fileCodes + rankCodes);
        if (algebra.length() < 2 ||
                fileCodes.indexOf(algebra.charAt(0)) == -1 ||
                rankCodes.indexOf(algebra.charAt(1)) == -1) {
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
