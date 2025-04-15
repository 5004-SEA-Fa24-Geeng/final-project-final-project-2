import controller.MainController;
import view.MainView;

/**
 * Main entry point for the Influencer Management System.
 */
public class Main {

    /**
     * Main method to start the application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        MainView view = new MainView();
        MainController controller = new MainController(view);

        controller.initialize();

        controller.run();

        System.out.println("Thank you for using the Influencer Management System. Goodbye!");
    }
}