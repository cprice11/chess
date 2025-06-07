package ui;

public class PreLogin implements Client {
    private static final String help = "I'm the pre-login help message.";

    public PreLogin(String serverUrl, Repl repl) {

    }

    public String eval(String input) {
        System.out.println(input);
        throw new RuntimeException("Not implemented yet");
    }

    public String help() {
        return help;
    }
}
