import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DAO
{
    private String url = "jdbc:mysql://localhost/";
    private String dbname = "dark_souls_ca5_oop";
    private String username ="root";
    private String password ="";

    private static DAO instance;

    private DAO()
    {

    }

    public static DAO getInstance() {
        if (instance == null)
            instance = new DAO();

        return instance;
    }

    public Connection getConnection()
    {
        try{
            Connection conn = DriverManager.getConnection
                    (url+dbname,username,password);
            return conn;
        }
        catch (SQLException e) {

            System.out.println("Unable to connect to databse");
            return null;
        }
    }

    //f1: View all
    /**
     * Main author: Michal Becmer
     * Other contributors: Jeffin Jesudas
     **/

    public List<DS_Weapons> getAllWeapons() throws SQLException
    {
        //creates list to store the retrieved weapons
        List<DS_Weapons> weapons = new ArrayList<>();
        //connects to db
        Connection conn = getConnection();

        String query = "SELECT * FROM Weapons"; //SQL query
        PreparedStatement stmt = conn.prepareStatement(query);//prepares statement
        ResultSet results = stmt.executeQuery();//execute query

        //Iterate through the result set
        while (results.next())
        {
            // Create a new DS_Weapons object for each retrieved weapon
            DS_Weapons weapon = new DS_Weapons();

            weapon.setID(results.getInt("ID"));
            weapon.setName(results.getString("Name"));
            weapon.setAttack(results.getInt("Attack"));
            weapon.setWeight(results.getFloat("Weight"));
            weapon.setLocation(results.getString("Location"));

            //Add the weapon to the list of weapons
            weapons.add(weapon);
        }

        conn.close();
        return weapons;
    }

    //f2:Find by ID
    /**
     * Main author: Jeffin Jesudas
     * Other contributors: Michal Becmer
     **/
    public DS_Weapons getWeaponById(int id) throws SQLException {
        Connection connection = getConnection(); // Get a connection to the database
        // Create a prepared statement with a parameterized query to retrieve a weapon by ID
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM weapons WHERE id = ?");
        statement.setInt(1, id); // Set the value of the parameter in the prepared statement

        ResultSet resultSet = statement.executeQuery(); // Execute the query and get the result set
        DS_Weapons weapon = null; // Initialize a Weapon object to store the result

        // Check if the result set contains a row and create a Weapon object if found
        if (resultSet.next()) {
            weapon = new DS_Weapons(); // Create a new Weapon object
            // Set attributes of the Weapon object from the result set
            weapon.setID(resultSet.getInt("ID"));
            weapon.setName(resultSet.getString("Name"));
            weapon.setAttack(resultSet.getInt("Attack"));
            weapon.setWeight(resultSet.getFloat("Weight"));
            weapon.setLocation(resultSet.getString("Location"));
        }

        // Close the result set, statement, and connection to free up resources
        resultSet.close();
        statement.close();
        connection.close();

        return weapon; // Return the Weapon object (or null if not found)
    }

    //f3:Delete
    /**
     * Main author: Michal Becmer
     * Other contributors: Stephen Carragher Kelly
     **/

    public void deleteWeapon(int weaponId) throws SQLException {
        //get connection to the db
        Connection conn = getConnection();
        //defining sql query
        String query = "DELETE FROM Weapons WHERE ID = ?";
        //prepare query with a placeholder until you give it an id
        PreparedStatement stmt = conn.prepareStatement(query);
        //set id as value and execute the update
        stmt.setInt(1, weaponId);
        stmt.executeUpdate();
        //success message
        System.out.println("Weapon with ID " + weaponId + " deleted successfully.");
        //stop connection
        conn.close();
    }

    //f4:Insert an Entity
    /**
     * Main author: Stephen Carragher Kelly
     * Other contributors: Michal Becmer
     **/
    public void insertWeapon(DS_Weapons weapon) throws SQLException
    {
        Connection conn = getConnection(); // Connects to database
        String query = "INSERT INTO Weapons (Name, Attack, Weight, Location) VALUES (?,?,?,?)"; //creates the query for the insert statement
        PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        stmt.setString(1, weapon.getName());
        stmt.setInt(2, weapon.getAttack());
        stmt.setFloat(3, weapon.getWeight());
        stmt.setString(4, weapon.getLocation());

        stmt.executeUpdate(); // Executes the statement

        ResultSet generatedKeys  = stmt.getGeneratedKeys(); //gets any auto-generated keys
        if(generatedKeys.next())
        {
            weapon.setID(generatedKeys.getInt(1)); //sets the ID in the weapon object
        }

        else
        {
            throw new SQLException("Creating weapon failed, no ID obtained."); //throwsd an exception if no ID was obtained
        }

        System.out.println("Weapon inserted successfully with ID: " + weapon.getID());
        conn.close();
    }

    /**
     * Main author: Michal Becmer
     * Other contributors:
     **/
    public DS_Weapons update(int id, DS_Weapons weapon) throws SQLException{

        //connects to database
        Connection conn = getConnection();
        //defines the needed sql query
        String query = "UPDATE Weapons SET Name=?, Attack=?, Weight=?, Location=? WHERE ID=?";
        //prepares sql statement
        PreparedStatement stmt = conn.prepareStatement(query);
        //sets all the parameters to ? assumes the parameters is its respected type
        stmt.setString(1, weapon.getName());//for example assumes this is a string using .setString
        stmt.setInt(2, weapon.getAttack());
        stmt.setFloat(3, weapon.getWeight());
        stmt.setString(4, weapon.getLocation());
        stmt.setInt(5, id);

        //executes operation
        int affectedRows = stmt.executeUpdate();

        //closes the prepared statement and database connection
        stmt.close();
        conn.close();

        //checking if update is successful
        if(affectedRows > 0)
        {
            System.out.println("Weapon with ID " + id+ " has been successfully updated!");
            return null;//returns null if no weapon was found
        }
        else
        {
            System.out.println("Weapon not found!");
            return null;//return null if no weapon was found with given id
        }

    }

    /**
     * Main author: Stephen Carragher Kelly
     * Other contributors:
     **/
    public List<DS_Weapons> getWeaponByFilter(String filterCriteria, float filterValue) throws SQLException
    {
        List<DS_Weapons> weapons = new ArrayList<>();
        Connection conn = getConnection();

        String query = "SELECT * FROM Weapons WHERE ";

        if ("attack".equalsIgnoreCase(filterCriteria))
        {
            query += "Attack = ?";
        }
        else if ("weight".equalsIgnoreCase(filterCriteria))
        {
            query += "Weight = ?";
        }
        else
        {
            throw new IllegalArgumentException("Invalid filter criteria: " + filterCriteria);
        }

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setFloat(1, filterValue);

        ResultSet results = stmt.executeQuery();

        while (results.next()) {
            DS_Weapons weapon = new DS_Weapons();
            weapon.setID(results.getInt("ID"));
            weapon.setName(results.getString("Name"));
            weapon.setAttack(results.getInt("Attack"));
            weapon.setWeight(results.getFloat("Weight"));
            weapon.setLocation(results.getString("Location"));
            weapons.add(weapon);
        }

        conn.close();
        return weapons;
    }

    public User logIn(String username, String password) throws SQLException
    {
        User u = null;
        Connection conn = getConnection();
        if(conn != null)
        {
            String query = "select * from Users where username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet res = stmt.executeQuery();
            while(res.next())
            {
                if(res.getString("password").equals(password))
                {
                    u = new User();
                    u.setId(res.getInt("ID"));
                    u.setUsername(res.getString("username"));
                    u.setPassword(res.getString("password"));
                    u.setDisplayName(res.getString("displayName"));
                    u.setAdmin(res.getInt("isAdmin")==1);
                }
            }
            conn.close();

        }
        return u;
    }
}