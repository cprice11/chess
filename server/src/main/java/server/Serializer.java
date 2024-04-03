package server;

import chess.ChessGame;
import com.google.gson.Gson;

public class Serializer {
//    You will be using the Gson library for serialization and deserialization. Gson can take a Java Object and convert
//    its contents to a JSON string. In the other direction, Gson can take a JSON string and a class type, and create a
//    new instance of that class with any matching fields being initialized from the JSON string. For this process to
//    work properly, the field names in your Request and Result classes must match exactly the property names in the
//    JSON strings, including capitalization.
//
//    Here is an example of using Gson to serialize and deserialize a ChessGame.

    public static void serialize() {
        var serializer = new Gson();
        var game = new ChessGame();

        // serialize to JSON
        var json = serializer.toJson(game);

        // deserialize back to ChessGame
        game = serializer.fromJson(json, ChessGame.class);
    }

//    We install the third party package already in your project as part of its initial configuration and so you are
//    ready to start using Gson in your code.
}