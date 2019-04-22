package com.name.client;

import com.name.client.dataprovider.ClientFilesDataProvider;
import com.name.client.dataprovider.IClientDataProvider;
import com.name.client.exceptions.ServerSideException;
import com.name.client.menu.Menu;
import com.name.client.menu.commands.RestoreDataStorageCommand;
import com.name.client.menu.commands.TranslatePhraseCommand;
import com.name.common.util.REQUEST;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    private final static int SERVER_PORT = 8050;
    private final static String TEMP_DIR = "buffer";
    private final static IClientDataProvider DATA_PROVIDER = ClientFilesDataProvider.getInstance();
    private final static Scanner KEYBOARD_INPUT = new Scanner(System.in);

    public static void main(String[] args) {

        Menu mainMenu = new Menu("Main Menu", KEYBOARD_INPUT);
        mainMenu.addItem(new TranslatePhraseCommand("Translate phrase", DATA_PROVIDER, KEYBOARD_INPUT));
        mainMenu.addItem(RestoreDataStorageCommand.getInstance());

        mainMenu.interactWithUser();
    }

     public static void checkDataIntegrity() {
        // Get list of needed files
        ArrayList<String> namesOfNeededFiles = DATA_PROVIDER.getNamesOfNeededFiles();

        // If list of needed files isn't empty
        if (!namesOfNeededFiles.isEmpty()) {
            for (String currentFileName : namesOfNeededFiles) {
                // TODO add class for classes of get request
                DATA_PROVIDER.pushFile(getFile("base", currentFileName));
            }
        }
    }

    static File getFile(String fileClass, String fileName) {

        File file = null;
        byte[] fileInBytes;
        int fileLength;

        try {

            // Client-server interaction: Client get a requested file as a byte array
            try (Socket clientSocket = new Socket(InetAddress.getLocalHost(), SERVER_PORT);
                 ObjectOutputStream universalOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                 ObjectInputStream universalInputStream = new ObjectInputStream(clientSocket.getInputStream())) {

                // Send GET request
                universalOutputStream.writeObject(REQUEST.GET.METHOD + REQUEST.SEPARATOR +
                        REQUEST.GET.DATA_TYPE.BASE + REQUEST.SEPARATOR + fileName);

                // If the request can be proceed
                if (universalInputStream.readBoolean()) {

                    // Get the file length from Server
                    fileLength = universalInputStream.readInt();
                    fileInBytes = new byte[fileLength];
                    universalInputStream.readFully(fileInBytes);
                } else {
                    throw new ServerSideException((String) universalInputStream.readObject());
                }
            } catch (IOException e) {
                throw new IOException("IOException: " + fileName + " file upload failure.", e);
            }

            // Create a file-connected object
            file = new File(TEMP_DIR + File.separator + fileName);

            // Client side operations: save the received byte array as a physical file
            // Create stream and write in the file
            try (BufferedOutputStream buffFileOutputStream = new BufferedOutputStream(new FileOutputStream(file, false))) {

                // Prepare a physical file for writing
                if (file.exists()) {
                    if (file.length() > fileLength) {
                        new RandomAccessFile(file, "rw").setLength(fileLength);
                    }
                } else {
                    if (!file.createNewFile()) {
                        throw new FileAlreadyExistsException("FileAlreadyExistsException: " + file.getName() + " creation failure.");
                    }
                }

                buffFileOutputStream.write(fileInBytes);

            } catch (FileAlreadyExistsException e) {
                throw e;
            }catch (FileNotFoundException e) {
                throw new FileNotFoundException("FileNotFoundException: " + fileName + " file was deleted during processing.");
            } catch (IOException e) {
                throw new IOException("IOException: " + fileName + " file saving failure.");
            }

        }catch (IOException | ClassNotFoundException | ServerSideException e) {
            System.err.println(e.getMessage() + "\nNo further correct work is possible.");
            System.exit(1);
        }

        return file;
    }
}