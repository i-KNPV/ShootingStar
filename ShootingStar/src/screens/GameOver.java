package screens;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import enemies.Enemy;
import items.Item;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameOver{
    private Scene scene;
    private final static Image BGSPRITE = new Image("assets/background/game_over_splash.png");
    private ImageView image;
    private GameScreen screen;
    private Stage primaryStage;
    private double highScore;
    private double score;
    private int globalHighVitality;
    private int localHighVitality;

    public GameOver(double width, double height, Stage primaryStage, GameScreen screen) {
        this.screen = screen;
    	
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
      
        
        root.getChildren().addAll(image, buttonsLayout);
        
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
        
        scene = new Scene(root, width, height, Color.BLACK);
        this.primaryStage = primaryStage;
        
        score = screen.getGeneralTimer();
        highScore = screen.getHighScore();
        highScore = compareScores(highScore, score);
        
        localHighVitality = screen.getLocalHighVitality();
        globalHighVitality = screen.getGlobalHighVitality();
        globalHighVitality = compareScores(localHighVitality, globalHighVitality);
        
        System.out.println(Item.getItems());
        System.out.println(Enemy.getEnemies());
        System.out.println(highScore);
        System.out.println(globalHighVitality);
    }

    public Scene getScene() {
        return scene;
    }
    
    private double compareScores(double score, double high) {
    	if (high < score) return score;
    	else return high;
    }
    
    private int compareScores(int score, int high) {
    	if (high < score) return score;
    	else return high;
    }
    
    private void resetGame() {
    	System.out.println( "Restarting!" );
    	
    	screen.resetGame();
    	primaryStage.close();
        GameScreen gameScreen = new GameScreen(primaryStage, highScore, globalHighVitality);
        primaryStage.setScene(gameScreen.getScene());
        primaryStage.show();
        
        System.out.println(screen.getGeneralTimer());
        System.out.println(gameScreen.getGeneralTimer());
    }
    
    private void showMainMenu() {
    	MainMenu mainMenu = new MainMenu(primaryStage, highScore, globalHighVitality);
    	primaryStage.setScene(mainMenu.getScene());
    }
    
}
