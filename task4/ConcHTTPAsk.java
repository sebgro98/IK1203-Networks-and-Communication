import java.net.*;
import java.io.*;


public class ConcHTTPAsk {
    public static void main(String[] args) {

        int port = Integer.parseInt(args[0]);
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while (true) {
                Socket socket = serverSocket.accept();
                MyRunnable runnable = new MyRunnable(socket);
                Thread thread = new Thread(runnable);
                thread.start();

            }

        } catch (Exception e) {
            System.out.println("Could not listen on port: " + port);
            System.exit(1);
        }

    }




}

