import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

/**
 * Main author: Stephen Carragher Kelly
 **/

public class Client
{
    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    public void start() {


        try (   // create socket to connect to the server
                Socket socket = new Socket("localhost", 8888);
                // get the socket's input and output streams, and wrap them in writer and readers
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        )
        {

            System.out.println("Client message: The Client is running and has connected to the server");

            //ask user to enter a command
            Scanner consoleInput = new Scanner(System.in);

            //while(true)
            //{
            /**
             * Main author: Stephen Carragher Kelly
             **/
            System.out.println("Please enter any of the following commands in the correct format.");
            System.out.println("ViewID:<ID>");
            System.out.println("ViewAll");
            System.out.println("AddEntity");
            System.out.println("Please enter a command: ");
            String userRequest = consoleInput.nextLine();
            // send the command to the server on the socket
            //out.println(userRequest);      // write the request to socket along with a newline terminator (which is required)
            // out.flush();                      // flushing buffer NOT necessary as auto flush is set to true

            //if (userRequest.equalsIgnoreCase("Exit")) {
            //    break; // Exit the loop if user types "Exit"
            //}
            // process the answer returned by the server
            //
            /**
             * Main author: Stephen Carragher Kelly
             **/
            if(userRequest.startsWith("ViewID"))
            {
                out.println(userRequest);
                String Jsonweapon = in.readLine();
                DS_Weapons weapon = JSON_Converter.JsonStringtoEntity(Jsonweapon, DS_Weapons.class);

                if(weapon != null)
                {
                    System.out.println("Weapon Details:");
                    System.out.println("Name: " + weapon.getName());
                    System.out.println("Attack: " + weapon.getAttack());
                    System.out.println("Weight: " + weapon.getWeight());
                    System.out.println("Location: " + weapon.getLocation());
                }
                else
                {
                    System.out.println("Weapon not found for ID");
                }
            }
            /**
             * Main author: Stephen Carragher Kelly
             **/
            else if(userRequest.startsWith("ViewAll"))
            {
                out.println(userRequest);
                String allJsonWeapons = in.readLine();
                List<DS_Weapons> allWeapons = JSON_Converter.jsonStringtoList(allJsonWeapons, DS_Weapons.class);

                if(allWeapons.isEmpty())
                {
                    System.out.println("No weapons found");
                }
                else
                {
                    System.out.println("All weapons:");
                    System.out.printf("%-5s %-35s %-15s %-15s %-5s\n", "ID", "Name", "Attack", "Weight", "Location");
                    for (DS_Weapons weapons: allWeapons)
                    {
                        System.out.printf("%-5s %-35s %-15s %-15s %-5s\n", weapons.getID(), weapons.getName(), weapons.getAttack(), weapons.getWeight(), weapons.getLocation());
                    }
                }
            }
            /**
             * Main author: Stephen Carragher Kelly
             **/
            else if(userRequest.startsWith("AddEntity"))
            {
                System.out.println("Enter the weapons Details");
                System.out.println("Name: ");
                String name = consoleInput.nextLine();

                System.out.println("Attack: ");
                int attack = consoleInput.nextInt();

                System.out.println("Weight: ");
                float weight = consoleInput.nextFloat();

                System.out.println("Location: ");
                consoleInput.nextLine();
                String location = consoleInput.nextLine();

                String JSONRequest = "AddEntity:|Name:" + name + "|Attack:" + attack + "|Weight:" + weight + "|Location:" + location;

                out.println(JSONRequest);


                String response;
                System.out.println(response = in.readLine());
            }
            else
            {
                System.out.println("Invaid request");
            }

        }

        //}
        catch (IOException e)
        {
            System.out.println("Client message: IOException: " + e);
        }

        // sockets and streams are closed automatically due to try-with-resources

        System.out.println("Exiting client, but server may still be running.");
    }
}
