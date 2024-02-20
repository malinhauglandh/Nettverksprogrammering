package task3.socket.server;

import java.io.*;
import java.net.*;

import task3.socket.client.SocketClientHandler;

class SocketServer {
  public static void main(String[] args) throws IOException {
    final int PORT_NBR = 1250;

    ServerSocket server = new ServerSocket(PORT_NBR);
    System.out.println("Server started on port: " + PORT_NBR + " Waiting...");

    while (true) {

      Socket connection = server.accept(); 
      SocketClientHandler clientHandlerThread = new SocketClientHandler(connection);
      clientHandlerThread.start();
      System.out.println("Client connected.");
    }
  }
}
