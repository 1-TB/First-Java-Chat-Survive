package com.xfinites.surviveserver;

import java.io.*;
import java.net.Socket;

public class UserThread extends Thread {
    private final Socket socket;
    private final ChatServer server;
    private PrintWriter writer;

    public UserThread(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            printUsers();

            String userName = reader.readLine();
            server.addUserName(userName);

            String serverMessage = "New user connected: " + userName;
            server.broadcast(serverMessage, this);

            String clientMessage;

            do {
                clientMessage = reader.readLine();

               if(clientMessage == null ){
                   server.removeUser(userName, this);
                   socket.close();

                   serverMessage = userName + " has left.";
                   server.broadcast(serverMessage, this);

                }else if(clientMessage.equals("/help")){


                   serverMessage = "[System]: " + "Commands are /help";

                   server.systemMessage(serverMessage,this);


               }
               else{

                    serverMessage = "[" + userName + "]: " + clientMessage;

                    server.broadcast(serverMessage, this);
                }

            } while (!(clientMessage == null));

            server.removeUser(userName, this);
            socket.close();

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    void printUsers() {
        if (server.hasUsers()) {
            writer.println("Connected users: " + server.getUserNames());
        } else {
            writer.println("No other users connected");
        }
    }

    void sendMessage(String message) {
        writer.println(message);
    }
}