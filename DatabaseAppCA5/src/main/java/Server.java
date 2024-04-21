import java.io.*;
import java.net.ServerSocket;
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

        try(ServerSocket serverSocket = new ServerSocket(SERVER_PORT_NUMBER))
        {
            System.out.println("Server started. listening for connections on port " + SERVER_PORT_NUMBER);
            int clientNumber = 0;

            while(true)
            {
                Socket clientSocket = serverSocket.accept();
                clientNumber++;
                System.out.println("Client" +clientNumber+ "connectd");

                ClientHandler clientHandler = new ClientHandler(clientSocket, clientNumber);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }
        catch (IOException e)
        {
            System.out.println("Server Message: IOException: " + e);
        }
    }

}

class ClientHandler implements Runnable
{
    private final Socket clientSocket;
    private final int clientNumber;
    public ClientHandler(Socket clientSocket, int clientNumber)
    {
        this.clientSocket = clientSocket;
        this.clientNumber = clientNumber;
    }

    @Override
    public void run()
    {
        try( PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));)
        {
            System.out.println("Client " + clientNumber + " handler started.");
            String request;

            while((request = in.readLine()) != null)
            {
                System.out.println("Client handler Message: Recieved from client: \"" + request + "\"");

                if(request.startsWith("ViewID:"))
                {
                    int id = Integer.parseInt(request.split(":")[1].trim());

                    try
                    {
                        DS_Weapons weapon = DAO.getInstance().getWeaponById(id);
                        String jsonWeapon = JSON_Converter.EntitytoJsonString(weapon);

                        out.println(jsonWeapon);
                        System.out.println("Server Message: Json Weapon sent to client. ");
                    }
                    catch (SQLException e)
                    {
                        out.println("Error cant retrieve weapon data: " +e.getMessage());
                        System.out.println("Server Message: Error invalid response");
                    }

                }
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
                else if(request.startsWith("DeleteID:"))
                {
                    int idToDelete = Integer.parseInt(request.split(":")[1].trim());
                    boolean deleted = deleteEntitybyID(idToDelete);
                    if(deleted)
                    {
                        out.println("ID "+idToDelete+" was deleted");
                    }
                    else
                    {
                        out.println("ID "+idToDelete+" failed tp delete");
                    }
                }
                else if (request.equals("GetImagesList"))
                {
                    //reads the file directory
                    File imageDirectory = new File("DatabaseAppCA5/Images");
                    //retrieve the list of image files in the specified directory above
                    String[] imageFiles = imageDirectory.list();

                    //checks that imageFiles is not null and contains more than 0 images
                    if (imageFiles != null && imageFiles.length > 0)
                    {
                        //Convert array of image file names to JSON format
                        String jsonImageList = JSON_Converter.arrayToJsonString(imageFiles);
                        //Send JSON response to client
                        out.println(jsonImageList);
                        System.out.println("Server Message: Image list sent to client.");
                    }
                    else
                    {
                        //sends empty list
                        out.println("[]");
                        System.out.println("Server Message: No images found.");
                    }
                }

                else if (request.startsWith("Exit"))
                {
                    out.println("Goodbye, Hope you have a nice day");
                    System.out.println("Server message: Client has notified us that it is quitting.");
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Client Message: IOException: " +e);
        }
        finally
        {
            try
            {
                clientSocket.close();
            }
            catch (IOException e)
            {
                System.out.println("Error closing client socket for client " +clientNumber + ": " + e.getMessage());
            }
        }
    }

    private boolean deleteEntitybyID(int id)
    {
        try
        {
            DAO.getInstance().deleteWeapon(id);
            return true;
        }
        catch(SQLException e)
        {
            System.out.println("Error deleting entity");
            return false;
        }
    }
}