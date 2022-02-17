package com.xfinites.survive;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class WriteThread extends Thread {
    private PrintWriter writer;
    private final Socket socket;
    private final ChatClient client;
    private Main main;


    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;


        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {

       // Console console = System.console();
        int random = (int)(Math.random() * 500 + 1);

        String userName = "Client_" + random;
        client.setUserName(userName);
        writer.println(userName);

        String text;
        do {
            System.out.println(client.textSent);
            if(client.textSent) {
                text = client.sendText;
                System.out.println(text);
                writer.println(text);
                client.textSent = false;


            }

        } while (true);

    }
}