package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
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
        
        // Load the images
        Image backgroundImage = new Image("assets/background/space.png");
        ImageView view_bg = new ImageView(backgroundImage); 
        view_bg.setFitWidth(600);
        view_bg.setPreserveRatio(true);
        
        Image logo = new Image("assets/buttons/logo.png", 500, 500, false, true);
        Image play = new Image("assets/buttons/play.png");
        Image store = new Image("assets/buttons/store.png");
        Image settings =  new Image("assets/buttons/settings.png");
       
        ImageView view_logo = new ImageView(logo);
        ImageView view_play = new ImageView(play);
        ImageView view_store = new ImageView(store);
        ImageView view_settings = new ImageView(settings);
        
        // Resize the images
        view_play.setFitWidth(170);
        view_store.setFitWidth(200);
        view_settings.setFitWidth(220);

        view_play.setPreserveRatio(true);
        view_store.setPreserveRatio(true);
        view_settings.setPreserveRatio(true);
        
        // Create buttons for the main menu
        Button playButton = new Button();  
        Button storeButton = new Button();
        Button settingsButton = new Button();
        
        playButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        storeButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        settingsButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
 
        playButton.setGraphic(view_play);
        storeButton.setGraphic(view_store);
        settingsButton.setGraphic(view_settings);

        // Set action handlers for the buttons 
        playButton.setOnAction(event -> showGameScreen());
        // storeButton.setOnAction(event -> showStoreScreen());
        // settingsButton.setOnAction(event -> showSettingsScreen());

        // Layout for the main menu
        StackPane logoLayout = new StackPane();
        logoLayout.getChildren().add(view_logo);
        
        VBox buttonsLayout = new VBox(-10);
        buttonsLayout.setAlignment(Pos.CENTER);
        buttonsLayout.getChildren().addAll(playButton, storeButton, settingsButton);
        
        StackPane mainMenuLayout = new StackPane();
        mainMenuLayout.getChildren().addAll(view_bg, logoLayout, buttonsLayout);
        Scene scene = new Scene(mainMenuLayout, 600, 800);
        
        // Change coordinates
        view_logo.setTranslateY(-200); 
        buttonsLayout.setTranslateY(50);
        playButton.setTranslateY(10);
        
        // Set the initial scene to the main menu
        primaryStage.setScene(scene);
        primaryStage.setTitle("Shooting Star [Main Menu][alpha]");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // Method to switch to the GameScreen
    private void showGameScreen() {
    	GameScreen gameScreen = new GameScreen();
    	primaryStage.setTitle("Shooting Star [Game Screen][alpha]");
        primaryStage.setScene(gameScreen.getScene());
    }
 
}
