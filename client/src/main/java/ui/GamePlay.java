package ui;

public class GamePlay implements Client {
    private static final String help = "I'm the gameplay help message.";

    public GamePlay(String serverUrl, Repl repl) {

    }

    public String eval(String input) {
        throw new RuntimeException("Not implemented yet");
    }

    public String help() {
        return help;
    }
}
