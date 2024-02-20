package task3.socket.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClientHandler extends Thread{

    private final Socket connection;

    public SocketClientHandler(Socket connection) {
        this.connection = connection;
    }

    public void run() {
        try {
            InputStreamReader readingConnection = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(readingConnection);
            PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);

            /* Sending text to client */
            System.out.println("Connection established.");
            writer.println("Hello, you have contact with the server side!");
            writer.println("In order to either add or subtract two numbers, please choose operator + or -:");
            
            
            String chosenOperation = reader.readLine();
            int firstNumber;
            int secondNumber;
            

            while(true) {
                if(!chosenOperation.equals("+") &&  !chosenOperation.equals("-")) {
                    writer.println("The operator is not valid.");
                    break;
                }
                writer.println("Type in the first number:");
                firstNumber = Integer.parseInt(reader.readLine());
                writer.println("Type in the second number:");
                secondNumber = Integer.parseInt(reader.readLine());
                String calculation = firstNumber + " "  + chosenOperation + " " + secondNumber;


                System.out.println("A client wants this to be calculated: " + calculation);

                int result = 0;

                if(chosenOperation.equals("+")) {
                result = firstNumber + secondNumber;
                } else if(chosenOperation.equals("-")) {
                result = firstNumber - secondNumber;
                }
                writer.println("The result from the requested calculation " + calculation + " is: " + result);


                writer.println("Would you like to perform another calculation? (yes/no)");
        
                String response = reader.readLine();
                if(response.equals("yes")) {
                    writer.println("In order to either add or subtract two numbers, please choose operator + or -:");
                    chosenOperation = reader.readLine();
                    continue;
                }
                else if (response.equals("no")) {
                    System.out.println("Client disconnected.");
                  break;
                } else {
                    System.out.println("Client error, disconnecting");
                    writer.println("Invalid response. Please type 'yes' or 'no'.");
                    continue;
                }
            }
            reader.close();
            writer.close();
            connection.close();
        } catch (IOException ioE){
            ioE.printStackTrace();
        }
    }
}
