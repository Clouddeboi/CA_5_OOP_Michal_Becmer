import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) throws SQLException {

        System.out.println("\n-----------------------------------------------------");
        System.out.println("Welcome to the Dark Souls 1 Weapons Database\nPlease Enter your login info\n-----------------------------------------------------");

        Scanner in = new Scanner(System.in);

        System.out.print("Username: ");
        String uName = in.nextLine();

        System.out.print("Password: ");
        String pass = in.nextLine();

        DAO dao = DAO.getInstance();
        User u = dao.logIn(uName, pass);

        if (u == null) {
            System.out.println("Login failed. Please try again.");
        } else {
            System.out.println("Welcome back, " + u.getDisplayName());
            int choice;
            do {

                System.out.println("\n-----------------------");
                System.out.println("1. View All Weapons");

                choice = in.nextInt();
                in.nextLine();

                switch (choice) {
                    case 1:
                        ShowUsers(dao);
                        break;
                    case 2:
                        addUser(dao, in);
                        break;
                    // Add cases for edit and remove user here
                    case 5:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 5); // Exit loop when choice is 5
        }
    }

    private static void addUser(DAO dao, Scanner kb) {
        // Implement logic to add a new user
    }

    private static void ShowUsers(DAO dao) throws SQLException {
        System.out.println("Current Users");
        List<User> all = dao.getAllUsers();
        System.out.printf("%-5s %-15s %-15s %-15s %-5s\n",
                "ID", "Username", "Password", "Name", "Admin");
        for (User user : all) {
            System.out.printf("%-5d %-15s %-15s %-15s %-5s\n",
                    user.getId(), user.getUsername(), user.getPassword(),
                    user.getDisplayName(), user.isAdmin());
        }
    }
}
