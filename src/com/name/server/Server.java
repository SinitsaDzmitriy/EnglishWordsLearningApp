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


        /*
         * Make the connection between Client and Server
         * port - identify the process on the server
         * accept() listen for a connection and accept Client socket
        */

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {

            clientSocket = serverSocket.accept();

//            universalInputStream = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
//            universalOutputStream = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));

            universalInputStream = new ObjectInputStream(clientSocket.getInputStream());
            universalOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

            String request = (String) universalInputStream.readObject();
            String[] splitRequest = request.split(REQUEST_SEPARATOR);

            System.out.println(splitRequest[2]);

            File requestedFile = dataProvider.getFileByName(splitRequest[2]);

            if (requestedFile != null) {
                // Inform Client if it's possible to proceed its request
                universalOutputStream.writeBoolean(true);

                final int requestedFileLength = (int) requestedFile.length();

                // TODO Check English in the commentary below
                // Send Client the length of the requested file
                universalOutputStream.writeInt(requestedFileLength);

                byte[] requestedFileInBytes = new byte[requestedFileLength];

                try (BufferedInputStream buffFileInputStream = new BufferedInputStream(new FileInputStream(requestedFile))){
                    // TODO handle with read return statement
                    buffFileInputStream.read(requestedFileInBytes);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                universalOutputStream.flush();

                // Send Client the requested file as a byte array
                universalOutputStream.write(requestedFileInBytes,0, 79_824 );

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

//           OutputStream inputStream = clientSocket.getOutputStream();

////            final int NUM_OF_FILE_IN_DATABASE = databaseFiles.length;
//
//            // Prepare a stream to send data to the Client
//            OutputStream outputStream = clientSocket.getOutputStream();
//
//            // Prepare a stream to read from database files
//            FileInputStream fileInputStream;
//
//            // Wrap output stream for base types
//            DataOutputStream outputStreamWrapForBaseTypes = new DataOutputStream(outputStream);
//
//            // Send the number of the files
//            outputStreamWrapForBaseTypes.writeInt(NUM_OF_FILE_IN_DATABASE);
//
//            for(int i = 0; i < NUM_OF_FILE_IN_DATABASE; ++i) {
//
//                // Tie the file No i and the file input stream
//                fileInputStream = new FileInputStream(databaseFiles[i]);
//                final int CURRENT_FILE_LENGTH = (int) databaseFiles[i].length();
//
//                // Send the length of array of bytes to the Client
//                outputStreamWrapForBaseTypes.writeInt(CURRENT_FILE_LENGTH);
//
//                // Read info from the database (file) to array of bytes
//                byte[] arrayOfBytes = new byte[CURRENT_FILE_LENGTH];
//                fileInputStream.read(arrayOfBytes, 0, CURRENT_FILE_LENGTH);
//
//                // Send the array of bytes to the Client
//                outputStream.write(arrayOfBytes, 0, CURRENT_FILE_LENGTH);
//            }
//            /*
//                FileInputStream - подкласс абстрактного класса InputStream для
//                чтения последовательности байтов
//            */
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                clientSocket.close();
//                serverSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

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
