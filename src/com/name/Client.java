package com.name;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) {
        Socket clientSocket = null;

//        try {
//            clientSocket = new Socket(InetAddress.getLocalHost(),8030);
//            InputStream inputStream = clientSocket.getInputStream();
//
//            DataInputStream inputStreamWrapForBaseTypes = new DataInputStream(inputStream);
//
//            final int NUM_OF_FILE_IN_DATABASE = inputStreamWrapForBaseTypes.readInt();
//            File[] databaseFiles = new File[NUM_OF_FILE_IN_DATABASE];
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
//
//            byte[] arrayOfBytes = new byte[FILE_LENGTH];
//
//            if(inputStream.read(arrayOfBytes) == -1) {
//                throw new IOException();
//            } else {
//                File database = new File("clientDatabase.txt");
//                database.createNewFile();
//                FileOutputStream fileOutputStream = new FileOutputStream(database);
//                fileOutputStream.write(arrayOfBytes);
//            }
//        } catch(Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                clientSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    File requestFileWithName(String fileName) {
        //TODO Send a request to the server for a file with name fileName
        return new File(fileName);
    }

}
