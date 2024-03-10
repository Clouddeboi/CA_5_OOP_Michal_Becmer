import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/*
 * Group Members:
 * Michal Becmer
 * Jeffin Jesudas
 * Stephen Carragher-Kelly
 *
 * Tasks:
 * - Stephen set up the database
 * - Jeff and Stephen had done the research for the database and put it into tables using: http://darksouls.wikidot.com/weapons
 * - Michal Created the users for the database (with an additional test user without admin privileges)
 * - Michal set up the java project to connect to the database
 * - Michal Did feature 1 and 3
 * - Jeff Did Feature 2
 * - Stephen did Feature 4
 * */

/**
 * Main author: Michal Becmer
 * Other contributors:
 **/

public class MainApp {
    public static void main(String[] args) throws SQLException {

        System.out.println("\n-----------------------------------------------------");
        System.out.println("Welcome to the Dark Souls 1 Weapons Database\nPlease Enter your login info\n-----------------------------------------------------");

        int weaponIdToDelete = 0;
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
                        System.out.print("Enter the ID of the weapon you would like to view: ");
                        int weaponIdToView = in.nextInt();
                        in.nextLine();
                        DS_Weapons viewedWeapon = dao.getWeaponById(weaponIdToView);
                        if (viewedWeapon != null)
                        {
                            System.out.println("\nWeapon Details:\n----------------------");
                            System.out.printf("%-5s %-35s %-15s %-15s %-5s\n",
                                    "ID", "Name", "Attack", "Weight", "Location");
                            System.out.printf("%-5d %-35s %-15s %-15s %-5s\n",
                                    viewedWeapon.getID(), viewedWeapon.getName(), viewedWeapon.getAttack(),
                                    viewedWeapon.getWeight(), viewedWeapon.getLocation());
                        } else {
                            System.out.println("Weapon not found for ID: " + weaponIdToView);
                        }
                        break;
                    case 3:
                        System.out.print("Enter the ID of the weapon you would like to remove: ");
                        weaponIdToDelete = in.nextInt();//reads input from user
                        in.nextLine(); // Consume newline character
                        dao.deleteWeapon(weaponIdToDelete);//call method
                        break;
                    case 4:
                        /**
                         * Main author: Stephen Carragher Kelly
                         * Other contributors: Michal Becmer
                         **/
                        System.out.println("Please enter the weapon's name: ");
                        String name = in.nextLine();
                        System.out.println("Please enter the weapon's attack: ");
                        int attack = in.nextInt();
                        in.nextLine();
                        System.out.println("Please enter the weapon's weight: ");
                        Float weight = in.nextFloat();
                        in.nextLine();
                        System.out.println("Please enter the weapon's Location: ");
                        String location = in.nextLine();

                        try
                        {
                            DS_Weapons newWeapon = new DS_Weapons();
                            newWeapon.setName(name);
                            newWeapon.setAttack(attack);
                            newWeapon.setWeight(weight);
                            newWeapon.setLocation(location);

                            dao.insertWeapon(newWeapon);
                        }
                        catch (SQLException  e)
                        {
                            e.printStackTrace();

                        }

                        break;
                    case 99:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 99); // Exit loop when choice is 5
        }
    }

    /**
     * Main author: Michal Becmer
     * Other contributors:
     **/

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