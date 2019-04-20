package com.name.server;

import com.name.server.dataprovider.IServerDataProvider;
import com.name.server.dataprovider.ServerFilesDataProvider;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class Server {

    private final static int SERVER_PORT = 8050;

    private final static String REQUEST_SEPARATOR = " ";

    public static void main(String[] args) {

        IServerDataProvider dataProvider = ServerFilesDataProvider.getInstance();

        Socket clientSocket = null;
        ObjectInputStream universalInputStream = null;
        ObjectOutputStream universalOutputStream = null;

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {

            clientSocket = serverSocket.accept();

            universalInputStream = new ObjectInputStream(clientSocket.getInputStream());
            universalOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

            String request = (String) universalInputStream.readObject();
            String[] splitRequest = request.split(REQUEST_SEPARATOR);

            System.out.println(splitRequest[2]);

            File requestedFile = dataProvider.getFileByName(splitRequest[2]);

            if (requestedFile != null) {
                // Inform Client if it's possible to proceed its request
                universalOutputStream.writeBoolean(true);

                int requestedFileLength = (int) requestedFile.length();

                // Send Client the length of the requested file
                universalOutputStream.writeInt(requestedFileLength);

                byte[] requestedFileInBytes = new byte[requestedFileLength];

                try (BufferedInputStream buffFileInputStream = new BufferedInputStream(new FileInputStream(requestedFile))) {
                    // TODO handle with read return statement
                    buffFileInputStream.read(requestedFileInBytes);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                universalOutputStream.flush();

                // Send Client the requested file as a byte array
                universalOutputStream.write(requestedFileInBytes);

            } else {
                universalOutputStream.writeBoolean(false);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (universalInputStream != null) {
                try {
                    universalInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (universalOutputStream != null) {
                try {
                    universalOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
