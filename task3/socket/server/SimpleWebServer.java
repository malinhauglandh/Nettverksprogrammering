package task3.socket.server;

import java.io.*;
import java.net.*;

public class SimpleWebServer {
    public static void main(String[] args) throws IOException {
        final int port = 8080;
        ServerSocket server = new ServerSocket(port);
        System.out.println("Web server started, listening on port " + port);

        while (true) { 
            Socket clientSocket = server.accept(); 
            System.out.println("Client connected.");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

              
                StringBuilder requestHeader = new StringBuilder();
                String line; 
                while (!(line = reader.readLine()).isBlank()) { 
                    requestHeader.append("<LI>").append(line).append("</LI>\n");
                }

                // Send the HTTP response to the client 
                writer.println("HTTP/1.1 200 OK");
                writer.println("Content-Type: text/html; charset=utf-8");
                writer.println(""); // Blank line between headers and content
                writer.println("<HTML><BODY>");
                writer.println("<H1>Welcome to the server</H1>");
                writer.println("Header from client is:");
                writer.println("<UL>");
                writer.println(requestHeader.toString());
                writer.println("</UL>");
                writer.println("</BODY></HTML>");

                System.out.println("Response sent. Closing connection...");

            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                clientSocket.close(); // Close the connection to the client
            }
            server.close();
        }
        
    }
}
