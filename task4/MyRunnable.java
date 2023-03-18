import tcpclient.TCPClient;

import java.net.*;
import java.io.*;

public class MyRunnable implements Runnable {
    Socket socket;

    public MyRunnable(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                String inputLine;
                String[] inputArray;
                String hostname = null;
                int port = 0;
                String stringToServer = "";
                String response = null;
                inputLine = in.readLine();
                boolean shutdown = false;
                Integer timeout = null;
                Integer limit = null;

                inputArray = inputLine.split("[?&= ]");

                if (inputArray[0].contains("GET") && inputLine.contains("HTTP/1.1")) {
                    //if (inputLine.contains("HTTP/1.1")) {
                    if (inputArray[1].equals("/ask")) {
                        for (int i = 0; i < inputArray.length; i++) {
                            switch (inputArray[i]) {
                                case "hostname" -> {
                                    hostname = inputArray[i + 1];
                                }
                                case "port" -> {
                                    port = Integer.parseInt(inputArray[i + 1]);
                                }
                                case "shutdown" -> {
                                    shutdown = Boolean.parseBoolean(inputArray[i + 1]);
                                }
                                case "timeout" -> {
                                    timeout = Integer.parseInt(inputArray[i + 1]);
                                }
                                case "limit" -> {
                                    limit = Integer.parseInt(inputArray[i + 1]);
                                }
                                case "string" -> {
                                    stringToServer = inputArray[i + 1];
                                }
                            }
                        }
                    }
                    try {
                        TCPClient tcpClient = new TCPClient(shutdown, timeout, limit);
                        byte[] fromServerBytes = tcpClient.askServer(hostname, port, stringToServer.getBytes());
                        String code = "HTTP/1.1 200 OK\r\n\r\n";
                        response = (code + new String(fromServerBytes));
                        out.write(response.getBytes());
                        socket.close();

                    } catch (Exception e) {
                        response = "HTTP/1.1 404 Not Found\r\n\r\n";
                        out.write(response.getBytes());
                        socket.close();
                    }
                } else {
                    response = "HTTP/1.1 400 Bad Request\r\n\r\n";
                    out.write(response.getBytes());
                    socket.close();
                }
                socket.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
