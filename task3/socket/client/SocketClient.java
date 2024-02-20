package task3.socket.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class SocketClient {
  public static void main(String[] args) throws IOException {
    final int PORTNR = 1250;

    /* Scanner to read from the command line */
    Scanner readFromCommandLine = new Scanner(System.in);
    System.out.print("Type the name of the machine where the server is running: ");
    String serverMachine = readFromCommandLine.nextLine();

    /* Sets a connection to the server */
    Socket connection = new Socket(serverMachine, PORTNR);
    System.out.println("Connection established.");

    /* Opens for communication with the server */
    InputStreamReader readingConnection = new InputStreamReader(connection.getInputStream());
    BufferedReader reader = new BufferedReader(readingConnection);
    PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);

    /* Reading the first two lines from the server */
    String helloString = reader.readLine();
    String chooseOperationString = reader.readLine();
    System.out.println(helloString + "\n" + chooseOperationString);

    String selection = readFromCommandLine.nextLine();
    writer.println(selection);


    String num1, num2;
    while(selection.equals("+") || selection.equals("-")) {
      String number1 = reader.readLine();
      System.out.println(number1);
      num1 = readFromCommandLine.nextLine();
      writer.println(num1);

      String number2 = reader.readLine();
      System.out.println(number2);
      num2 = readFromCommandLine.nextLine();
      writer.println(num2);

      String result = reader.readLine();
      String cont = reader.readLine();
      System.out.println(result);
      System.out.println(cont);

      String shouldCont = readFromCommandLine.nextLine();
      writer.println(shouldCont);

      if(shouldCont.equals("yes")) {
        String operatorSelection = reader.readLine();
        System.out.println(operatorSelection);
        selection = readFromCommandLine.nextLine();
        writer.println(selection);
        continue;
      }
      else if (shouldCont.equals("no")) {
        break;
      } else {
        System.out.println("Invalid input.");
        break;
      }
    }

    /* Closes connection */
    reader.close();
    writer.close();
    connection.close();
  }
}
