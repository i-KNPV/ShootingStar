package screens;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameOver{
    private Scene scene;
    private GameScreen screen;
    private Stage primaryStage;
    private Text gameOverText;
    private double highScore;
    private double score;

    public GameOver(double width, double height, Stage primaryStage, GameScreen screen) {
        this.screen = screen;
    	
    	Group root = new Group();
        Image tryAgain = new Image("assets/buttons/retry.png");
        Image returnToMainMenu = new Image("assets/buttons/return.png");
        
        ImageView view_tryAgain = new ImageView(tryAgain);
        ImageView view_returnToMainMenu = new ImageView(returnToMainMenu);
        
        view_tryAgain.setFitWidth(100);
        view_returnToMainMenu.setFitWidth(100);
        
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
        buttonsLayout.setTranslateY(height / 2 + 100);
  
        gameOverText = new Text("Game Over");
        gameOverText.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        gameOverText.setFill(Color.RED);
        gameOverText.setTextAlignment(TextAlignment.CENTER);
        gameOverText.setLayoutX(width / 2 - gameOverText.getLayoutBounds().getWidth() / 2);
        gameOverText.setLayoutY(height / 2);
        root.getChildren().addAll(gameOverText, buttonsLayout);
        scene = new Scene(root, width, height, Color.BLACK);
        this.primaryStage = primaryStage;
        
        score = screen.getGeneralTimer();
        highScore = screen.getHighScore();
        highScore = compareScores(highScore, score);
        
        System.out.println(highScore);
    }

    public Scene getScene() {
        return scene;
    }
    
    private double compareScores(double score, double high) {
    	if (high < score) return score;
    	else return high;
    }
    
    private void resetGame() {
    	screen.resetGame();
    	
        GameScreen gameScreen = new GameScreen(primaryStage, highScore);
        primaryStage.setScene(gameScreen.getScene());
    }
    
    private void showMainMenu() {
    	MainMenu mainMenu = new MainMenu(primaryStage, highScore);
    	primaryStage.setScene(mainMenu.getScene());
    }
    
}
