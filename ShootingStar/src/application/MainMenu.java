package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu extends Application {

    private Stage primaryStage;
    private Scene mainMenuScene;
    private Scene helloWorldScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Create buttons for the main menu
        Button playButton = new Button("Play");
        Button storeButton = new Button("Store");
        Button settingsButton = new Button("Settings");

        // Set action handlers for the buttons
        playButton.setOnAction(e -> showHelloWorldScene());
        storeButton.setOnAction(e -> showHelloWorldScene());
        settingsButton.setOnAction(e -> showHelloWorldScene());

        // Layout for the main menu
        VBox mainMenuLayout = new VBox(10);
        mainMenuLayout.getChildren().addAll(playButton, storeButton, settingsButton);
        mainMenuScene = new Scene(mainMenuLayout, 300, 200);

        // Create the "Hello World!" scene
        Button backButton = new Button("Back to Main Menu");
        backButton.setOnAction(e -> primaryStage.setScene(mainMenuScene));
        VBox helloWorldLayout = new VBox(10);
        helloWorldLayout.getChildren().addAll(new Button("Hello World!"), backButton);
        helloWorldScene = new Scene(helloWorldLayout, 300, 200);

        // Set the initial scene to the main menu
        primaryStage.setScene(mainMenuScene);
        primaryStage.setTitle("Simple Main Menu");
        primaryStage.show();
    }

    // Method to switch to the "Hello World!" scene
    private void showHelloWorldScene() {
        primaryStage.setScene(helloWorldScene);
    }
}
