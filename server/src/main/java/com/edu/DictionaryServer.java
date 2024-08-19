package com.edu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class DictionaryServer {
    private ServerSocket server;
    private HashMap<String, ArrayList<String>> dictionary;
    public static final String STOP_STRING = "##";
    private static final String DICTIONARY_FILE = "server/dictionary.txt";
    private int index = 0;
    
    public DictionaryServer(int port){
        dictionary = new HashMap<>();
        loadDictionaryFromFile();

        try {
            server = new ServerSocket(port);
            System.out.println("Server started on port " + port + "...");
            while (true) {
                iniConnections();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void iniConnections() throws IOException{
        Socket clientSocket = server.accept();

        // Threads
        if(clientSocket.isConnected()){
            new Thread(()->{
                index++;
                ConnectedClient client = new ConnectedClient(clientSocket, index, dictionary);
                client.readMessage();
                client.close();
            }).start();
        }
    }

    private void loadDictionaryFromFile() {
        File file = new File(DICTIONARY_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":", 2);
                    if (parts.length == 2) { // Ensure there are exactly two parts
                        String word = parts[0];
                        String[] meanings = parts[1].split(";");
                        ArrayList<String> meaningList = new ArrayList<>();
                        for (String meaning : meanings) {
                            meaningList.add(meaning);
                        }
                        dictionary.put(word, meaningList);
                    } else {
                        System.err.println("Invalid line format in dictionary file: " + line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Dictionary file not found.");
        }
    }
    
    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 3080;
        new DictionaryServer(port);
    }
}


