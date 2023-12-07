package screens;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import player.Star;

import enemies.Enemy;
import enemies.Rocket;
import enemies.Bird;

public class GameScreen {
	private Group root;
    private Star star;
    private Text messageText;
    private Text timerText;
    private Text gameOverText;
    private Text generalTimerText;
    private double outOfBoundsTimer = 5.0;
    private double generalTimer = 0.0;
    private boolean abort = false;
    
    private int enemySpawnCount = 0;
    private final int MAX_ENEMY_COUNT = 6;
    private final long NANOSECONDS_PER_SECOND = 1_000_000_000;
    private long lastSpawnTime = 0;
    private final long spawnInterval = NANOSECONDS_PER_SECOND; // Spawn an enemy every second

	
	public GameScreen() {
		root = new Group();
		Scene scene = new Scene(root, 600, 800, Color.WHITE);
		
		star = new Star(scene.getWidth(), scene.getHeight());		
		root.getChildren().add(star.getObject());
		
		gameOverText = createGameOverText();
		root.getChildren().add(gameOverText);
		gameOverText.setVisible(false);
		
		generalTimerText = createGeneralTimerText();
		root.getChildren().add(generalTimerText);
		generalTimerText.setVisible(true);
		
		messageText = createMessageText();
		timerText = createTimerText();
	
		scene.setOnKeyPressed(event -> star.handleKeyPress(event.getCode()));
		scene.setOnKeyReleased(event -> star.handleKeyRelease(event.getCode()));
		
		AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
            	long currentTime = System.nanoTime();
            	
            	if (currentTime - lastSpawnTime >= spawnInterval && enemySpawnCount < MAX_ENEMY_COUNT) {
                    spawnEnemy();
                    lastSpawnTime = currentTime;
                }
            	
            	for (Enemy enemy: Enemy.getEnemies()) {
            		enemy.updatePosition();
            		star.handleCollisions(enemy);
            	}
                star.updatePosition();
                handleCollisions();
                handleOutOfBoundsMessages();
                handleTimer();
            }
        };
        timer.start(); // Start the timer to continuously update the star's position
	}
	
	public Scene getScene() {
		return root.getScene();
	}
	
	private void spawnEnemy() {
        // Create and add a new rocket to the scene
		Enemy rocket = new Rocket(getScene().getWidth(), getScene().getHeight());
        root.getChildren().add(rocket.getObject());
        Enemy.addEnemy(rocket);
        
        Enemy bird = new Bird(getScene().getWidth(), getScene().getHeight());
        root.getChildren().add(bird.getObject());
        Enemy.addEnemy(bird);
        enemySpawnCount++;
    }
	
	private void handleCollisions() { 
		
		if (star.isCollided()){
			stopGame();
		}
	}
	
	private void stopGame() {
		star.stopMovement();
		abort = true;
		
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
	
	private void handleTimer() {
		if (!abort) generalTimer += 0.016;
		else {
			generalTimerText.setFont(Font.font("Arial", FontWeight.BOLD, 30));
	    	generalTimerText.setTextAlignment(TextAlignment.CENTER);
	    	generalTimerText.setLayoutX((root.getScene().getWidth() - generalTimerText.getLayoutBounds().getWidth()) / 2);
	    	generalTimerText.setLayoutY(root.getScene().getHeight() / 2);
		}
		
		if(!root.getChildren().contains(generalTimerText)) {
			root.getChildren().add(generalTimerText);
		}
		
		generalTimerText.setText(String.format("Time elapsed: %.2f", generalTimer));
		
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
    
    private Text createGeneralTimerText() {
    	Text text = new Text("Time elapsed: ");
    	text.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        text.setFill(Color.BLACK);
        text.setTextAlignment(TextAlignment.RIGHT);
        text.setLayoutX(root.getScene().getWidth() - 50 - text.getLayoutBounds().getWidth());
        text.setLayoutY(30);
        text.setVisible(true);
        text.toFront();
        return text;
    	
    }
}
