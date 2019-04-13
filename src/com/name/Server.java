package com.name;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            // [+] 1. Make the connection between Client and Server
            serverSocket = new ServerSocket(8030);                                  // port - identify the process on the server
            clientSocket = serverSocket.accept();                                        // accept() listen for a connection and accept Client socket

            // [+] 2. Prepare the database (file) to provide info
            File database = new File("serverDatabase.txt");
            FileInputStream fileInputStream = new FileInputStream(database);
            final int FILE_LENGTH = (int) database.length();

            // [+] 3. Prepare a stream to send data to the Client
            OutputStream outputStream = clientSocket.getOutputStream();

            // [+] 4. Wrap output stream for base types
            DataOutputStream outputStreamWrapForBaseTypes = new DataOutputStream(outputStream);

            // [+] 5.Send the length of array of bytes to the Client
            outputStreamWrapForBaseTypes.writeInt(FILE_LENGTH);

            // [+] 6. Read info from the database (file) to array of bytes
            byte[] arrayOfBytes = new byte[FILE_LENGTH];
            fileInputStream.read(arrayOfBytes, 0, FILE_LENGTH);

            // [+] 7. Send the array of bytes to the Client
            outputStream.write(arrayOfBytes, 0, FILE_LENGTH);
            
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
