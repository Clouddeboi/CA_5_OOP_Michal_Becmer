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

    public List<User> getAllUsers() throws SQLException
    {
        List<User> users = new ArrayList<>();
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("Select * from Users");

        while (results.next())
        {
            User u = new User();
            u.setId(results.getInt("ID"));
            u.setUsername(results.getString("username"));
            u.setPassword(results.getString("password"));
            u.setDisplayName(results.getString("displayName"));
            u.setAdmin(results.getBoolean("isAdmin"));
            users.add(u);
        }

        conn.close();
        return users;
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
