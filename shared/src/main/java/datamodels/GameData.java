package datamodels;

import chess.ChessGame;

public record GameData(int gameID, String blackUsername, String whiteUsername, String gameName, ChessGame game) {
}
