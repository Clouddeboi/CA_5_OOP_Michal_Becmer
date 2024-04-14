import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

/**
 * Main author: Stephen Carragher Kelly
 **/

public class Server
{
    final int SERVER_PORT_NUMBER = 8888;  // could be any port from 1024 to 49151 (that doesn't clash with other Apps)

    public static void main(String[] args)
    {
        Server server = new Server();
        server.start();
    }

    public void start()
    {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT_NUMBER); )
        {
            while(true)
            {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                )

                {
                    System.out.println("Server Message: A Client has connected.");

                    String request = in.readLine(); // wait for input from the client, then read it.

                    System.out.println("Server message: Received from client : \"" + request + "\"");

                    // Implement our PROTOCOL
                    // The protocol is the logic that determines the responses given based on requests received.
                    //
                    /**
                     * Main author: Stephen Carragher Kelly
                     **/
                    if(request.startsWith("ViewID:"))
                    {
                        int id = Integer.parseInt(request.split(":")[1].trim());

                        try
                        {
                            DS_Weapons weapon = DAO.getInstance().getWeaponById(id);
                            String jsonWeapon = JSON_Converter.EntitytoJsonString(weapon);

                            out.println(jsonWeapon);
                            System.out.println("Server Message: Json Weapon sent to client. ");
                        } catch (SQLException e)
                        {
                            out.println("Error cant retrieve weapon data: " +e.getMessage());
                            System.out.println("Server Message: Error invalid response");
                        }

                    }
                    /**
                     * Main author: Stephen Carragher Kelly
                     **/
                    else if(request.startsWith("ViewAll"))
                    {
                        try
                        {
                            List<DS_Weapons> allWeapons = DAO.getInstance().getAllWeapons();

                            String allJsonWeapons = JSON_Converter.listToJsonString(allWeapons);

                            out.println(allJsonWeapons);
                            System.out.println("Server Message: Weapons sent to client");
                        }
                        catch(SQLException e)
                        {
                            out.println("Error cant retrieve weapon data: " +e.getMessage());
                            System.out.println("Server Message: Error invalid response");
                        }
                    }
                    /**
                     * Main author: Stephen Carragher Kelly
                     **/
                    else if(request.startsWith("AddEntity"))
                    {
                        String[] parts = request.split("\\|");

                        String name = "";
                        int attack = 0;
                        float weight = 0;
                        String location = "";

                        for(String part : parts)
                        {
                            String[] attributeValue = part.split(":");
                            if(attributeValue.length == 2)
                            {
                                String attrubute = attributeValue[0];
                                String value = attributeValue[1];

                                switch (attrubute)
                                {
                                    case "Name":
                                        name = value;
                                        break;
                                    case "Attack":
                                        attack = Integer.parseInt(value);
                                        break;
                                    case "Weight":
                                        weight = Float.parseFloat(value);
                                        break;
                                    case "Location":
                                        location = value;
                                        break;
                                    default:
                                        System.out.println("Wrong attriutes: " + attrubute);
                                        break;
                                }
                            }
                        }

                        DS_Weapons newWeapon = new DS_Weapons();
                        newWeapon.setName(name);
                        newWeapon.setAttack(attack);
                        newWeapon.setWeight(weight);
                        newWeapon.setLocation(location);

                        try
                        {
                            DAO.getInstance().insertWeapon(newWeapon);
                            String jsonNewWeapon = JSON_Converter.EntitytoJsonString(newWeapon);
                            out.println(jsonNewWeapon);
                            System.out.println("Serer Message: Weapon added Successfully");
                        }
                        catch (SQLException e)
                        {
                            out.println("Error cant create weapon: " +e.getMessage());
                            System.out.println("Server Message: Error failed to add weapon");
                        }
                    }
                    else
                    {
                        out.println("Error invalid request");
                        System.out.println("Server Message: Error invalid request from client");
                    }
                    //out.flush();  // force the response to be sent
                }
            }
        } catch (IOException e) {
            System.out.println("Server Message: IOException: " + e);
        }
    }

}