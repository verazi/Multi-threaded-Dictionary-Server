package com.edu;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConnectedClient {
    private Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;
    private int ID;
    private HashMap<String, ArrayList<String>> dictionary;

    public ConnectedClient(Socket clientSocket, int ID, HashMap<String, ArrayList<String>> dictionary){
        this.clientSocket = clientSocket;
        this.ID = ID;
        this.dictionary = dictionary;
        System.out.println("Client" + this.ID + " connected");
        try {
            this.in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            this.out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void readMessage() {
        while (true) {
            try {
                String line = in.readUTF();
                if (line.equals(DictionaryServer.STOP_STRING)) {
                    break;
                }
                System.out.println("Client" + ID + ": " + line);
                handleClientCommand(line);
            } catch (EOFException e) {
                System.err.println("Client" + ID + " disconnected unexpectedly.");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        System.out.println("Client" + this.ID + " disconnected");
    }


    private void handleClientCommand(String command) throws IOException {
        String[] parts = command.split("%", 3);
        String action = parts[0].toUpperCase();
        String word;
        String response;

        switch (action) {
            case "QUERY":
                word = parts[1];
                response = queryWord(word);
                System.out.println(response);
                break;

            case "ADD":
                word = parts[1];
                String meaning = parts[2];
                response = addWord(word, meaning);
                break;

            case "REMOVE":
                word = parts[1];
                response = removeWord(word);
                break;

            case "ADD_MEANING":
                word = parts[1];
                meaning = parts[2];
                response = addMeaning(word, meaning);
                break;

            case "UPDATE_MEANING":
                word = parts[1];
                String newMeaning = parts[2];
                response = updateMeaning(word, newMeaning);
                break;

            default:
                response = "Unknown command.";
        }
        out.writeUTF(response);
        out.flush();

        // Save dictionary to file after each modification
        saveDictionaryToFile();
    }

    private String queryWord(String word) {
        if (dictionary.containsKey(word)) {
            StringBuilder sb = new StringBuilder("Meanings of " + word + ":\n");
            for (String meaning : dictionary.get(word)) {
                sb.append(meaning).append("\n");
            }
            return sb.toString();
        } else {
            return "Word not found.";
        }
    }

    private String addWord(String word, String meaning) {
        if (!dictionary.containsKey(word)) {
            dictionary.put(word, new ArrayList<>());
            dictionary.get(word).add(meaning);
            return "Word added successfully.";
        } else {
            return "This word already exists .";
        }
    }

    private String removeWord(String word) {
        if (dictionary.containsKey(word)) {
            dictionary.remove(word);
            return "Word removed successfully.";
        } else {
            return "Word not found.";
        }
    }

    private String addMeaning(String word, String meaning) {
        if (dictionary.containsKey(word)) {
            
            List<String> meanings = dictionary.get(word);
            if (meanings.contains(meaning)) {
                return "Meaning already exists for this word.";
            } else {
                dictionary.get(word).add(meaning);
                return "Additional meaning added successfully.";
            }
        } else {
            return "Word not found.";
        }
    }

    private String updateMeaning(String word, String newMeaning) {
        if (dictionary.containsKey(word)) {
            dictionary.get(word).clear();
            dictionary.get(word).add(newMeaning);
            return "Meaning updated successfully.";
        } else {
            return "Word not found.";
        }
    }

    private void saveDictionaryToFile() {
        File file = new File("dictionary.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String word : dictionary.keySet()) {
                writer.write(word + ":" + String.join(";", dictionary.get(word)));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            clientSocket.close();
            in.close();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

