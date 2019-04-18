package com.name.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            // Make the connection between Client and Server
            serverSocket = new ServerSocket(8030);                                  // port - identify the process on the server
            clientSocket = serverSocket.accept();                                        // accept() listen for a connection and accept Client socket

//            final int NUM_OF_FILE_IN_DATABASE = databaseFiles.length;

            // Prepare a stream to send data to the Client
            OutputStream outputStream = clientSocket.getOutputStream();

            // Prepare a stream to read from database files
            FileInputStream fileInputStream;

            // Wrap output stream for base types
            DataOutputStream outputStreamWrapForBaseTypes = new DataOutputStream(outputStream);

            // Send the number of the files
            outputStreamWrapForBaseTypes.writeInt(NUM_OF_FILE_IN_DATABASE);

            for(int i = 0; i < NUM_OF_FILE_IN_DATABASE; ++i) {

                // Tie the file No i and the file input stream
                fileInputStream = new FileInputStream(databaseFiles[i]);
                final int CURRENT_FILE_LENGTH = (int) databaseFiles[i].length();

                // Send the length of array of bytes to the Client
                outputStreamWrapForBaseTypes.writeInt(CURRENT_FILE_LENGTH);

                // Read info from the database (file) to array of bytes
                byte[] arrayOfBytes = new byte[CURRENT_FILE_LENGTH];
                fileInputStream.read(arrayOfBytes, 0, CURRENT_FILE_LENGTH);

                // Send the array of bytes to the Client
                outputStream.write(arrayOfBytes, 0, CURRENT_FILE_LENGTH);
            }
            /*
                FileInputStream - подкласс абстрактного класса InputStream для
                чтения последовательности байтов
            */
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

//======================================================================================================================
//        File file = new File("pathname");
//        try (ServerSocket serverSocket = new ServerSocket(8030);
//             Socket socket = serverSocket.accept();
//             BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(GLOBAL_DICTIONARY_PATH)));
//             OutputStream os = socket.getOutputStream()) {

// Нужно ли оборачивать потоки классами BufferedInputStream, BufferedOutputStream
// Если да, то зачем?
// Если нет, то зачем они нужны?
