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
                System.out.println("2. View Specific Weapon by ID");
                if(u.isAdmin())
                {
                    System.out.println("3. Delete Weapon By ID");
                    System.out.println("4. Insert Weapon By ID");
                }
                System.out.println("99. Exit");
                System.out.println("-----------------------\n");

                choice = in.nextInt();
                in.nextLine();

                switch (choice) {
                    case 1:
                        ShowAllWeapons(dao);
                        break;
                    case 2:
                        System.out.println("");
                        break;
                    // Add cases for edit and remove user here
                    case 99:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 99); // Exit loop when choice is 5
        }
    }

    private static void ShowAllWeapons(DAO dao) throws SQLException {
        System.out.println("Complete Weapons List\n-----------------------------------------------------------------------------------");
        List<DS_Weapons> all = dao.getAllWeapons();
        System.out.printf("%-5s %-35s %-15s %-15s %-5s\n",
                "ID", "Name", "Attack", "Weight", "Location");
        for (DS_Weapons weapon : all) {
            System.out.printf("%-5d %-35s %-15s %-15s %-5s\n",
                    weapon.getID(), weapon.getName(), weapon.getAttack(),
                    weapon.getWeight(), weapon.getLocation());
        }
    }
}
