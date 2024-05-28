import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPClient {

    public static void main(String[] args) throws IOException {
        final String SERVER_IP = "127.0.0.1";
        final int SERVER_PORT = 8081;
        DatagramSocket socket = new DatagramSocket();

        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.println("Please input command in either of the following forms: ");
            System.out.println("    GET <key>");
            System.out.println("    PUT <key> <val>");
            System.out.println("    DELETE <key>");
            System.out.println("    KEYS");
            System.out.println("    QUIT");

            System.out.print("Enter command: ");
            String command = scan.nextLine();

            byte[] sendData = command.getBytes();
            InetAddress serverAddress = InetAddress.getByName(SERVER_IP);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, SERVER_PORT);
            socket.send(sendPacket);

            if (command.equals("QUIT")) {
                break;
            }

            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);
            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println(response);
        }

        socket.close();
    }
}
