package ui;

import chess.ChessGame;
import chess.ChessPiece;

import java.util.Hashtable;

public class PieceCharacters {
    public static final Hashtable<ChessPiece, String> SOLID_SYMBOLS = new Hashtable<>() {
        {
            put(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING), "\uDB82\uDC57");
            put(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN), "\uDB82\uDC5A");
            put(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP), "\uDB82\uDC5C");
            put(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT), "\uDB82\uDC58");
            put(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK), "\uDB82\uDC5B");
            put(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN), "\uDB82\uDC59");
            put(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.EN_PASSANT), " ");
            put(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING), "\uDB82\uDC57");
            put(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN), "\uDB82\uDC5A");
            put(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP), "\uDB82\uDC5C");
            put(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT), "\uDB82\uDC58");
            put(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK), "\uDB82\uDC5B");
            put(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN), "\uDB82\uDC59");
            put(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.EN_PASSANT), " ");
        }
    };
    public static final Hashtable<ChessPiece, String> LETTER_SYMBOLS = new Hashtable<>() {
        {
            put(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING), "K");
            put(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN), "Q");
            put(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP), "B");
            put(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT), "N");
            put(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK), "R");
            put(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN), "P");
            put(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.EN_PASSANT), " ");
            put(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING), "k");
            put(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN), "q");
            put(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP), "b");
            put(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT), "n");
            put(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK), "r");
            put(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN), "p");
            put(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.EN_PASSANT), " ");
        }
    };
}
