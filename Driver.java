import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Random;

public class Driver extends Application {
    File file;

    @Override
    public void start(Stage stage) {
        Image background = new Image("background.png");
        BackgroundImage backgroundImage = new BackgroundImage(background,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                null,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true));

        GridPane gridPane = new GridPane();
        gridPane.setBackground(new Background(backgroundImage));

        Button btEncrypt = new Button("Encrypt");
        Button btDecrypt = new Button("Decrypt");
        HBox hButtons = new HBox(10);
        hButtons.getChildren().addAll(btEncrypt, btDecrypt);

        TextField messageLength = new TextField();
        messageLength.setPromptText("Message Length");
        TextArea message = new TextArea();
        message.setPromptText("Message");
        TextField key = new TextField();
        key.setPromptText("2 x 2 key seperated by space");
        Button btChooseImage = new Button("Choose Image");

        btChooseImage.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.bmp"));
            fileChooser.setTitle("Choose Image");
            fileChooser.setInitialDirectory(new java.io.File("."));
            file = fileChooser.showOpenDialog(stage);
            if (file == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No file selected");
                alert.showAndWait();
            }
        });

        btEncrypt.setOnAction(e -> {
            if (file == null || message.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Please fill all fields and make sure to choose an image");
                alert.showAndWait();
            } else {
                try {
                    int[][] keyArr = generateKey();
                    key.setText(keyArr[0][0] + " " + keyArr[0][1] + " " + keyArr[1][0] + " " + keyArr[1][1]);
                    HillCipher hillCipher = new HillCipher(keyArr);
                    hillCipher.setPlaintText(message.getText());
                    String cipherText = hillCipher.encrypt(message.getText());
                    System.out.println("Hill Cipher encrypt:" + cipherText);
                    LSB lsb = new LSB(file, cipherText);
                    lsb.encrypt();
                    messageLength.setText(cipherText.length() + "");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Encryption successful");
                    alert.showAndWait();
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error while encrypting");
                    alert.showAndWait();
                }
            }
        });

        btDecrypt.setOnAction(e -> {
            String length = messageLength.getText();
            if (file == null || length.isEmpty() || key.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Please fill all fields and make sure to choose an image");
                alert.showAndWait();
            } else {
                try {
                    int lengthInt = Integer.parseInt(length);
                    LSB lsb = new LSB(file);
                    String cipherText = lsb.decrypt(lengthInt);
                    System.out.println("LSB decrypt:" + cipherText);
                    String keyStr = key.getText();
                    String[] keyStrArr = keyStr.split(" ");
                    int[][] keyArr = new int[2][2];
                    keyArr[0][0] = Integer.parseInt(keyStrArr[0]);
                    keyArr[0][1] = Integer.parseInt(keyStrArr[1]);
                    keyArr[1][0] = Integer.parseInt(keyStrArr[2]);
                    keyArr[1][1] = Integer.parseInt(keyStrArr[3]);
                    HillCipher hillCipher = new HillCipher(keyArr);
                    String decryptedText = hillCipher.decrypt(cipherText);
                    message.setText(decryptedText);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Decryption successful");
                    alert.showAndWait();
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error while decrypting");
                    alert.showAndWait();
                }

            }
        });

        hButtons.setAlignment(javafx.geometry.Pos.CENTER);

        gridPane.add(messageLength, 0, 1);
        gridPane.add(message, 0, 2);
        gridPane.add(key, 0, 3);
        gridPane.add(hButtons, 0, 4);
        gridPane.add(btChooseImage, 0, 5);

        messageLength.setPrefWidth(200);
        message.setPrefWidth(200);
        key.setPrefWidth(200);
        btChooseImage.setPrefWidth(200);
        hButtons.setPadding(new Insets(10, 0, 0, 0));
        hButtons.setPrefWidth(200);
        hButtons.setPrefHeight(50);
        hButtons.setPadding(new Insets(10, 0, 0, 0));
        hButtons.setPrefWidth(200);
        hButtons.setPrefHeight(50);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(javafx.geometry.Pos.CENTER);

        Scene scene = new Scene(gridPane, 600, 600);
        stage.setScene(scene);
        stage.setTitle("Hill Cipher");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public int[][] generateKey() {
        long seed = System.nanoTime();
        Random random = new Random(seed);
        int[][] key;

        do {
            key = new int[2][2];
            key[0][0] = random.nextInt(9000) + 1000;
            key[0][1] = random.nextInt(9000) + 1000;
            key[1][0] = random.nextInt(9000) + 1000;
            key[1][1] = random.nextInt(9000) + 1000;
        } while (!HillCipher.isValidKey(key));

        return key;
    }
}
