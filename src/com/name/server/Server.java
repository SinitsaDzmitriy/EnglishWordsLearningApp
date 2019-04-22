package com.name.server;

import com.name.server.dataprovider.IServerDataProvider;
import com.name.server.dataprovider.ServerFilesDataProvider;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    // private final IServerVerifier VERIFIER = SimpleVerifier.getInstance();
    private final static IServerDataProvider DATA_PROVIDER = ServerFilesDataProvider.getInstance();

    private final static int SERVER_PORT = 8050;
    private final static String REQUEST_SEPARATOR = " ";

    private Server() {
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT, 5)) {

            while (true) {
                new RequestProcessingThread(serverSocket.accept()).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class RequestProcessingThread extends Thread {
        private final Socket CLIENT_SOCKET;
        private ObjectInputStream socketInputStream;
        private ObjectOutputStream socketOutputStream;

        RequestProcessingThread(Socket clientSocket) {
            CLIENT_SOCKET = clientSocket;
        }

        @Override
        public void run() {
            try {
                socketInputStream = new ObjectInputStream(CLIENT_SOCKET.getInputStream());
                socketOutputStream = new ObjectOutputStream(CLIENT_SOCKET.getOutputStream());

                String request = (String) socketInputStream.readObject();
                String[] splitRequest = request.split(REQUEST_SEPARATOR);

                switch (splitRequest[0]) {
                    case "get":
                        processGetRequest(splitRequest[1], splitRequest[2]);
                        break;
                    case "verify":
                        // processVerifyRequest();
                        break;
                    default:
                        // TODO Add some actions
                }

            } catch (ClassNotFoundException e) {
                System.err.println(e.getMessage());
            } catch (IOException e) {
                System.err.println("IOException: socket streams operation failure");
            } finally {
                if (socketInputStream != null) {
                    try {
                        socketInputStream.close();
                    } catch (IOException e) {
                        System.err.println();
                    }
                }
                if (socketOutputStream != null) {
                    try {
                        socketOutputStream.close();
                    } catch (IOException e) {
                        System.err.println();
                    }
                }
                if (CLIENT_SOCKET != null) {
                    try {
                        CLIENT_SOCKET.close();
                    } catch (IOException e) {
                        System.err.println();
                    }
                }
            }
        }

        private void processGetRequest(String tag, String fileName) {

            File requestedFile = DATA_PROVIDER.getFileByName(fileName);

            try {

                // If file with such name wasn't found
                if (requestedFile == null) {
                    throw new FileNotFoundException("FileNotFoundException: file " + fileName + " wasn't found.");
                } else {
                    int requestedFileLength = ((int) requestedFile.length() / 1024 + 1) * 1024;
                    byte[] requestedFileInBytes = new byte[requestedFileLength];

                    // Read data from the file
                    try (BufferedInputStream buffFileInputStream = new BufferedInputStream(new FileInputStream(requestedFile))) {

                        // If end of stream has been reached unexpectedly
                        if (buffFileInputStream.read(requestedFileInBytes) == -1) {
                            throw new EOFException("EOFException: unexpected end of " + fileName + " file while reading.");
                        }

                    } catch (EOFException e) {
                        System.err.println(e.getMessage());
                        throw new IOException("IOException: " + fileName + " file reading failure.", e);
                    } catch (FileNotFoundException e) {
                        System.err.println("FileNotFoundException: " + fileName + " file was deleted at the time of processing.");
                        throw new IOException("IOException: " + fileName + " file reading failure.", e);
                    } catch (IOException e) {
                        String eMessage = "IOException: " + fileName + " file reading failure.";
                        System.err.println(eMessage);
                        throw new IOException(eMessage, e);
                    }

                    try {
                        // Inform Client that it's possible to upload the file
                        socketOutputStream.writeBoolean(true);

                        // Send Client the length of the requested file
                        socketOutputStream.writeInt(requestedFileLength);
                        socketOutputStream.flush();

                        // Send Client the file as a byte array
                        socketOutputStream.write(requestedFileInBytes);
                    } catch (IOException e) {
                        System.err.println("SocketIOException: socket writing failure.");
                    }

                }

            } catch (IOException e) {
                try {
                    socketOutputStream.writeBoolean(false);
                    socketOutputStream.writeObject("[Server]" + e.getMessage());
                } catch (IOException innerException) {
                    System.err.println("IOException: socket writing failure.");
                }
            }
        }
    }
}
