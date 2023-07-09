package lk.epic.javaSE_server.server;

import org.jpos.iso.packager.ISO87APackager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final static int PORT = 5000;
    private static ServerSocket serverSocket;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server has started in port : " + PORT);

            Socket localSocket = serverSocket.accept();

            System.out.println("Connected Client IP Address : " + localSocket.getInetAddress());
            System.out.println("Connected Client Port Number : " + localSocket.getLocalPort());

            dataInputStream = new DataInputStream(localSocket.getInputStream());
            dataOutputStream = new DataOutputStream(localSocket.getOutputStream());
            String quitMessage = "No";
            ISO87APackager packager = new ISO87APackager();

            while (!quitMessage.equals("Yes")) {
                int requestLength = dataInputStream.readInt();
                byte[] requestData = new byte[requestLength];
                dataInputStream.readFully(requestData);

                // Let's Unpack the packed ISO8583 message request

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
