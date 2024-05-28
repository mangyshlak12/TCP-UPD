import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPServer {
    private static DatagramSocket socket;
    private static Map<String, String> keyvalStore;

    public static void main(String[] args) throws IOException {
        final int PORT = 8081;
        socket = new DatagramSocket(PORT);
        keyvalStore = new HashMap<>();

        System.out.println("Server started on port " + PORT);

        while (true) {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);
            String cmd = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();

            System.out.println("Received command: " + cmd);

            String response = "";
            if (cmd.startsWith("PUT")) {
                response = handlePutRequest(cmd);
            } else if (cmd.equals("KEYS")) {
                response = handleKeysRequest();
            } else if (cmd.startsWith("GET")) {
                response = handleGetRequest(cmd);
            } else if (cmd.startsWith("DELETE")) {
                response = handleDelRequest(cmd);
            } else {
                response = "Unknown command";
            }

            System.out.println("Sending response: " + response);

            byte[] sendData = response.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
            socket.send(sendPacket);
        }
    }

    private static String handlePutRequest(String cmd) {
        String[] parts = cmd.split(" ");
        if (parts.length < 3) {
            return "Invalid PUT command format";
        }
        String key = parts[1];
        String value = parts[2];
        keyvalStore.put(key, value);
        return getCurrentTimeStamp() + " Success!";
    }

    private static String handleGetRequest(String cmd) {
        String[] parts = cmd.split(" ");
        if (parts.length < 2) {
            return "Invalid GET command format";
        }
        String key = parts[1];
        if (keyvalStore.containsKey(key)) {
            return "Value for key '" + key + "': " + keyvalStore.get(key);
        } else {
            return "Key '" + key + "' does not exist";
        }
    }

    private static String handleDelRequest(String cmd) {
        String[] parts = cmd.split(" ");
        if (parts.length < 2) {
            return "Invalid DELETE command format";
        }
        String key = parts[1];
        if (keyvalStore.containsKey(key)) {
            keyvalStore.remove(key);
            return "Key '" + key + "' removed successfully";
        } else {
            return "Key '" + key + "' does not exist";
        }
    }

    private static String handleKeysRequest() {
        if (keyvalStore.isEmpty()) {
            return "No keys stored";
        }
        StringBuilder keys = new StringBuilder("Keys stored:\n");
        for (String key : keyvalStore.keySet()) {
            keys.append(key).append("\n");
        }
        return keys.toString();
    }

    private static String getCurrentTimeStamp() {
        long timestamp = System.currentTimeMillis();
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
