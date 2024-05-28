import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class TCPClient {

    static PrintWriter dataOut;
    static Scanner dataIn;
    static Socket input;


    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        input = new Socket("127.0.0.1",8081);
        dataOut = new PrintWriter(input.getOutputStream());
        dataIn = new Scanner(input.getInputStream());

        boolean k = true;
         while(k){


             System.out.println("Please input command in either of following forms: ");
             System.out.println("    GET <key>");
             System.out.println("    PUT <key> <val>");
             System.out.println("    DELETE <key>");
             System.out.println("    KEYS");
             System.out.println("    QUIT");

             System.out.print("Enter command: ");
             String command = scan.nextLine();

             String[] parts = command.split(" ");
             if (parts.length < 1) {
                 System.err.println(  "  Invalid command format");
             }
             String action = parts[0].toUpperCase();
             switch (action) {
                 case "GET":
                     if (parts.length != 2) {
                         System.out.println(  " Invalid GET command format.");
                         break;
                     }
                     String cur = parts[1];
                     handleGetRequest(cur,"GET");
                     System.out.println(dataIn.nextLine());
                     System.out.println(dataIn.nextLine());
                     break;

                 case "PUT":
                     if (parts.length != 3) {
                         System.err.println( " Invalid PUT command format.");
                         break;
                     }
                     String putKey = parts[1];
                     String putValue = parts[2];
                     handlePutRequest(putKey,putValue,"PUT");
                     System.out.println(dataIn.nextLine());

                     break;

                 case "DELETE":
                     if (parts.length != 2) {
                         System.out.println(  " Invalid DELETE command format.");
                         break;
                     }
                     String deleteKey = parts[1];
                     handleDelRequest(deleteKey,"DELETE");
                     System.out.println(dataIn.nextLine());
                     System.out.println(dataIn.nextLine());

                     break;

                 case "KEYS":
                     handleKeysRequest("KEYS");
                        int size = Integer.valueOf(dataIn.nextLine());
                       for(int i = 0; i<size; i++){
                           System.out.println(dataIn.nextLine());
                       }
                     System.out.println(dataIn.nextLine());


                     break;

                 case "QUIT":
                    k =false;
                     break;

             }
         }
        dataOut.close();
        input.close();


    }

    public static void handlePutRequest(String key,String value, String cmd) throws IOException {
        dataOut.println(cmd);
        dataOut.println(key);
        dataOut.println(value);
        dataOut.flush();


    }
    public static void handleDelRequest(String key,  String cmd){
        dataOut.println(cmd);
        dataOut.println(key);
        dataOut.flush();
    }
    public static void handleGetRequest(String key, String cmd){
        dataOut.println(cmd);
        dataOut.println(key);
        dataOut.flush();
    }
    public static void handleKeysRequest(String cmd){
        dataOut.println(cmd);
        dataOut.flush();
    }
}
