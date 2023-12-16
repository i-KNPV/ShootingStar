package screens;

import application.Sound;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;


public class MainMenu {
	private Sound sound;
    private Stage primaryStage;
    private Scene scene;
    private double highScore;

    public MainMenu(Stage primaryStage, double highScore) {
        this.primaryStage = primaryStage;
        this.highScore = highScore;
        
        this.sound = new Sound();
        playMusic();
        
        // Load the images
        Image backgroundImage = new Image("assets/background/spacebg.gif");
        ImageView view_bg = new ImageView(backgroundImage); 
        view_bg.setRotate(90);
        view_bg.setPreserveRatio(true);
        
        Image logo = new Image("assets/buttons/logo.png", 500, 500, false, true);
        Image play = new Image("assets/buttons/play.png");
        Image tutorial = new Image("assets/buttons/tutorial.png");
        Image credits =  new Image("assets/buttons/credits.png");
       
        ImageView view_logo = new ImageView(logo);
        ImageView view_play = new ImageView(play);
        ImageView view_tutorial = new ImageView(tutorial);
        ImageView view_credits = new ImageView(credits);
        
        // Resize the images
        view_play.setFitWidth(200);
        view_tutorial.setFitWidth(200);
        view_credits.setFitWidth(200);

        view_play.setPreserveRatio(true);
        view_tutorial.setPreserveRatio(true);
        view_credits.setPreserveRatio(true);
        
        // Create buttons for the main menu
        Button playButton = new Button();  
        Button tutorialButton = new Button();
        Button creditsButton = new Button();
        
        playButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        tutorialButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        creditsButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
 
        playButton.setGraphic(view_play);
        tutorialButton.setGraphic(view_tutorial);
        creditsButton.setGraphic(view_credits);

        // Set action handlers for the buttons 
        playButton.setOnAction(event -> {
            System.out.println("Play button clicked"); // Debug print
            showGameScreen();
        });

        // storeButton.setOnAction(event -> showStoreScreen());
        // settingsButton.setOnAction(event -> showSettingsScreen());

        // Layout for the main menu
        StackPane logoLayout = new StackPane();
        logoLayout.getChildren().add(view_logo);
        
        VBox buttonsLayout = new VBox(-10);
        buttonsLayout.setAlignment(Pos.CENTER);
        buttonsLayout.getChildren().addAll(playButton, tutorialButton, creditsButton);
        
        StackPane mainMenuLayout = new StackPane();
        
        this.scene = new Scene(mainMenuLayout, 600, 800);
        mainMenuLayout.getChildren().addAll(view_bg, logoLayout, buttonsLayout);
        
        // White flash effect
        Rectangle whiteFlash = new Rectangle(scene.getWidth(), scene.getHeight(), Color.WHITE);
        mainMenuLayout.getChildren().add(whiteFlash);
        
        
        view_logo.setTranslateY(-200); 
        buttonsLayout.setTranslateY(200);

        // Fade out transition for white flash
        FadeTransition fadeOutFlash = new FadeTransition(Duration.seconds(3), whiteFlash);
        fadeOutFlash.setFromValue(1.0);
        fadeOutFlash.setToValue(0.0);
        fadeOutFlash.setOnFinished(event -> {
            mainMenuLayout.getChildren().remove(whiteFlash); // Remove the rectangle after fade out
        });

        // Fade in transition for logo
        FadeTransition fadeInLogo = new FadeTransition(Duration.seconds(2), view_logo);
        fadeInLogo.setFromValue(0.0);
        fadeInLogo.setToValue(1.0);

        // Fade in transition for buttons
        FadeTransition fadeInButtons = new FadeTransition(Duration.seconds(1), buttonsLayout);
        fadeInButtons.setFromValue(0.0);
        fadeInButtons.setToValue(1.0);


        // Sequential transition to ensure order
        SequentialTransition sequentialTransition = new SequentialTransition();
        sequentialTransition.getChildren().addAll(fadeOutFlash, fadeInLogo, fadeInButtons);
        sequentialTransition.play();
      
    }
    
    public Scene getScene() {
        return scene;
    }

    private void showGameScreen() {
    	System.out.println("Switching to the Game Screen");
    	stopMusic();
    	GameScreen gameScreen = new GameScreen(primaryStage, highScore);
    	primaryStage.setTitle("Shooting Star [Game Screen] [alpha]");
        primaryStage.setScene(gameScreen.getScene());
    }
    
    private void playMusic() {
    	sound.setFile(6);
    	sound.play();
    	sound.loop(6);
    }
    
    private void stopMusic() {
    	sound.stop();
    }
}
