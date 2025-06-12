package ui;

import chess.ChessColor;
import serverfacade.ServerFacade;

import java.util.Scanner;

public abstract class Client {
    protected ServerFacade server;
    protected Repl repl;
    protected ChessColor color = new ChessColor();
    String help;
    String startMessage;

    abstract public String eval(String input);

    abstract public void handleServerMessage(String message);

    public void printHelp() {
        System.out.println(help);
    }

    public void printStartMessage() {
        System.out.println(startMessage);
    }

    protected String getLine(String prompt) {
        prompt = prompt == null ? "> " : prompt + ": ";
        System.out.print(new ChessColor().ternaryText() + prompt + ChessColor.RESET);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    protected void error(String message) {
        System.out.println("\t" + color.errorText().toString() + message + ChessColor.RESET);
    }

    protected void warning(String message) {
        System.out.println("\t" + color.primaryText().toString() + message + ChessColor.RESET);
    }

    protected String commandString(String command, String description, String printColor) {
        return printColor + " " + String.format("%-14s", command) + color.lightText() + description;
    }

    protected String commandString(String command, String[] extendedDescription) {
        StringBuilder description = new StringBuilder(extendedDescription[0]);
        for (int i = 1; i < extendedDescription.length; i++) {
            description.append("\n\t\t\t\t\t").append(extendedDescription[i]);
        }
        return commandString(command, description.toString());
    }

    protected String commandString(String command, String description) {
        return commandString(command, description, color.ternaryText().toString());
    }
}
