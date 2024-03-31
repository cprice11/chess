package chess;

import java.util.Vector;

public class ChessMain {
    // public static ChessGame game = new ChessGame();
    public static void main (String[] args) {
        String mcDonnalsMatch = "23.Rxd3 c4 24.Rc3 a5 25.Bd6 Rfd8 26.Ba3 a4 27.Kf1 Rdb8 28.Nd2 f5 29.Rc1 Rc8\n" +
                "30.Nb1 Rab8 31.Nc3 Rb3 32.Nxd5 Ra8 33.Rc3 Ra5 34.Bc5 a3 35.Bxa3 Rxd5 36.Rxc4 Rxa3\n" +
                "37.Ke2 Ra2+ 38.Kf3 Rb5 39.Rc8+ Kf7 40.Rc7+ Kg6 41.Rc6+ Kh5 42.h3 Rbb2 43.g4+ fxg4+\n" +
                "44.hxg4+ Kh4 45.Rc7 Rxf2+ 46.Ke4 g5 47.Rxh7+ Kxg4 48.d5 Rf6 49.Re7 Rf5 50.Rd7 Ra4+\n" +
                "51.Kd3 Re5 52.d6 Ra3+ 53.Kd4 Raxe3 54.Rb7 R3e4+ 55.Kd3 Rf4 56.d7 Rd5+ 57.Ke3 Rfd4  0-1\n" +
                "\n" +
                "[Event \"Paris\"]\n" +
                "[Site \"Paris\"]\n" +
                "[Date \"1900.??.??\"]\n" +
                "[Round \"?\"]\n" +
                "[White \"Brody, Miklos\"]\n" +
                "[Black \"Marshall, Frank James\"]\n" +
                "[Result \"0-1\"]\n" +
                "[WhiteElo \"\"]\n" +
                "[BlackElo \"\"]\n" +
                "[ECO \"C43\"]\n" +
                "\n" +
                "1.e4 e5 2.Nf3 Nf6 3.d4 Nxe4 4.Bd3 d5 5.Nxe5 Bd6 6.O-O O-O 7.c4 c6 8.Qc2 Re8\n" +
                "9.cxd5 cxd5 10.Bxe4 dxe4 11.Qxe4 f6 12.Qd5+ Re6 13.f4 Bc7 14.Qxd8+ Bxd8 15.Nf3 Bb6\n" +
                "16.Kh1 Rd6 17.Nc3 Bg4 18.d5 Na6 19.h3 Bxf3 20.Rxf3 Nb4 21.b3 Nxd5 22.Rd3 Rad8\n" +
                "23.Ba3 R6d7 24.Nxd5 Rxd5 25.Rxd5 Rxd5 26.Bb2 Rd2 27.Bc3 Rf2 28.g3 Kf7 29.b4 Kg6\n" +
                "30.a4 Rf3 31.Be1 Bd4 32.Rd1 Rf1+ 33.Kg2 Rg1+ 34.Kh2 Be3 35.g4 h5 36.f5+ Kh6\n" +
                "37.a5 hxg4 38.hxg4 Kg5 39.Kh3 Kf4 40.b5 Kf3 41.Kh2 Rf1  0-1\n" +
                "\n" +
                "[Event \"Paris\"]\n" +
                "[Site \"Paris\"]\n" +
                "[Date \"1900.??.??\"]\n" +
                "[Round \"?\"]\n" +
                "[White \"Chigorin, Mikhail\"]\n" +
                "[Black \"Marshall, Frank James\"]\n" +
                "[Result \"1/2-1/2\"]\n" +
                "[WhiteElo \"\"]\n" +
                "[BlackElo \"\"]\n";

        AlgebraParser parser = new AlgebraParser();
        GameState state = parser.parsePGN(mcDonnalsMatch);
        ChessGame game = new ChessGame(state);
        game.state.board().pprintBoard();
    }
}