import chess.ChessColor;
import chess.ChessGame;
import chess.ChessPiece;
import server.Server;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessColor useAllColors = new ChessColor();
        useAllColors.primaryText().noBackground();
        System.out.println(useAllColors + "♕ 240 Chess Server: " + piece + useAllColors.getResetString());
        Server server = new Server();
        server.run(8765);
    }
}