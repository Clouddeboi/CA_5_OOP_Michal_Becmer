import java.sql.*;
import java.util.ArrayList;
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
    //Completed by: Michal

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

    //f3:Delete
    //Completed By: Michal Becmer

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
