package task4.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class UdpServer extends Thread{

    private DatagramSocket socket;
    private byte[] buffer;
    private DatagramPacket packet;
    private int port = 4555;

    public UdpServer() throws IOException {
        try {
            socket = new DatagramSocket(port);
            System.out.println("Server is running...\nWaiting for client to send data...");
            buffer = new byte[65535];
            packet = null;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void run() {
        try {
            sendAndReceive();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            close();
        }
    }

    public void sendAndReceive() throws IOException {
        while(true) {
            try {
                packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String input = new String(packet.getData(), 0, packet.getLength());
                
                System.out.println("Client: " + input);
                if("exit".equalsIgnoreCase(input.trim())) {
                    System.out.println("Client sent exit...Exiting...");
                    return;
                }

                String result = calculate(input);

            
                byte [] responseData = result.getBytes();
                packet = new DatagramPacket(responseData, responseData.length, packet.getAddress(), packet.getPort());
                socket.send(packet);
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
                break;
            } finally {
                buffer = new byte[65535]; // Clearing the buffer  
            }
        }
    }
    public void close() {
        if(socket != null) {
            socket.close();
        }
    }

    public String calculate(String requestedCalculation) {
        try {
            String[] parts = requestedCalculation.split("([*+\\-/])", 2);
            if (parts.length < 2) {
                return "Invalid input: " + requestedCalculation;
            }
    
            double number1 = Double.parseDouble(parts[0].trim());
            double number2 = Double.parseDouble(parts[1].trim());
            char operator = requestedCalculation.replaceAll("[^*+\\-/]", "").charAt(0);
    
            double result;
            switch (operator) {
                case '+':
                    result = number1 + number2;
                    break;
                case '-':
                    result = number1 - number2;
                    break;
                case '*':
                    result = number1 * number2;
                    break;
                case '/':
                    if (number2 == 0) {
                        return "Cannot divide by zero";
                    }
                    result = number1 / number2;
                    break;
                default:
                    return "Unsupported operator: " + operator;
            }
            return "The result of " + requestedCalculation + " is " + result;
        } catch (NumberFormatException e) {
            return "Invalid number format in input: " + requestedCalculation;
        } catch (Exception e) {
            return "Error processing calculation: " + e.getMessage();
        }
    }
    
}




