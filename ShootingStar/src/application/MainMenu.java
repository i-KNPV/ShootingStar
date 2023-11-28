package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import screens.GameScreen;

public class MainMenu extends Application {

    private Stage primaryStage;

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
        playButton.setOnAction(event -> showGameScreen());
        // storeButton.setOnAction(event -> showStoreScreen());
        // settingsButton.setOnAction(event -> showSettingsScreen());

        // Layout for the main menu
        VBox mainMenuLayout = new VBox(10);
        mainMenuLayout.getChildren().addAll(playButton, storeButton, settingsButton);
        Scene scene = new Scene(mainMenuLayout, 512, 512);


        // Set the initial scene to the main menu
        primaryStage.setScene(scene);
        primaryStage.setTitle("Shooting Star [Main Menu][alpha]");
        primaryStage.show();
    }

    // Method to switch to the GameScreen
    private void showGameScreen() {
    	GameScreen gameScreen = new GameScreen();
    	primaryStage.setTitle("Shooting Star [Game Screen][alpha]");
        primaryStage.setScene(gameScreen.getScene());
    }
 
}
