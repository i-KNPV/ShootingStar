package screens;

import application.Settings;
import application.Sound;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.animation.KeyFrame;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;


public class MainMenu {
	// Image resources for the main menu
	private static final Image logo = new Image("assets/buttons/logo.png", 450, 450, false, true);
	private static final Image play = new Image("assets/buttons/play.png");
	private static final Image tutorial = new Image("assets/buttons/help.png");
	private static final Image credits = new Image("assets/buttons/credits.png");
	private static final Image quit = new Image("assets/buttons/quit.png");
	private static final Image musicOn = new Image("assets/buttons/bg.png");
	private static final Image musicOff = new Image("assets/buttons/bgmute.png");
	private static final Image sfxOn = new Image("assets/buttons/sfx.png");
	private static final Image sfxOff = new Image("assets/buttons/sfxmute.png");
	private static final Image backgroundImage = new Image("assets/background/spacebg.gif");
	private static final Image loadingImage = new Image("assets/background/loading.png");
	
	private Settings settings;
	private AnimationTimer animation;
	private ImageView view_bg;
	private ImageView view_musicToggle;
	private ImageView view_sfxToggle;
	private Button musicToggleButton;
	private Button sfxToggleButton;
    private Stage primaryStage;
    private Scene scene;
 	private Sound sound;
    private Text highScoreText;
    private Text highVitalityText;
    private StackPane mainMenuLayout;
    private Pane starContainer;
    private double highScore;
    private int highVitality;

    public MainMenu(Stage primaryStage, double highScore, int highVitality, boolean hasDied, Settings settings) {
    	this.primaryStage = primaryStage;
        this.highScore = highScore;
        this.highVitality = highVitality;
        this.settings = settings;
       
        // Play the main menu music
        this.sound = new Sound();
        playMusic();
        if (settings.isMusicMuted()) muteMusic();
        
        // Load and setup the images
        view_bg = new ImageView(backgroundImage); 
        view_bg.setRotate(90);
        view_bg.setPreserveRatio(true);
        
        view_musicToggle = new ImageView(settings.isMusicMuted() ? musicOff : musicOn);
        view_sfxToggle = new ImageView(settings.isSfxMuted() ? sfxOff : sfxOn);
        
        // Put the volume images to buttons
        musicToggleButton = new Button();
        sfxToggleButton = new Button();
        musicToggleButton.setGraphic(view_musicToggle);
        sfxToggleButton.setGraphic(view_sfxToggle);
        view_musicToggle.setFitWidth(50);  
        view_musicToggle.setFitHeight(50);
        view_sfxToggle.setFitWidth(50);    
        view_sfxToggle.setFitHeight(50);   
        view_musicToggle.setOpacity(70);
        view_sfxToggle.setOpacity(70);
        view_musicToggle.setPreserveRatio(true);
        view_sfxToggle.setPreserveRatio(true);
        
        // Make the buttons invincible
        musicToggleButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        sfxToggleButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        
        // Event handlers for when the mute buttons are clicked
        musicToggleButton.setOnAction(event -> toggleMusic());
        sfxToggleButton.setOnAction(event -> toggleSFX());
        
        // Put the mute buttons to a VBox
        VBox soundControlLayout = new VBox(-10); 
        soundControlLayout.setPadding(new Insets(20, 20, 20, 20));
        soundControlLayout.getChildren().addAll(musicToggleButton, sfxToggleButton);
        soundControlLayout.setAlignment(Pos.BOTTOM_LEFT);
        soundControlLayout.setOpacity(0);
        soundControlLayout.setMaxSize(VBox.USE_PREF_SIZE, VBox.USE_PREF_SIZE);
        soundControlLayout.setPickOnBounds(false);
        
        // Initialize the containers
        starContainer = new Pane();
        mainMenuLayout = new StackPane();
        mainMenuLayout.getChildren().add(view_bg);
        mainMenuLayout.getChildren().add(starContainer);
        
        // Load the main buttons
        this.scene = new Scene(mainMenuLayout, 600, 800);
        ImageView view_logo = new ImageView(logo);
        ImageView view_play = new ImageView(play);
        ImageView view_tutorial = new ImageView(tutorial);
        ImageView view_credits = new ImageView(credits);
        ImageView view_quit = new ImageView(quit);
        
        // Resize the images
        view_play.setFitWidth(200);
        view_tutorial.setFitWidth(200);
        view_credits.setFitWidth(200);
        view_quit.setFitWidth(200);
        
        view_play.setPreserveRatio(true);
        view_tutorial.setPreserveRatio(true);
        view_credits.setPreserveRatio(true);
        view_quit.setPreserveRatio(true);
        
        // Create buttons for the main menu
        Button playButton = new Button();  
        Button tutorialButton = new Button();
        Button creditsButton = new Button();
        Button quitButton = new Button();
        
        // Make the main buttons invincible
        playButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        tutorialButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        creditsButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        quitButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        
        // Put the images on the buttons
        playButton.setGraphic(view_play);
        tutorialButton.setGraphic(view_tutorial);
        creditsButton.setGraphic(view_credits);
        quitButton.setGraphic(view_quit);

        // Set action handlers for the buttons 
        playButton.setOnAction(event -> {
            System.out.println("Play button clicked"); // Debug print
            showGameScreen();
        });

        tutorialButton.setOnAction(event -> {
            System.out.println("Tutorial button clicked"); // Debug print
            hideChildren();
            showTutorial();
        });
        
        creditsButton.setOnAction(event -> {
            System.out.println("Credits button clicked"); // Debug print
            hideChildren();
            showCredits();
        });
  
        quitButton.setOnAction(event -> quitApplication());
        
        // Layout for the main menu
        StackPane logoLayout = new StackPane(view_logo);
        VBox buttonsLayout = new VBox(-10);
        buttonsLayout.setAlignment(Pos.CENTER);
        buttonsLayout.getChildren().addAll(playButton, tutorialButton, creditsButton, quitButton);
        buttonsLayout.setTranslateY(230);
        view_logo.setTranslateY(-140); 

        mainMenuLayout.getChildren().addAll(logoLayout, buttonsLayout, soundControlLayout);
        StackPane.setAlignment(soundControlLayout, Pos.BOTTOM_LEFT);
        
        // UI for the high score
        highScoreText = createHighScoreTimeText();
        double highScoreTextY = (scene.getHeight() / 2) + 60 - (scene.getHeight() / 2);
        
        highVitalityText = createHighScoreVitalityText();
        double highVitalityTextY = (scene.getHeight() / 2) + 80 - (scene.getHeight() / 2);
        highScoreText.setTranslateY(highScoreTextY);
        highVitalityText.setTranslateY(highVitalityTextY);
        
        // White flash effect
        Rectangle whiteFlash = new Rectangle(scene.getWidth(), scene.getHeight(), Color.WHITE);
        mainMenuLayout.getChildren().add(whiteFlash);

        if (hasDied) {
        	mainMenuLayout.getChildren().addAll(highScoreText, highVitalityText);
        }
        
        // Fade out transition for white flash
        FadeTransition fadeOutFlash = new FadeTransition(Duration.seconds(3), whiteFlash);
        fadeOutFlash.setFromValue(1.0);
        fadeOutFlash.setToValue(0.0);
        fadeOutFlash.setOnFinished(event -> {
            mainMenuLayout.getChildren().remove(whiteFlash); // Remove the rectangle after fade out
        });

        // Fade in transitions
        FadeTransition fadeInLogo = new FadeTransition(Duration.seconds(2), view_logo);
        fadeInLogo.setFromValue(0.0);
        fadeInLogo.setToValue(1.0);
      
        FadeTransition fadeInButtons = new FadeTransition(Duration.seconds(1), buttonsLayout);
        fadeInButtons.setFromValue(0.0);
        fadeInButtons.setToValue(1.0);
        
        FadeTransition fadeInHighScoreText = new FadeTransition(Duration.seconds(.25), highScoreText);
        fadeInHighScoreText.setFromValue(0.0);
        fadeInHighScoreText.setToValue(1.0);

        FadeTransition fadeInHighVitalityText = new FadeTransition(Duration.seconds(.25), highVitalityText);
        fadeInHighVitalityText.setFromValue(0.0);
        fadeInHighVitalityText.setToValue(1.0);
        
        FadeTransition fadeInSettings = new FadeTransition(Duration.seconds(.25), soundControlLayout);
        fadeInSettings.setFromValue(0.0);
        fadeInSettings.setToValue(1.0);

        // Sequential transition to ensure order
        SequentialTransition sequentialTransition = new SequentialTransition();
        sequentialTransition.getChildren().addAll(fadeOutFlash, fadeInLogo, fadeInButtons, fadeInHighScoreText, fadeInHighVitalityText, fadeInSettings);
        sequentialTransition.play();
        
        TranslateTransition upDownAnimation = new TranslateTransition(Duration.seconds(1.5), view_logo);
        upDownAnimation.setFromY(-140); // Starting Y position
        upDownAnimation.setToY(-140 - 10); // Move up and down by 10 pixels
        upDownAnimation.setAutoReverse(true); // Automatically reverse the direction
        upDownAnimation.setCycleCount(TranslateTransition.INDEFINITE); // Repeat indefinitely
        upDownAnimation.play();

        initializeStarAnimation();
    }
    
    // Get the scene
    public Scene getScene() {
        return scene;
    }
    
    // Set up the play screen
    private void showGameScreen() {
        System.out.println("Switching to the Game Screen");
        ImageView loading = new ImageView(loadingImage);
        mainMenuLayout.getChildren().add(loading);
        
        // Create a concurrent thread to load the play screen
        Task<Void> gameScreenTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                GameScreen gameScreen = new GameScreen(primaryStage, highScore, highVitality, settings);
                
                // Call this once the play screen has finished loading
                Platform.runLater(() -> {
                    stopMusic();
                    animation.stop();
                    primaryStage.setScene(gameScreen.getScene());
                });
                return null;
            }
        };

        new Thread(gameScreenTask).start();
    }
   
    // Hide the main menu
    private void hideChildren() {
        for (Node child : mainMenuLayout.getChildren()) {
            if (child != view_bg) {
                child.setVisible(false);
            }
        }
    }

    // Show the main menu
    public void showChildren() {
        for (Node child : mainMenuLayout.getChildren()) {
            if (child != view_bg) {
                child.setVisible(true);
            }
        }
    }

    // Show tutorial page
    private void showTutorial() {
    	System.out.println("Switching to the Tutorial Screen");
    	Tutorial tutorial = new Tutorial(primaryStage, this);
    	primaryStage.setTitle("Shooting Star [Game Screen] [alpha]");
        primaryStage.setScene(tutorial.getScene());
    }
    
    // Show developers page
    private void showCredits() {
        System.out.println("Switching to the Credits Screen");
        Credits credits = new Credits(primaryStage, this);
        primaryStage.setTitle("Shooting Star [Credits] [alpha]");
        primaryStage.setScene(credits.getScene());
    }
    
    // Exit the application
    private void quitApplication() {
        System.out.println("Quitting the application");
        System.exit(0);
    }
    
    // Play the music
    private void playMusic() {
    	sound.setFile(6);
    	sound.play();
    	sound.loop(6);
    }
    
    // Stop the music
    private void stopMusic() {
    	sound.stop();
    }
    
    // Mute the music
    private void muteMusic() {
    	sound.setVolume(0);
    }
    
    // Unmute the music
    private void unmuteMusic() {
    	sound.setVolume(1);
    }
    
    // Setup the animation for the shooting stars
    private void initializeStarAnimation() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> spawnStar()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
   
    // Spawn a shooting star in the background
    private void spawnStar() {
        ImageView star = new ImageView(new Image("assets/sprites/star.png"));
        double startX, startY;

        // Randomly decide whether to start from the top or the right boundary
        if (Math.random() < 0.5) {
            startX = Math.random() * scene.getWidth();
            startY = -30; // Start slightly above the visible area
        } else {
            startX = scene.getWidth() + 30; // Start slightly to the right of the visible area
            startY = Math.random() * scene.getHeight();
        }

        star.setX(startX);
        star.setY(startY);
        star.setFitWidth(30);
        star.setFitHeight(30);
        star.setPreserveRatio(true);

        starContainer.getChildren().add(star); // Add star to the container

        animation = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Update the star's position
                star.setX(star.getX() - 2); // Move left
                star.setY(star.getY() + 1); // Move down

                // Remove the star if it moves out of the view
                if (star.getX() < -50 || star.getY() > scene.getHeight() + 50) {
                    starContainer.getChildren().remove(star);
                    this.stop(); // Stop this AnimationTimer
                }
            }
        };
        
        animation.start();
    }
    
    // Methods for the high score texts
    
    private Text createHighScoreTimeText() {
    	Text text = new Text();
    	String scoreToTime = convertSecondToTimeFormat(highScore);
    	text.setText("Time Survived: " + scoreToTime);
    	text.setFont(loadCustomFont("/assets/fonts/TitanOne-Regular.ttf", 15));
    	text.setFill(Color.WHITE);  
    	
    	return text;
    }
    
    private Text createHighScoreVitalityText() {
    	Text text = new Text();
    	text.setText("Highest Vitality Attained: " + highVitality);
    	text.setFont(loadCustomFont("/assets/fonts/TitanOne-Regular.ttf", 15));
    	text.setFill(Color.WHITE); 
    	
    	return text;
    }

  
    private Font loadCustomFont(String fontPath, double size) {
		try {
			Font customFont = Font.loadFont(getClass().getResourceAsStream(fontPath), size);
			if (customFont == null) {
				throw new Exception("Font file not found: " + fontPath);
			}
			return customFont;
		} catch (Exception e) {
			e.printStackTrace();
			// Fallback to default
			return Font.font("Arial", size);
		}
	}
    
    private static String convertSecondToTimeFormat(double score) {
    	int minutes = (int) score / 60;
        int seconds = (int) score % 60;
        int milliseconds = (int) ((score - (int)score) * 1000);
    	
    	return String.format("%02d:%02d.%02d", minutes, seconds, milliseconds);
    }
    
    // Methods for toggling the music and SFX mute
    
    private void toggleMusic() {
    	settings.setMusicMuted(!settings.isMusicMuted());
        view_musicToggle.setImage(settings.isMusicMuted() ? musicOff : musicOn);
        if (settings.isMusicMuted()) {
        	System.out.println("Music muted.");
            muteMusic();
        } else {
        	System.out.println("Music volume back to 100");
            unmuteMusic();
        }
    }

    private void toggleSFX() {
    	settings.setSfxMuted(!settings.isSfxMuted());
        view_sfxToggle.setImage(settings.isSfxMuted() ? sfxOff : sfxOn);
        if (settings.isSfxMuted()) {
        	System.out.println("SFX muted.");
        } else {
        	System.out.println("SFX volume back to 100");
        }
    }

}
