package app;

import controller.MainController;
import util.InputHandler;
import view.ConsoleView;
import view.IView;
import view.ViewState;

import java.util.HashMap;
import java.util.Map;

public class Application {
    public static void main(String[] args) {
        // Initialize the view, controller, and input handler.
        IView view = new ConsoleView();
        MainController controller = new MainController(view);
        InputHandler inputHandler = new InputHandler();

        // Initialize business logic: load data and show login screen.
        controller.initialize();

        boolean running = true;
        while (running) {
            // Render the current view (menu)
            view.render();

            // Read a line of input from the user.
            String commandLine = inputHandler.readCommand();
            if ("exit".equalsIgnoreCase(commandLine.trim())) {
                running = false;
                continue;
            }

            Map<String, Object> params = new HashMap<>();
            String action = "";

            // If input is pure numeric, map it to an action based on current view state.
            if (commandLine.trim().matches("\\d+")) {
                int option = Integer.parseInt(commandLine.trim());
                if (((ConsoleView) view).getCurrentState() == ViewState.LOGIN) {
                    // LOGIN screen options.
                    switch (option) {
                        case 1:
                            System.out.print("Enter username: ");
                            String username = inputHandler.readCommand();
                            System.out.print("Enter password: ");
                            String password = inputHandler.readCommand();
                            params.put("username", username);
                            params.put("password", password);
                            action = "login";
                            break;
                        case 2:
                            System.out.print("Enter new username: ");
                            String regUsername = inputHandler.readCommand();
                            System.out.print("Enter new password: ");
                            String regPassword = inputHandler.readCommand();
                            params.put("user", new model.User(regUsername, regPassword));
                            action = "register";
                            break;
                        default:
                            System.err.println("Invalid option. Please try again.");
                            continue;
                    }
                } else if (((ConsoleView) view).getCurrentState() == ViewState.MAIN_MENU) {
                    // MAIN_MENU options.
                    // Options:
                    // 1 - List All Influencers
                    // 2 - Search Influencers
                    // 3 - Sort Influencers
                    // 4 - Filter Influencers
                    // 5 - Add Influencer to Favorites
                    // 6 - View Favorites
                    // 7 - Logout
                    // 8 - Subscribe/Unsubscribe (toggle)
                    switch (option) {
                        case 1:
                            action = "list";
                            break;
                        case 2:
                            System.out.print("Enter search keyword: ");
                            String query = inputHandler.readCommand();
                            params.put("query", query);
                            action = "search";
                            break;
                        case 3:
                            System.out.print("Enter sort criteria (name/followers/adRate): ");
                            String criteria = inputHandler.readCommand().trim().toLowerCase();
                            System.out.print("Enter order (asc/desc): ");
                            String order = inputHandler.readCommand().trim().toLowerCase();
                            params.put("criteria", criteria);
                            params.put("ascending", "asc".equals(order));
                            action = "sort";
                            break;
                        case 4:
                            System.out.print("Enter filter type (platform/category/follower/country): ");
                            String type = inputHandler.readCommand().trim().toLowerCase();
                            if ("platform".equals(type) || "category".equals(type) || "country".equals(type)) {
                                System.out.print("Enter value for " + type + ": ");
                                String value = inputHandler.readCommand().trim();
                                params.put(type, value);
                            } else if ("follower".equals(type)) {
                                System.out.print("Enter minimum follower count (0 for none): ");
                                int min = Integer.parseInt(inputHandler.readCommand().trim());
                                System.out.print("Enter maximum follower count (0 for no limit): ");
                                int max = Integer.parseInt(inputHandler.readCommand().trim());
                                params.put("minFollowers", min);
                                params.put("maxFollowers", max == 0 ? Integer.MAX_VALUE : max);
                            } else {
                                System.err.println("Invalid filter type.");
                                continue;
                            }
                            action = "filter";
                            break;
                        case 5:
                            System.out.print("Enter influencer ID to add to favorites: ");
                            int id = Integer.parseInt(inputHandler.readCommand().trim()) - 1;
                            if (id < 0 || id >= ((ConsoleView) view).getCurrentInfluencers().size()) {
                                System.err.println("Invalid influencer ID.");
                                continue;
                            }
                            params.put("influencer", ((ConsoleView) view).getCurrentInfluencers().get(id));
                            action = "addtofavorites";
                            break;
                        case 6:
                            action = "showfavorites";
                            break;
                        case 7:
                            action = "logout";
                            break;
                        case 8:
                            // Toggle subscription status based on current status.
                            if (((ConsoleView) view).isUserSubscribed()) {
                                action = "unsubscribe";
                            } else {
                                action = "subscribe";
                            }
                            break;
                        default:
                            System.err.println("Invalid option. Please try again.");
                            continue;
                    }
                }
            } else {
                // For non-numeric commands, let the input handler parse them.
                action = inputHandler.extractAction(commandLine);
                params = inputHandler.parseCommand(commandLine);
            }
            // Dispatch the action and parameters to the controller.
            controller.handleRequest(action, params);
        }
        System.out.println("Thank you for using the Influencer Management System. Goodbye!");
    }
}
