package screens;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import player.Star;
import enemies.Rocket;

public class GameScreen {
	private Group root;
    private Star star;
    private Text messageText;
    private Text timerText;
    private Text gameOverText;
    private double outOfBoundsTimer = 5.0;
    private Rocket rocket;
	
	public GameScreen() {
		root = new Group();
		Scene scene = new Scene(root, 600, 800, Color.WHITE);
		
		star = new Star(scene.getWidth(), scene.getHeight());		
		root.getChildren().add(star.getObject());
		
		rocket = new Rocket(scene.getWidth(), scene.getHeight());
		root.getChildren().add(rocket.getObject());
		
		gameOverText = createGameOverText();
		root.getChildren().add(gameOverText);
		gameOverText.setVisible(false);
		
		messageText = createMessageText();
		timerText = createTimerText();
	
		scene.setOnKeyPressed(event -> star.handleKeyPress(event.getCode()));
		scene.setOnKeyReleased(event -> star.handleKeyRelease(event.getCode()));
		
		AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
            	rocket.updatePosition();
                star.updatePosition();
                handleCollisions();
                rocket.updateSurvivalTime(Duration.seconds(rocket.getSurvivalTime().toSeconds()));
                handleOutOfBoundsMessages();
            }
        };
        timer.start(); // Start the timer to continuously update the star's position
	}
	
	public Scene getScene() {
		return root.getScene();
	}
	
	private void handleCollisions() { 
		if (rocket.isCollidedWithStar(star)){
			stopGame();
		}
	}
	
	private void stopGame() {
		star.stopMovement();
		
		gameOverText.setText("Game Over! You survived for: " + (int) rocket.getSurvivalTime().toSeconds() + " seconds");
		gameOverText.setVisible(true);
	}
	
	private Text createGameOverText() {
		Text text = new Text();
        text.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        text.setFill(Color.RED);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutX(root.getScene().getWidth() / 2 - 200);
        text.setLayoutY(50);
        return text;
	}
	
    private void handleOutOfBoundsMessages() {
        if (star.isOutOfBounds()) {
            if (!root.getChildren().contains(messageText)) {
                root.getChildren().addAll(messageText, timerText);
            }
            outOfBoundsTimer -= 0.016; // Update timer (60 FPS)

            timerText.setText(String.format("Return in %.0f seconds", outOfBoundsTimer));
            timerText.setVisible(true);
            
            if (outOfBoundsTimer <= 0) {
                messageText.setText("Out of bounds! Return to play area immediately");
                messageText.setVisible(true);
                timerText.setVisible(false);
                outOfBoundsTimer = 5.0; // Reset the timer
            }
        } else {
            root.getChildren().removeAll(messageText, timerText);
            outOfBoundsTimer = 5.0;
        }
    }

    private Text createMessageText() {
        Text text = new Text("Out of bounds! Return to play area immediately!");
        text.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        text.setFill(Color.RED);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutX(root.getScene().getWidth() / 2 - 200);
        text.setLayoutY(50);
        text.setVisible(false);
        return text;
    }

    private Text createTimerText() {
        Text text = new Text("Return in 5 seconds");
        text.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        text.setFill(Color.RED);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutX(root.getScene().getWidth() / 2 - 100);
        text.setLayoutY(100);
        text.setVisible(false);
        return text;
    }
}
