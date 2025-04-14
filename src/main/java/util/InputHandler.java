package util;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public String extractAction(String commandLine) {
        String[] tokens = commandLine.trim().split("\\s+");
        return tokens.length > 0 ? tokens[0] : "";
    }

    public Map<String, Object> parseCommand(String commandLine) {
        Map<String, Object> params = new HashMap<>();
        String[] tokens = commandLine.trim().split("\\s+");
        if (tokens.length == 0) return params;
        String action = tokens[0].toLowerCase();
        if ("login".equals(action) && tokens.length >= 3) {
            params.put("username", tokens[1]);
            params.put("password", tokens[2]);
        } else if ("register".equals(action) && tokens.length >= 3) {
            params.put("user", new model.User(tokens[1], tokens[2]));
        }
        // Extend parsing for other commands (export, import, etc.) if needed.
        return params;
    }
}
