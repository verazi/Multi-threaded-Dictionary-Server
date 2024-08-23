package com.edu;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DictionaryClient extends Application {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private TextField wordField;
    private TextField meaningField;
    private TextField existingMeaningField;
    private TextArea logArea;
    private ListView<String> meaningsList;
    private Button queryButton;
    private Button addButton;
    private Button removeButton;
    private Button addMeaningButton;
    private Button updateMeaningButton;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Dictionary App");

        // Start with the connection page
        showConnectionPage();
    }

    private void showConnectionPage() {
        // UI components for the connection page
        TextField serverField = new TextField();
        serverField.setPromptText("Enter server address");
        TextField portField = new TextField();
        portField.setPromptText("Enter port");

        Button connectButton = new Button("Connect");
        Label statusLabel = new Label();

        // Layout for the connection page
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        gridPane.add(new Label("Server Address:"), 0, 0);
        gridPane.add(serverField, 1, 0);
        gridPane.add(new Label("Port:"), 0, 1);
        gridPane.add(portField, 1, 1);
        gridPane.add(connectButton, 1, 2);
        gridPane.add(statusLabel, 1, 3);

        connectButton.setOnAction(e -> {
            String serverAddress = serverField.getText().trim();
            String portText = portField.getText().trim();
            
            // Validate port number
            try {
                int port = Integer.parseInt(portText);
                if (port < 0 || port > 65535) {
                    statusLabel.setText("Error: Port number must be between 0 and 65535.");
                } else {
                    connectToServer(serverAddress, port, statusLabel);
                }
            } catch (NumberFormatException ex) {
                statusLabel.setText("Error: Invalid port number. Please enter a valid integer.");
            }
        });

        // Create and show the scene
        Scene connectionScene = new Scene(gridPane, 400, 200);
        primaryStage.setScene(connectionScene);
        primaryStage.show();
    }

    private void connectToServer(String serverAddress, int port, Label statusLabel) {
        Task<Void> connectTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    socket = new Socket(serverAddress, port);
                    out = new DataOutputStream(socket.getOutputStream());
                    in = new DataInputStream(socket.getInputStream());
                    Platform.runLater(() -> {
                        statusLabel.setText("Connected to server.");
                        showDictionaryPage();  // Transition to the dictionary page
                    });
                } catch (IOException e) {
                    Platform.runLater(() -> statusLabel.setText("Failed to connect: " + e.getMessage()));
                }
                return null;
            }
        };
        new Thread(connectTask).start();
    }

    private void showDictionaryPage() {
        // UI components for the dictionary page
        wordField = new TextField();
        wordField.setPromptText("Enter word");
    
        meaningField = new TextField();
        meaningField.setPromptText("Enter meaning");
    
        existingMeaningField = new TextField();
        existingMeaningField.setPromptText("Enter existing meaning (for update)");
    
        meaningsList = new ListView<>();
    
        queryButton = new Button("Query");
        addButton = new Button("Add Word");
        removeButton = new Button("Remove Word");
        addMeaningButton = new Button("Add Meaning");
        updateMeaningButton = new Button("Update Meaning");
    
        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
    
        // Layout for the dictionary page
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
    
        gridPane.add(new Label("Word:"), 0, 0);
        gridPane.add(wordField, 1, 0);
        gridPane.add(new Label("Meanings:"), 0, 1);
        gridPane.add(meaningsList, 1, 1, 1, 4);
    
        HBox meaningInputBox = new HBox(20, meaningField, addMeaningButton, updateMeaningButton);
        gridPane.add(meaningInputBox, 1, 5);
    
        HBox existingMeaningInputBox = new HBox(20, existingMeaningField);
        gridPane.add(existingMeaningInputBox, 1, 6);
    
        VBox buttonBox = new VBox(10, queryButton, addButton, removeButton);
        gridPane.add(buttonBox, 2, 0, 1, 4);
        gridPane.add(new Label("Log:"), 0, 7);
        gridPane.add(logArea, 1, 7, 2, 1);
    
        // Set button actions
        queryButton.setOnAction(e -> runTask(this::queryWord));
        addButton.setOnAction(e -> runTask(this::addWord));
        removeButton.setOnAction(e -> runTask(this::removeWord));
        addMeaningButton.setOnAction(e -> runTask(this::addMeaning));
        updateMeaningButton.setOnAction(e -> runTask(this::updateMeaning));
    
        // Create and show the dictionary page
        Scene dictionaryScene = new Scene(gridPane, 600, 400);
        primaryStage.setScene(dictionaryScene);
    }

    private synchronized void queryWord() {
        String word = wordField.getText().trim();
        if (word.isEmpty()) {
            Platform.runLater(() -> logArea.appendText("Please enter a word to query.\n"));
            return;
        }

        try {
            out.writeUTF("QUERY%" + word);
            String response = readResponse();
            Platform.runLater(() -> {
                meaningsList.getItems().clear();
                updateLogAndList(response);
            });
        } catch (IOException e) {
            handleError("Error querying the word.", e);
        }
    }
    private synchronized void addWord() {
        String word = wordField.getText().trim();
        String meaning = meaningField.getText().trim();

        if (word.isEmpty() || meaning.isEmpty()) {
            Platform.runLater(() -> logArea.appendText("Please enter both word and meaning to add.\n"));
            return;
        }

        try {
            out.writeUTF("ADD%" + word + "%" + meaning);
            String response = readResponse();
            Platform.runLater(() -> {
                meaningsList.getItems().clear();
                updateLogAndList(response);
            });
        } catch (IOException e) {
            handleError("Error adding the word.", e);
        }
    }

    private synchronized void removeWord() {
        String word = wordField.getText().trim();
        if (word.isEmpty()) {
            Platform.runLater(() -> logArea.appendText("Please enter a word to remove.\n"));
            return;
        }

        try {
            out.writeUTF("REMOVE%" + word);
            String response = readResponse();
            Platform.runLater(() -> {
                meaningsList.getItems().clear();
                updateLogAndList(response);
            });
        } catch (IOException e) {
            handleError("Error removing the word.", e);
        }
    }

    private void addMeaning() {
        String word = wordField.getText().trim();
        String meaning = meaningField.getText().trim();

        if (word.isEmpty() || meaning.isEmpty()) {
            Platform.runLater(() -> logArea.appendText("Please enter both word and meaning to add.\n"));
            return;
        }

        try {
            out.writeUTF("ADD_MEANING%" + word + "%" + meaning);
            String response = readResponse();
            Platform.runLater(() -> {
                meaningsList.getItems().clear();
                updateLogAndList(response);
            });
        } catch (IOException e) {
            handleError("Error adding meaning to the word.", e);
        }
    }

    private synchronized void updateMeaning() {
        String word = wordField.getText().trim();
        String existingMeaning = existingMeaningField.getText().trim();
        String newMeaning = meaningField.getText().trim();
    
        if (word.isEmpty() || existingMeaning.isEmpty() || newMeaning.isEmpty()) {
            Platform.runLater(() -> logArea.appendText("Please enter word, existing meaning, and new meaning to update.\n"));
            return;
        }
    
        try {
            out.writeUTF("UPDATE_MEANING%" + word + "%" + existingMeaning + "%" + newMeaning);
            String response = readResponse();
            Platform.runLater(() -> {
                meaningsList.getItems().clear();
                updateLogAndList(response);
            });
        } catch (IOException e) {
            handleError("Error updating the word's meaning.", e);
        }
    }
    

    private String readResponse() throws IOException {
        return in.readUTF();
    }

    private void updateLogAndList(String response) {
        Platform.runLater(() -> {
            logArea.appendText(response + "\n");
            String[] lines = response.split("\n");
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    if (!line.startsWith("Meanings of")) {
                        meaningsList.getItems().add(line);
                    }
                }
            }
        });
    }

    private void handleError(String logMessage, Exception e) {
        e.printStackTrace();
        logArea.appendText(logMessage + "\n");
    }

    private void runTask(Runnable task) {
        Task<Void> backgroundTask = new Task<>() {
            @Override
            protected Void call() {
                task.run();
                return null;
            }
        };
        new Thread(backgroundTask).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
