
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TCPServer {
    static Socket input;
    static ServerSocket serverSocket;
    static PrintWriter dataOut;
    static Scanner dataIn;
    static Map<String,String> keyvalStore;
    static String quit;
    static  String keys;
    static String put;
    static String delete;
    static String get;
    public static void main(String[] args) throws IOException {
        serverSocket = new ServerSocket(8081);
        input  = serverSocket.accept();
        dataIn = new Scanner(input.getInputStream());
        dataOut = new PrintWriter(input.getOutputStream());
        keyvalStore = new HashMap<>();
        while(dataIn.hasNextLine()){
            String cmd = dataIn.nextLine();
            cmd.toUpperCase();
            if(cmd.equals("PUT")){
                handlePutRequest("Asd",8081);
                dataOut.println(put);
            }else if(cmd.equals("KEYS")){
                handleKeysRequest("dad",8081);
                dataOut.println(keys);

            }else if(cmd.equals("GET")){
                handleGetRequest("dad",8081);
                dataOut.println(get);

            }else if(cmd.equals("DELETE")){
                handleDelRequest("dad",8081);
                dataOut.println(delete);
            }
            dataOut.flush();
        }




        dataIn.close();
        input.close();
        serverSocket.close();



    }
    public static void handlePutRequest(String clientSocketIp, int clientSocketPort) throws IOException {
        String key = dataIn.nextLine();
        String value = dataIn.nextLine();
        keyvalStore.put(key,value);
        put = getCurrentTimeStamp() +" Success!";
    }

    public static void handleDelRequest(String clientSocketIp, int clientSocketPort){
        String key = dataIn.nextLine();
        if(keyvalStore.containsKey(key)){
            keyvalStore.remove(key);
            dataOut.println("Removed!");
        }else{
            dataOut.println("Not contain that Key");
        }
        delete = getCurrentTimeStamp() + " Success!";
    }
    public static void handleGetRequest(String clientSocketIp, int clientSocketPort){
        String key = dataIn.nextLine();
        if(keyvalStore.containsKey(key)){
           dataOut.println("Exists!");
        }else{
            dataOut.println("Not Exists!");

        }
        get = getCurrentTimeStamp() + " Success!";

    }
    public static void handleKeysRequest(String clientSocketIp, int clientSocketPort){
            int size = keyvalStore.size();
            dataOut.println(String.valueOf(size));
            for(String i : keyvalStore.keySet()){
                dataOut.println(i);
            }
            keys = getCurrentTimeStamp() +" Success!";

    }
    public static String getCurrentTimeStamp(){
        long timestamp = Long.parseLong(String.valueOf(System.currentTimeMillis()));
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
    public static String getLogHeader(String clientSocketIp, int clientSocketPort){
        return "";
    }
}
