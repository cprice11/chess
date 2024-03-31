package chess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FENParserTest {

    @Test
    void parseFEN() {
        String START_FEN = "3K2B1/6Q1/P3ppq1/7P/7n/1N1k1PB1/1P1P3p/8 w - - 0 1";
        FENParser parser = new FENParser();
        GameState state  = parser.parseFEN(START_FEN);
        ChessGame game = new ChessGame(state);
        game.state().prettyPrint();
        String fen = parser.getFen(game.state());
        assert (START_FEN.equals(fen));
    }

}