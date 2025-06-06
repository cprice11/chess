package ui;

public class PostLogin implements Client {
    private static final String help = "I'm the post-login help message.";

    public PostLogin(String serverUrl, Repl repl) {

    }

    public String eval(String input) {
        throw new RuntimeException("Not implemented yet");
    }

    public String help() {
        return help;
    }
}
