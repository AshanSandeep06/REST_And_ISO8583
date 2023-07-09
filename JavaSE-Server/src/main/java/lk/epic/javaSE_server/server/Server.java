package lk.epic.javaSE_server.server;

import lk.epic.javaSE_server.ISO8583Message.ISO8583Message;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.ISO87APackager;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
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
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String quitMessage = "No";
            ISO87APackager packager = new ISO87APackager();

            while (!quitMessage.equals("Yes")) {
                int requestLength = dataInputStream.readInt();
                byte[] requestData = new byte[requestLength];
                dataInputStream.readFully(requestData);

                // Let's Unpack the packed ISO8583 message request
                ISOMsg requestMessage = ISO8583Message.getInstance().getIsoMessage();
                requestMessage.setPackager(packager);
                requestMessage.unpack(requestData);

                // Request From Client
                processClientMessage(requestMessage);

                // Send ISO8583 Response message to the Client
                sendISO8583ResponseToClient(packager);

                System.out.print("Do you want to Quit the Server : ");
                quitMessage = bufferedReader.readLine().trim();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void processClientMessage(ISOMsg requestMessage) {
        try {
            for (int i = 0; i < 64; i++) {
                if (requestMessage.getString(i) != null) {
                    System.out.println("DataField " + "[" + i + "] : " + requestMessage.getString(i));
                }
            }

            System.out.println("Client Request Message : " + ISOUtil.hexString(requestMessage.pack()));
        } catch (ISOException e) {
            e.printStackTrace();
        }
    }

    public static void sendISO8583ResponseToClient(ISO87APackager packager) {
        try {
            ISOMsg iso8583Response = ISO8583Message.getInstance().getIsoMessage();
            iso8583Response.setMTI("0110");
            iso8583Response.set("3", "000000");
            iso8583Response.set("4", "000000430000");
            iso8583Response.set("11", "001271");
            iso8583Response.set("22", "052");
            iso8583Response.set("24", "875");
            iso8583Response.set("25", "00");

            iso8583Response.set(35, "7A5A\u0012e\t\u0083Ò`R\u0001\u0007\u0097yp\u0010");
            iso8583Response.set(41, "40203344");
            iso8583Response.set(42, "000000009913000");
            iso8583Response.set(55, "\u0001A\u009F'\u0001\u0080\u009F\u0010\u0007\u0006\u0001\n\u0003  \u0002\u009F7\u0004$wRð\u009F6\u0002\u00018\u0095\u0005\u0080\u0080\u009A\u0003#\u0004\t\u009C\u0001\u009F\u0002\u0006C_*\u0002\u0001D\u0082\u0002<\u009F\u001A\u0002\u0001D\u009F\u0003\u0006\u009F3\u0003à¸È\u009F4\u0003\u001E\u0003\u009F5\u0001\"\u009F\u001E\b04702988\u0084\u0007 \u0003\u0010\u0010\u009F\t\u0002\u008C_4\u0001\u0001\u009B\u0002è\u009F@\u0005ÿ\u0080ð \u0001\u009F&\b®\u0094oP\u0018FT\t");
            iso8583Response.set(62, "\u0006001271");

            iso8583Response.setPackager(packager);
            byte[] packedData = iso8583Response.pack();

            dataOutputStream.writeInt(packedData.length);
            dataOutputStream.write(packedData);
            dataOutputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
