package tcpclient;

import java.net.*;
import java.io.*;

public class TCPClient {
    private static final int BUFFER_SIZE = 1024;

    public TCPClient() {
    }

    public byte[] askServer(String hostname, int port, byte[] toServerBytes) throws IOException {

        Socket socket = new Socket(hostname, port);

        // create an array of bytes in memory buffer, to store data from the output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();


        byte[] fromServerBuffer = new byte[BUFFER_SIZE];

        // Get the socket object then send bytes to it.
        socket.getOutputStream().write(toServerBytes, 0, toServerBytes.length);


        int i = 0;
        // Get the socket object then read what is there.
        while((i = socket.getInputStream().read(fromServerBuffer)) != -1 ) {
            outputStream.write(fromServerBuffer, 0, i);
        }
        socket.close();
        return outputStream.toByteArray();
    }
}
