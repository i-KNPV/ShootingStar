package screens;

import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;

import application.Sound;
import player.Star;
import enemies.Enemy;
import enemies.Rocket;
import enemies.Bird;
import items.Item;
import items.Shimmer;

public class GameScreen {
	private Stage primaryStage;
	private Group root;
    private Star star;
    private Sound sound;
    private Sound bgmusic;
    private Text messageText;
    private Text timerText;
    private Text gameOverText;
    private Text generalTimerText;
    private Text countdownText;
    private Text damageText;
    private Rectangle timerBox;
    private AnimationTimer countdownTimer;
    private AnimationTimer gameLoopTimer;
    private int countdownValue = 5;
    private double outOfBoundsTimer = 5.0;
    private double generalTimer = 0.0;
    private double highScore;
    private boolean abort = false;
    private boolean countdownRunning = false;
    
    private Text vitalityText;
    private int enemySpawnCount = 0;
    private final int MAX_ENEMY_PHASE1 = 100;
    private final long NANOSECONDS_PER_SECOND = 500_000_000;
    private long lastSpawnTime = 0;
    private final long spawnInterval = NANOSECONDS_PER_SECOND; 
    
    private long lastShimmerSpawnTime = 0;
    private final long SHIMMER_SPAWN_INTERVAL = 20_000_000_000L;
    
    public GameScreen(Stage primaryStage, double highScore) {
		this.primaryStage = primaryStage;
		this.highScore = highScore;
		
		bgmusic = new Sound();
		sound = new Sound();
		playMusic(1);
		
		root = new Group();
		Scene scene = new Scene(root, 600, 800, Color.WHITE);
		
		star = new Star(scene.getWidth(), scene.getHeight());		
		root.getChildren().add(star.getObject());
		root.getChildren().add(star.getStarImage());
		
		gameOverText = createGameOverText();
		root.getChildren().add(gameOverText);
		gameOverText.setVisible(false);
		
		messageText = createMessageText();
		timerText = createTimerText();	
		
		countdownText = new Text();
	    countdownText.setFont(Font.font("TTMarxianaW05-Grotesque", FontWeight.BOLD, 48));
	    countdownText.setFill(Color.BLACK);
	    countdownText.setTextAlignment(TextAlignment.CENTER);
	    countdownText.setVisible(false); // Initially invisible
	    root.getChildren().add(countdownText);
	    
	    damageText = new Text();
        damageText.setFont(Font.font("TTMarxianaW05-Grotesque", FontWeight.BOLD, 18));
        damageText.setFill(Color.RED);
        damageText.setLayoutX(100); 
        damageText.setLayoutY(20);
        root.getChildren().add(damageText);
        
        timerBox = new Rectangle(200, 50); 
        timerBox.setArcWidth(20); // Rounded corners
        timerBox.setArcHeight(20);
        timerBox.setFill(Color.WHITE);
        timerBox.setStroke(Color.BLACK);
        timerBox.setVisible(true);
        timerBox.setWidth(100); 
	    timerBox.setHeight(50); 
	    timerBox.setLayoutX((root.getScene().getWidth() - timerBox.getWidth()) / 2);
	    timerBox.setLayoutY(45); 
        root.getChildren().add(timerBox);
        
        generalTimerText = createGeneralTimerText();
		root.getChildren().add(generalTimerText);
		generalTimerText.setVisible(true);
		
		vitalityText = new Text("Vitality: 100");
	    vitalityText.setFont(Font.font("TTMarxianaW05-Grotesque", FontWeight.BOLD, 18));
	    vitalityText.setFill(Color.BLACK);
	    vitalityText.setLayoutX((scene.getWidth() - vitalityText.getLayoutBounds().getWidth()) / 2);
	    vitalityText.setLayoutY(timerBox.getLayoutY() + timerBox.getHeight() + 20); 
	    root.getChildren().add(vitalityText);

	
		scene.setOnKeyPressed(event -> star.handleKeyPress(event.getCode()));
		scene.setOnKeyReleased(event -> star.handleKeyRelease(event.getCode()));
		
		gameLoopTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
            	long currentTime = System.nanoTime();
            	
            	if (currentTime - lastSpawnTime >= spawnInterval 
            			&& enemySpawnCount < MAX_ENEMY_PHASE1
            			&& !abort) {
                    spawnEnemy();
                    lastSpawnTime = currentTime;
                }  
            	
            	if (currentTime - lastShimmerSpawnTime >= SHIMMER_SPAWN_INTERVAL) {
            	    spawnShimmer();
            	    lastShimmerSpawnTime = currentTime;
            	}
            	
            	ArrayList<Enemy> enemiesToRemove = new ArrayList<>();
            	for (Enemy enemy: Enemy.getEnemies()) {
            		enemy.updatePosition();
            		star.handleCollisions(enemy);
            		
            		if (enemy.isOutOfBounds()) {
            			root.getChildren().remove(enemy.getImage());
            			enemiesToRemove.add(enemy);
            			enemySpawnCount--;
            		}
            	}
            	
            	ArrayList<Item> itemsToRemove = new ArrayList<>();
            	for (Item item : Item.getItems()) {
        	        if (item instanceof Shimmer && star.isCollidedWithShimmer((Shimmer) item)) {
        	  
        	        	playSoundEffect(4);
        	            
        	        	star.setVitality(((Shimmer) item).hasCollided(star));
        	        	 root.getChildren().remove(item.getImage()); 
        	        	 root.getChildren().remove(item.getObject()); 
        	            itemsToRemove.add(item);
        	        }
        	        
        	        if (item.isOutOfBounds()) {
        	        	itemsToRemove.add(item);
        	        }
        	    }
            	
        	    Item.getItems().removeAll(itemsToRemove);
            	Enemy.getEnemies().removeAll(enemiesToRemove);
            	
            	for (Item item : Item.getItems()) {
                    if (item instanceof Shimmer) {
                        ((Shimmer) item).updatePosition();
                    }
                }
            	
            	vitalityText.setText("Vitality: " + star.getVitality());
                damageText.setText(star.getDamageText());
                
                if (!star.getDamageText().isEmpty()) {
                    new Thread(() -> {
                        try {
                            Thread.sleep(1000); // Show the damage text for 1 second
                            Platform.runLater(() -> star.clearDamageText());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
    
                
                star.updatePosition();
                handleCollisions();
                handleOutOfBoundsMessages();
                handleTimer();
            }
        };
       gameLoopTimer.start();
	}
	
	public Scene getScene() {
		return root.getScene();
	}
	
	private void spawnEnemy() {
		if (generalTimer > 10.0) {
			Rocket rocket = new Rocket(getScene().getWidth(), getScene().getHeight(), this);
	        root.getChildren().add(rocket.getImage());
	        Enemy.addEnemy(rocket);
		}
		
		Bird bird = new Bird(getScene().getWidth(), getScene().getHeight(), this);
	    root.getChildren().add(bird.getImage());
	    Enemy.addEnemy(bird);
	    enemySpawnCount++;
    }
	
	private void spawnShimmer() {
	    Shimmer shimmer = new Shimmer(getScene().getWidth(), getScene().getHeight(), this);
	    root.getChildren().add(shimmer.getObject());
	    root.getChildren().add(shimmer.getImage());
	    Item.addItem(shimmer);
	}
	
	private void handleCollisions() { 
		System.out.println(star.getVitality());

		if (star.getVitality() < 1){
			stopGame();
		}
	}
	
	private void stopGame() {
        if (!abort) {
        	stopMusic();
        	
            star.stopMovement();
            star.setGameActive(false);
            abort = true;
            showCountdown();

            for (Enemy enemy : Enemy.getEnemies()) {
                enemy.initiateSlowdown(); // Initiate slowdown for each enemy
            }

            for (Item item : Item.getItems()) {
                if (item instanceof Shimmer) {
                    ((Shimmer) item).initiateSlowdown(); // Initiate slowdown for each Shimmer
                    ((Shimmer) item).stopSoundEffect();
                }
            }
            
            playSoundEffect(3);
         
        }
    }
	
	private void showCountdown() {
		if (!countdownRunning) {
			countdownValue = 5; // Reset countdown value
		    countdownText.setText(Integer.toString(countdownValue));
		    countdownText.setX((root.getScene().getWidth() - countdownText.getLayoutBounds().getWidth()) / 2);
		    countdownText.setY((root.getScene().getHeight() / 2)-50);
		    countdownText.setVisible(true); 

	        countdownTimer = new AnimationTimer() {
	            private long lastUpdate = 0;
	            private final long countdownDuration = 1_000_000_000; // 1 second

	            @Override
	            public void handle(long now) {
	                if (now - lastUpdate >= countdownDuration) {
	                    lastUpdate = now;
	                    updateCountdown();
	                }
	            }
	        };
	        countdownTimer.start();
		}
	}
	
	private void updateCountdown() {
	    if (countdownValue > 0) {
	        countdownText.setText(Integer.toString(countdownValue--));
	    } else {
	    	gameLoopTimer.stop();
	        countdownTimer.stop();
	        countdownText.setVisible(false); 
	        countdownRunning = false;
	        showGameOverScreen();
	    }
	}
	
	private void showGameOverScreen() {
		GameOver gameOverScreen = new GameOver(root.getScene().getWidth(), root.getScene().getHeight(), primaryStage, this);
		primaryStage.setScene(gameOverScreen.getScene());
	}

	public int getSpawnCount() {
		return enemySpawnCount;
	}
	
	public void setSpawnCount(int value) {
		enemySpawnCount = value;
	}
	
	public double getGeneralTimer() {
		return generalTimer;
	}
	
	public double getHighScore() {
		return highScore;
	}
	
	private Text createGameOverText() {
		Text text = new Text();
        text.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        text.setFill(Color.RED);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutX(root.getScene().getWidth() / 2 - 200);
        text.setLayoutY(-200);
        return text;
	}
	
	private void handleTimer() {
		if (!abort) generalTimer += 0.016;
		else {
			generalTimerText.setFont(Font.font("Arial", FontWeight.BOLD, 30));
	    	generalTimerText.setTextAlignment(TextAlignment.CENTER);
	    	generalTimerText.setLayoutX((root.getScene().getWidth() - generalTimerText.getLayoutBounds().getWidth()) / 2);
	    	generalTimerText.setLayoutY((root.getScene().getHeight() / 2) + 32);
		    timerBox.setLayoutX((root.getScene().getWidth() - timerBox.getWidth()) / 2);
		    timerBox.setLayoutY(root.getScene().getHeight() / 2); 
		}
		
		if (!root.getChildren().contains(timerBox)) {
	        root.getChildren().add(timerBox);
	    }
	    if (!root.getChildren().contains(generalTimerText)) {
	        root.getChildren().add(generalTimerText);
	    }
		
	    generalTimerText.setLayoutX((root.getScene().getWidth() - generalTimerText.getLayoutBounds().getWidth()) / 2);
		generalTimerText.setText(String.format("%.0f", generalTimer));
		
	}
	
    private void handleOutOfBoundsMessages() {
        if (star.isOutOfBounds()) {
            if (!root.getChildren().contains(messageText)) {
                root.getChildren().addAll(messageText, timerText);
            }
            outOfBoundsTimer -= 0.016;

            timerText.setText(String.format("Return in %.0f seconds", outOfBoundsTimer));
            timerText.setVisible(true);
            
            if (outOfBoundsTimer <= 0) {
                messageText.setText("Out of bounds! Return to play area immediately");
                messageText.setVisible(true);
                timerText.setVisible(false);
                outOfBoundsTimer = 5.0; 
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
        Text text = new Text("");
        text.setFont(Font.font("TTMarxianaW05-Grotesque", FontWeight.BOLD, 30));
        text.setFill(Color.BLACK);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutY(80);
        text.setVisible(true);
        text.toFront();
        text.setLayoutX(((root.getScene().getWidth() - text.getLayoutBounds().getWidth()) / 2) - 8);

        return text;
    }
    
    public void playMusic(int i) {
    	bgmusic.setFile(i);
    	bgmusic.setVolume(0.85f);
    	bgmusic.play();
    }
    
    public void stopMusic() {
    	bgmusic.stop();
    }
    
    public void playSoundEffect(int i) {
    	sound.setFile(i);
    	sound.play();
    }
   
    public void resetGame() {
    	star.reset();
    	
    	root.getChildren().clear();
    	
    	ArrayList<Enemy> enemiesToRemove = new ArrayList<>();
    	ArrayList<Item> itemsToRemove = new ArrayList<>();
    	for (Enemy enemy: Enemy.getEnemies()) enemy.reset();
    	for (Item item: Item.getItems()) item.reset();
    	
    	Item.getItems().removeAll(itemsToRemove);
    	Enemy.getEnemies().removeAll(enemiesToRemove);
    	Enemy.clearEnemies();
    	Item.clearItems();
    	enemySpawnCount = 0;
    	generalTimer = 0.0;
    	abort = false;
    	lastShimmerSpawnTime = 0;
    	
    }
    
}
