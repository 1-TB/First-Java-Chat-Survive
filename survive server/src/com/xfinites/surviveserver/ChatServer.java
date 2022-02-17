package com.xfinites.surviveserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {
    private final int port;
    private final Set<String> userNames = new HashSet<>();
    private final Set<UserThread> userThreads = new HashSet<>();

    public ChatServer(int port) {
        this.port = port;
    }

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Chat Server is listening on port " + port);

            //noinspection InfiniteLoopStatement
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");

                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();

            }

        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void run() {

        int port = 5001;

        ChatServer server = new ChatServer(port);
        server.execute();
    }

    void broadcast(String message, UserThread excludeUser) {
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }
    void systemMessage(String message, UserThread User) {
        for (UserThread aUser : userThreads) {
            if (aUser.equals(User)) {
                aUser.sendMessage(message);
            }
        }
    }


    void addUserName(String userName) {
        userNames.add(userName);
    }

    //remove name in user thread
    void removeUser(String userName, UserThread aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            System.out.println("The user " + userName + " left");
        }
    }

    Set<String> getUserNames() {
        return this.userNames;
    }


    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }
}