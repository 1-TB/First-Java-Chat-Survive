package com.xfinites.survive;

import java.io.IOException;
import java.net.Socket;

public class ChatClient extends Main{
    private final String hostname;
    private final int port;
    private String userName;

    public ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void execute() {
        try {
            Socket socket = new Socket(hostname, port);

            //System.out.println("Connected to the chat server");
            showLabel("Connected to the server!");

            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();

        }catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }

    }

    void setUserName(String userName) {
        this.userName = userName;
    }

    String getUserName() {
        return this.userName;
    }


    public static void main() {

        String hostname = "127.0.0.1";
        int port = 5001;

        ChatClient client = new ChatClient(hostname, port);
        client.execute();
    }
}