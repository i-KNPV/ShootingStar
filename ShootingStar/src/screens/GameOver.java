package screens;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import application.Settings;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameOver{
    private Scene scene;
    private final static Image BGSPRITE = new Image("assets/background/game_over_splash.png");
    private ImageView image;
    private GameScreen screen;
    private Stage primaryStage;
    private Text timeScoreText;
    private Text vitalityScoreText;
    private Settings settings;
    
    private boolean newHighScore = false;
    private Text newHighScoreText;
    private double highScore;
    private double score;
    
    private boolean newHighVitality = false;
    private Text newHighVitalityText;
    private int globalHighVitality;
    private int localHighVitality;

    public GameOver(double width, double height, Stage primaryStage, GameScreen screen, Settings settings) {
    	// Initialize the game over screen with UI elements
    	this.screen = screen;
        this.settings = settings;
    	
    	Group root = new Group();
        Image tryAgain = new Image("assets/buttons/retry.png");
        Image returnToMainMenu = new Image("assets/buttons/return.png");
        
        ImageView view_tryAgain = new ImageView(tryAgain);
        ImageView view_returnToMainMenu = new ImageView(returnToMainMenu);
        image = new ImageView(BGSPRITE);
        
        view_tryAgain.setFitWidth(200);
        view_returnToMainMenu.setFitWidth(200);
        
        view_tryAgain.setPreserveRatio(true);
        view_returnToMainMenu.setPreserveRatio(true);
        
        Button tryAgainButton = new Button();  
        Button returnToMainMenuButton = new Button();
        
        tryAgainButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        returnToMainMenuButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        
        tryAgainButton.setGraphic(view_tryAgain);
        returnToMainMenuButton.setGraphic(view_returnToMainMenu);
        
        tryAgainButton.setOnAction(event -> resetGame());
        returnToMainMenuButton.setOnAction(event -> showMainMenu());
        
        VBox buttonsLayout = new VBox(-10);
        buttonsLayout.setAlignment(Pos.CENTER);
        buttonsLayout.getChildren().addAll(tryAgainButton, returnToMainMenuButton);
        buttonsLayout.setLayoutX((width - view_tryAgain.getFitWidth()) / 2);
        buttonsLayout.setTranslateY(height / 2 + 200);
        
        
        scene = new Scene(root, width, height, Color.BLACK);
        this.primaryStage = primaryStage;
        
        // Show the results based on the current session scores and previous high scores
        score = screen.getGeneralTimer();
        highScore = screen.getHighScore();
        highScore = compareScores(score, highScore);
        
        localHighVitality = screen.getLocalHighVitality();
        globalHighVitality = screen.getGlobalHighVitality();
        globalHighVitality = compareScores(localHighVitality, globalHighVitality);
        
        timeScoreText = createHighScoreTimeText();
        timeScoreText.setLayoutX((scene.getWidth() - timeScoreText.getLayoutBounds().getWidth()) / 2);
        timeScoreText.setLayoutY(height / 2 + 70);
        
        vitalityScoreText = createHighScoreVitalityText();
        vitalityScoreText.setLayoutX((scene.getWidth() - vitalityScoreText.getLayoutBounds().getWidth()) / 2);
        vitalityScoreText.setLayoutY(height / 2 + 100); 
     
        root.getChildren().addAll(image, timeScoreText, vitalityScoreText, buttonsLayout);
        
        newHighScoreText = createNewRecordText();
        newHighVitalityText = createNewRecordText();
        
        positionNewRecordText(newHighScoreText, timeScoreText);
        positionNewRecordText(newHighVitalityText, vitalityScoreText);

        // Add animations if new records are set
        if (newHighScore) {
            animateNewRecordText(newHighScoreText);
            root.getChildren().add(newHighScoreText);
        }
        if (newHighVitality) {
            animateNewRecordText(newHighVitalityText);
            root.getChildren().add(newHighVitalityText);
        }
        
        // Create a rectangle to cover the entire scene for the fade-in effect
        Rectangle fadeRectangle = new Rectangle(width, height);
        fadeRectangle.setFill(Color.WHITE);
        fadeRectangle.setOpacity(1); // Start fully opaque

        // Add the fadeRectangle to the root
        root.getChildren().add(fadeRectangle);

        // Set up the fade-in transition
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(3), fadeRectangle);
        fadeIn.setFromValue(1);
        fadeIn.setToValue(0);
        fadeIn.setOnFinished(event -> root.getChildren().remove(fadeRectangle)); // Remove rectangle after transition

        // Start the fade-in transition
        fadeIn.play();
    }

    // Getter for the scene
    public Scene getScene() {
        return scene;
    }
    
    // Methods for comparing scores and vitality
    
    private double compareScores(double score, double high) {
    	if (high < score) {
    		newHighScore = true;
    		return score;
    	}
    	else return high;
    }
    
    private int compareScores(int score, int high) {
    	if (high < score) {
    		newHighVitality = true;
    		return score;
    	}
    	else return high;
    }
    
    // Reset the previous game session to make way for a new one
    private void resetGame() {
    	System.out.println( "Restarting..." );
    	
    	screen.resetGame();
    	primaryStage.close();
        GameScreen gameScreen = new GameScreen(primaryStage, highScore, globalHighVitality, settings);
        primaryStage.setScene(gameScreen.getScene());
        primaryStage.show();
        
        System.out.println(screen.getGeneralTimer());
        System.out.println(gameScreen.getGeneralTimer());
    }
    
    // Reset the previous session to go back to main menu
    private void showMainMenu() {
    	System.out.println( "Going back to Main Menu..." );
    	screen.resetGame();
    	
    	primaryStage.close();
    	MainMenu mainMenu = new MainMenu(primaryStage, highScore, globalHighVitality, true, settings);
    	primaryStage.setScene(mainMenu.getScene());
    	primaryStage.show();
    }
    
    // Methods to create text elements for displaying scores
    
    private Text createHighScoreTimeText() {
    	Text text = new Text();
    	String scoreToTime = convertSecondToTimeFormat(highScore);
    	text.setText("Time: " + scoreToTime);
    	text.setFont(loadCustomFont("/assets/fonts/TitanOne-Regular.ttf", 18));
    	
    	if (newHighScore) text.setFill(Color.GOLD);
    	else text.setFill(Color.WHITE);  
    	
    	return text;
    }
    
    private Text createHighScoreVitalityText() {
    	Text text = new Text();
    	text.setText("Shine: " + localHighVitality);
    	text.setFont(loadCustomFont("/assets/fonts/TitanOne-Regular.ttf", 18));
    	
    	if (newHighVitality) text.setFill(Color.GOLD);
    	else text.setFill(Color.WHITE); 
    	
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
    
    // Create text for new beaten records
    private Text createNewRecordText() {
        Text text = new Text("NEW");
        text.setFont(loadCustomFont("/assets/fonts/TitanOne-Regular.ttf", 18));
        text.setFill(Color.WHITE);
        return text;
    }
    
    // Position the new record text
    private void positionNewRecordText(Text recordText, Text scoreText) {
        recordText.setLayoutX(scoreText.getLayoutX() - recordText.getLayoutBounds().getWidth() - 10);
        recordText.setLayoutY(scoreText.getLayoutY());
    }
    
    // Animate the new record text with rainbow effect
    private void animateNewRecordText(Text recordText) {
        // Rainbow color animation
        Timeline colorAnimation = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(recordText.fillProperty(), Color.RED)),
            new KeyFrame(Duration.seconds(1), new KeyValue(recordText.fillProperty(), Color.BLUE)),
            new KeyFrame(Duration.seconds(2), new KeyValue(recordText.fillProperty(), Color.GREEN)),
            new KeyFrame(Duration.seconds(3), new KeyValue(recordText.fillProperty(), Color.YELLOW))
        );
        colorAnimation.setCycleCount(Animation.INDEFINITE);
        colorAnimation.setAutoReverse(true);
        colorAnimation.play();

        // Pulsing effect
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), recordText);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.setCycleCount(Animation.INDEFINITE);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
    }
}
