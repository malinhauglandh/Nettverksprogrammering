package task4.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpClient implements AutoCloseable{

    private DatagramSocket socket;
    private DatagramPacket packet;
    private InetAddress ip;
    private byte[] buf;
    private int port = 4555;

    public UdpClient() throws IOException {
        socket = new DatagramSocket();
        ip = InetAddress.getLocalHost();
        System.out.println("Connection with server established! ");
    } 
    
    public void sendAndReceive(String message) throws IOException {
        buf = message.getBytes();
        packet = new DatagramPacket(buf, buf.length, ip, port);
        socket.send(packet);

        if(message.equals("exit")) {
            System.out.println("Exiting...");
            return;
        }


        buf = new byte[1024]; // Clearing the buffer
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String response = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Server response: " + response);
    }  
    
    @Override
    public void close() {
        if(socket != null) {
            socket.close();
        }
    }
}
