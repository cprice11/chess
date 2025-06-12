package datamodels;

import chess.ChessMove;

import java.util.ArrayList;

public record DenseGame(String fen, ArrayList<ChessMove> history) {
}
