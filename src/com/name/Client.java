package com.name;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) {
        Socket clientSocket = null;

        try {
            clientSocket = new Socket(InetAddress.getLocalHost(),8030);
            InputStream inputStream = clientSocket.getInputStream();

            DataInputStream inputStreamWrapForBaseTypes = new DataInputStream(inputStream);
            final int FILE_LENGTH = inputStreamWrapForBaseTypes.readInt();

            byte[] arrayOfBytes = new byte[FILE_LENGTH];

            if(inputStream.read(arrayOfBytes) == -1) {
                throw new IOException();
            } else {
                File database = new File("clientDatabase.txt");
                database.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(database);
                fileOutputStream.write(arrayOfBytes);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
