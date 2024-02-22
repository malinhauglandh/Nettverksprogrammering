package task4.udp;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UdpServer server = null;
        try {
            server = new UdpServer();
            server.start();

            try (Scanner scanner = new Scanner(System.in);
                 UdpClient client = new UdpClient()) {

                while (true) {
                    System.out.println("Enter a calculation (e.g., '5 + 5') or 'exit' to quit:");
                    String message = scanner.nextLine();

                    client.sendAndReceive(message);

                    if ("exit".equalsIgnoreCase(message)) {
                        break;
                    }
                }
            } catch (IOException e) {
                System.err.println("Client error: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            if (server != null) {
                server.close(); 
            }
        }
    }
}
