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

    public List<DS_Weapons> getAllWeapons() throws SQLException
    {
        List<DS_Weapons> weapons = new ArrayList<>();
        Connection conn = getConnection();

        if (conn != null) {
            String query = "SELECT * FROM Weapons"; // Your SQL query
            PreparedStatement stmt = conn.prepareStatement(query);
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
