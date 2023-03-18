import java.net.*;
import java.io.*;
import tcpclient.*;

public class HTTPAsk {
    public static void main(String[] args) throws IOException {
        try {
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
            while (true) {

                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

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
                                        limit = Integer.parseInt(inputArray[i + 1]);;
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
                            clientSocket.close();

                        } catch (Exception e) {
                            response = "HTTP/1.1 404 Not Found\r\n\r\n";
                            out.write(response.getBytes());
                            clientSocket.close();
                        }
                    } else {
                        response = "HTTP/1.1 400 Bad Request\r\n\r\n";
                        out.write(response.getBytes());
                        clientSocket.close();
                    }
                }
            }catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + args[0] + " or listening for a connection");
            System.out.println(e.getMessage());
            System.exit(1);
        }
        }
    }


