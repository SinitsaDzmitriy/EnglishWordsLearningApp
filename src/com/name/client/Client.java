package com.name.client;

import com.name.client.dataprovider.ClientFilesDataProvider;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {
    private final static int SERVER_PORT = 8050;

    private final static String TEMP_DIR = "buffer";

    public static void main(String[] args) {

        ClientFilesDataProvider dataProvider = ClientFilesDataProvider.getInstance();
        ArrayList<String> namesOfNeededFiles = dataProvider.getNamesOfNeededFiles();

        // If list of needed files isn't empty
        if (!namesOfNeededFiles.isEmpty()) {

            Socket clientSocket = null;
            ObjectOutputStream universalOutputStream = null;
            ObjectInputStream universalInputStream = null;

            try {
                clientSocket = new Socket(InetAddress.getLocalHost(), SERVER_PORT);
                universalOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                universalInputStream = new ObjectInputStream(clientSocket.getInputStream());

                File currentFile;
                byte[] currentFileInBytes;
                int currentFileSizeInBytes;
                BufferedOutputStream buffFileOutputStream = null;

                for (String fileName : namesOfNeededFiles) {
                    // Send a "get" request
                    universalOutputStream.writeObject("get base " + fileName);

                    // If the request can be proceed
                    if (universalInputStream.readBoolean()) {
                        // TODO send file length from Server!!!
                        currentFileSizeInBytes = universalInputStream.readInt();
                        currentFileInBytes = new byte[currentFileSizeInBytes];

                        //==============================================================================================

                        // If the end of the stream reached earlier then expected
                        if (universalInputStream.read(currentFileInBytes) == -1) {
                            // TODO what should I do here?
                            System.err.println("Exception: data storage recovery failure");
                            System.exit(1);
                        } else {
                            // Create a file-connected object
                            currentFile = new File(TEMP_DIR + File.separator + fileName);

                            // Prepare a physical file for writing
                            if (currentFile.exists()) {
                                /*
                                 * If size of a exist file is bigger then length of another one with
                                 * the same name which was sent from the Server as a byte array
                                 */
                                if (currentFile.length() > currentFileSizeInBytes) {
                                    if (!currentFile.delete()) { throw new IOException("Exception: file " + currentFile.getName() + " delete failure"); }
                                    if (!currentFile.createNewFile()) { throw new IOException("Exception: file " + currentFile.getName() + " creation failure"); }
                                }
                            } else {
                                if (!currentFile.createNewFile()) { throw new IOException("Exception: file " + currentFile.getName() + " creation failure"); }
                            }

                            // Create stream and write in the file
                            try {
                                buffFileOutputStream = new BufferedOutputStream(new FileOutputStream(currentFile, false));
                                buffFileOutputStream.write(currentFileInBytes);
                            } catch (IOException e) {
                                e.printStackTrace();
                                // TODO some actions
                            } finally {
                                if (buffFileOutputStream != null) {
                                    try {
                                        buffFileOutputStream.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                try {
                                    if (buffFileOutputStream != null) {
                                        buffFileOutputStream.close();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            dataProvider.pushFile(currentFile);
                        }
                    }
                }

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
}

//======================================================================================================================
// TODO [?] 1. If a file wasn't successfully deleted

/*
 * a:
 * System.err.println("Exception: file " + currentFile.getName() + " delete failure");
 * System.exit(1);
 * b:
 * throw new IOException("Exception: file " + currentFile.getName() + " delete failure");
 */