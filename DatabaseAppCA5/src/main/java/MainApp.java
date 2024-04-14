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
 * - Michal did Feature 5
 * - Stephen did Feature 6
 * - Michal did Feature 7
 * - Stephen did Feature 8
 * */

/**
 * Main author: Michal Becmer
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
                    System.out.println("5. Update Weapon By ID");
                }
                System.out.println("6. View Weapons based on filter");
                System.out.println("7. Convert List to JSON String");
                System.out.println("8. View weapon as Json String by ID");
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
                        if (u.isAdmin()) {
                            System.out.print("Enter the ID of the weapon you would like to remove: ");
                            weaponIdToDelete = in.nextInt();//reads input from user
                            in.nextLine(); // Consume newline character
                            dao.deleteWeapon(weaponIdToDelete);//call method
                        }
                        else
                        {
                            System.out.println("You do not have Admin Privilages");
                        }
                        break;
                    case 4:
                        /**
                         * Main author: Stephen Carragher Kelly
                         * Other contributors: Michal Becmer
                         **/
                        if (u.isAdmin()) {
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

                            try {
                                DS_Weapons newWeapon = new DS_Weapons();
                                newWeapon.setName(name);
                                newWeapon.setAttack(attack);
                                newWeapon.setWeight(weight);
                                newWeapon.setLocation(location);

                                dao.insertWeapon(newWeapon);
                            } catch (SQLException e) {
                                e.printStackTrace();

                            }
                        }
                        else
                        {
                            System.out.println("You do not have Admin Privilages");
                        }
                        break;
                    case 5:
                        if (u.isAdmin()) {
                            System.out.println("Enter The ID of the weapon you wish to update: ");
                            int idForUpdate = in.nextInt();//id of the weapon needed to be updated
                            in.nextLine();

                            System.out.println("Enter updated information here:");
                            System.out.println("--------------------------------");
                            System.out.println("Weapon Name: ");
                            String newName = in.nextLine();

                            System.out.println("Weapon Attack: ");
                            int newAttack = in.nextInt();
                            in.nextLine();//needed to be able to take in the weight

                            System.out.println("Weapon Weight: ");
                            float newWeight = in.nextFloat();
                            in.nextLine();//needed to be able to take in the location

                            System.out.println("Weapon Location: ");
                            String newLocation = in.nextLine();

                            //new object to contain the updated info
                            DS_Weapons weaponUpdate = new DS_Weapons();

                            weaponUpdate.setName(newName);
                            weaponUpdate.setAttack(newAttack);
                            weaponUpdate.setWeight(newWeight);
                            weaponUpdate.setLocation(newLocation);

                            //calling the method with the given id
                            DS_Weapons complete = dao.update(idForUpdate, weaponUpdate);
                            if (complete != null) {
                                System.out.println("Weapon with ID " + idForUpdate + " updated");
                            } else {
                                System.out.println("Failed to update!");
                            }
                        }
                        else
                        {
                            System.out.println("You do not have Admin Privilages");
                        }
                        break;
                    case 6:
                        /**
                         * Main author: Stephen Carragher Kelly
                         **/
                        filterWeaponsByCriteria(dao);
                        break;
                    case 7:
                        System.out.println(JSON_Converter.listToJsonString(DAO.getInstance().getAllWeapons()));
                        break;
                    case 8:
                        /**
                         * Main author: Stephen Carragher Kelly
                         **/
                        System.out.println("Enter the ID of the weapon you would like to see as a JSON String");
                        int weaponID = in.nextInt();
                        in.nextLine();
                        String weaponJson = dao.getWeaponbyIDasJson(weaponID);
                        if(weaponJson != null)
                        {
                            System.out.println("Weapon Json:/n");
                            System.out.println(weaponJson);
                        }
                        else
                        {
                            System.out.println("Weapon not found for ID: " + weaponID);
                        }
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

    /**
     * Main author: Stephen Carragher Kelly
     **/
    private static void filterWeaponsByCriteria(DAO dao) throws SQLException
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Filter by Attack");
        System.out.println("2. Filter by Weight");
        System.out.println("Please choose the filter criteria: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        String filterCriteria;
        if(choice == 1)
        {
            filterCriteria = "attack";
        }
        else if (choice == 2)
        {
            filterCriteria = "weight";
        }
        else
        {
            System.out.println("Invalid option, Try agin");
            return;
        }

        System.out.println("now enter the value for filtering: ");
        float value = scanner.nextFloat();
        scanner.nextLine();

        List<DS_Weapons> weaponsfiltered = dao.getWeaponByFilter(filterCriteria, value);

        System.out.println("Filter List");
        System.out.println(" ");
        System.out.printf("%-5s %-35s %-15s %-15s %-5s\n", "ID", "Name", "Attack", "Weight", "Location");
        for (DS_Weapons weapon : weaponsfiltered) {
            System.out.printf("%-5d %-35s %-15s %-15s %-5s\n", weapon.getID(), weapon.getName(), weapon.getAttack(), weapon.getWeight(), weapon.getLocation());
        }
    }
}