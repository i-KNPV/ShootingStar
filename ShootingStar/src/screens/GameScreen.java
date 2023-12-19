package screens;

import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;

import application.Sound;
import player.Inventory;
import player.Star;
import enemies.Enemy;
import enemies.Laser;
import enemies.Rocket;
import enemies.Bird;
import items.Boost;
import items.Item;
import items.Shimmer;

public class GameScreen {
	private Stage primaryStage;
	private Group root;
    private Star star;
    private Inventory inventory;
    private Sound sound;
    private Sound bgmusic;
    private Sound noise;
    private Rectangle whiteCover;
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
    private double laserInterval = 1.0;
    private double outOfBoundsTimer = 5.0;
    private double generalTimer = 0.0;
    private double highScore;
    private int globalHighVitality;
    private int localHighVitality = 100;
    private boolean hasLaser = false;
    private boolean abort = false;
    private boolean countdownRunning = false;
    
    private Text vitalityText;
    private int enemySpawnCount = 0;
    private final int MAX_ENEMY_PHASE1 = 100;
    private final long NANOSECONDS_PER_SECOND = 500_000_000;
    private long lastSpawnTime = 0;
    private final long spawnInterval = NANOSECONDS_PER_SECOND; 
    
    private long lastBoostSpawnTime = 0;
    private final long BOOST_SPAWN_INTERVAL = 50_000_000_000L; // 50 seconds in nanoseconds
    
    private long lastShimmerSpawnTime = 0;
    private final long SHIMMER_SPAWN_INTERVAL = 20_000_000_000L;
    
    public GameScreen(Stage primaryStage, double highScore, int highVitality) {
		this.primaryStage = primaryStage;
		this.highScore = highScore;
		this.globalHighVitality = highVitality;
		
		bgmusic = new Sound();
		sound = new Sound();
		noise = new Sound();
		playMusic(1);
		
		noise.setFile(8);
		noise.play();
		noise.loop(8);
		
		root = new Group();
		Scene scene = new Scene(root, 600, 800, Color.WHITE);
		
		Image background = new Image("assets/background/main.gif");
		ImageView view_bg = new ImageView(background);
		view_bg.setFitHeight(800);
        view_bg.setPreserveRatio(true);
        root.getChildren().add(view_bg);
        
        this.lastBoostSpawnTime = System.nanoTime();
		
		star = new Star(scene.getWidth(), scene.getHeight());	
		this.inventory = star.viewInventory();
		root.getChildren().add(star.getObject());
		root.getChildren().add(star.getStarImage());
		
		gameOverText = createGameOverText();
		root.getChildren().add(gameOverText);
		gameOverText.setVisible(false);
		
		timerBox = createTimerBox(); 
		messageText = createMessageText();
		timerText = createTimerText();	
		countdownText = createCountdownText();
		countdownText.setLayoutX(((scene.getWidth() - countdownText.getLayoutBounds().getWidth()) / 2) - 20);
		countdownText.setLayoutY((scene.getHeight() / 2) - countdownText.getLayoutBounds().getHeight());
		positionInventoryImage();
        
        generalTimerText = createGeneralTimerText();
		generalTimerText.setVisible(true);
		
		vitalityText = createVitalityText();
		vitalityText.setLayoutX((scene.getWidth() - vitalityText.getLayoutBounds().getWidth()) / 2);
		vitalityText.setLayoutY(timerBox.getLayoutY() + timerBox.getHeight() + 20); 
		
		damageText = createDamageText();
		double damageTextX = vitalityText.getLayoutX() + vitalityText.getLayoutBounds().getWidth() + 10; // 10 is a small gap
		damageText.setLayoutX(damageTextX);
		damageText.setLayoutY(vitalityText.getLayoutY());

	    root.getChildren().addAll(countdownText, timerBox, generalTimerText, vitalityText, damageText);
		scene.setOnKeyPressed(event -> star.handleKeyPress(event.getCode()));
		scene.setOnKeyReleased(event -> star.handleKeyRelease(event.getCode()));
		
		whiteCover = new Rectangle(0, 0, scene.getWidth(), scene.getHeight());
        whiteCover.setFill(Color.WHITE);
        whiteCover.setOpacity(0);
        whiteCover.setVisible(false);
        root.getChildren().add(whiteCover);
		
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
            	
            	if (currentTime - lastShimmerSpawnTime >= SHIMMER_SPAWN_INTERVAL && !abort) {
            	    spawnShimmer();
            	    lastShimmerSpawnTime = currentTime;
            	}
            	
            	if (currentTime - lastBoostSpawnTime >= BOOST_SPAWN_INTERVAL && !abort) {
                    spawnBoost();
                    lastBoostSpawnTime = currentTime;
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
        	        if (item instanceof Shimmer && star.isCollidedWith((Shimmer) item)) {
        	  
        	        	playSoundEffect(4);
        	            
        	        	star.setVitality(((Shimmer) item).hasCollided(star), getScreen());
        	        	root.getChildren().remove(item.getImage()); 
        	        	root.getChildren().remove(item.getObject()); 
        	            itemsToRemove.add(item);
        	        }
        	        
        	        if (item instanceof Boost && star.isCollidedWith((Boost) item)) {
        	        	  
        	        	playSoundEffect(10);
        	            
        	        	star.viewInventory().addBoost();
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

                    if (item instanceof Boost) {
                        ((Boost) item).updatePosition();
                    }
                }
            	
            	vitalityText.setText("Vitality: " + star.getVitality());
                damageText.setText(star.getDamageText());
                
                if (!star.getDamageText().isEmpty()) {
                    new Thread(() -> {
                        try {
                            Thread.sleep(500); // Show the damage text for 1 second
                            Platform.runLater(() -> star.clearDamageText());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
    
                star.updateMovement();
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
	
	public Group getRoot() {
		return root;
	}
	
	private void spawnEnemy() {
		if (generalTimer > 10.0) {
			if (!hasLaser && laserInterval < 0) {
				triggerLaser();
				
				// Reset laser interval
				laserInterval = 25.0;
				hasLaser = true;
			}

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
	
	private void spawnBoost() {
	    Boost boost = new Boost(root.getScene().getWidth(), root.getScene().getHeight(), this);
	    root.getChildren().add(boost.getImage());
	    Item.addItem(boost);
	}
	
	private void handleCollisions() { 
		if (star.getVitality() < 1){
			stopGame();
		}
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
	
	private void stopGame() {
        if (!abort) {
        	stopMusic();
        	noise.stop();
        	
        	generalTimerText.setVisible(false);
            timerBox.setVisible(false);
            vitalityText.setVisible(false);
            inventory.getImage().setVisible(false);

            star.stopMovement();
            star.setGameActive(false);
            abort = true;

            for (Enemy enemy : Enemy.getEnemies()) {
                enemy.initiateSlowdown(); // Initiate slowdown for each enemy
            }

            for (Item item : Item.getItems()) {
                if (item instanceof Shimmer) {
                    ((Shimmer) item).initiateSlowdown(); // Initiate slowdown for each Shimmer
                    ((Shimmer) item).stopSoundEffect();
                }
                
                if (item instanceof Boost) {
                    ((Boost) item).initiateSlowdown(); // Initiate slowdown for each Boost on screen
                }
            }
            
            playSoundEffect(3);
            
            whiteCover.setVisible(true);
            whiteCover.toFront();
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), whiteCover);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.setOnFinished(event -> showGameOverScreen());
            fadeTransition.play();

        }
    }
	
	private void positionInventoryImage() {
        ImageView inventoryImage = inventory.getImage();

        double timerBoxX = timerBox.getLayoutX();
        double timerBoxY = timerBox.getLayoutY();
        double timerBoxWidth = timerBox.getWidth();
        double timerBoxHeight = timerBox.getHeight();

        // Position the inventory image beside the timer box
        inventoryImage.setLayoutX(timerBoxX + timerBoxWidth + 10); // 10 is a small gap
        inventoryImage.setLayoutY(timerBoxY + (timerBoxHeight - inventoryImage.getFitHeight()) / 2); // Align vertically with timer box

        root.getChildren().add(inventoryImage); // Add inventory image to the root node
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
	
	public void triggerLaser() {
		Laser laser = new Laser(root.getScene().getWidth(), root.getScene().getHeight(), this); // root is your root container
        laser.activate(star); // player is your Star instance
	}
	
	public double getGeneralTimer() {
		return generalTimer;
	}
	
	public double getHighScore() {
		return highScore;
	}
	
	public int getHighVitality() {
		return globalHighVitality;
	}
	
	public void setHasLaser(boolean hasLaserCheck) {
		hasLaser = hasLaserCheck;
	}
	
	private void handleTimer() {
		if (!abort) {
			generalTimer += 0.016;
			if (generalTimer > 20) {
				laserInterval -= 0.016;
			}
		}
		else {
			generalTimerText.setFont(loadCustomFont("/assets/fonts/TitanOne-Regular.ttf", 30));
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
        text.setFont(loadCustomFont("/assets/fonts/TitanOne-Regular.ttf", 18));
        text.setFill(Color.RED);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutX(root.getScene().getWidth() / 2 - 200);
        text.setLayoutY(50);
        text.setVisible(false);
        return text;
    }

    private Text createTimerText() {
        Text text = new Text("Return in 5 seconds");
        text.setFont(loadCustomFont("/assets/fonts/TitanOne-Regular.ttf", 16));
        text.setFill(Color.RED);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutX(root.getScene().getWidth() / 2 - 100);
        text.setLayoutY(100);
        text.setVisible(false);
        return text;
    }
    
	private Text createGameOverText() {
		Text text = new Text();
		text.setFont(loadCustomFont("/assets/fonts/TitanOne-Regular.ttf", 24));
        text.setFill(Color.RED);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutX(root.getScene().getWidth() / 2 - 200);
        text.setLayoutY(-200);
        return text;
	}
    
    private Text createGeneralTimerText() {
        Text text = new Text();
        text.setFont(loadCustomFont("/assets/fonts/TitanOne-Regular.ttf", 30));
        text.setFill(Color.BLACK);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutY(80);
        text.setVisible(true);
        text.toFront();
        text.setLayoutX(((root.getScene().getWidth() - text.getLayoutBounds().getWidth()) / 2) - 8);
        return text;
    }
    
    private Text createCountdownText() {
    	Text text = new Text();
    	text.setFont(loadCustomFont("/assets/fonts/TitanOne-Regular.ttf", 48));
    	text.setFill(Color.BLACK);
    	text.setTextAlignment(TextAlignment.CENTER);
        return text;
    }
   
    private Text createVitalityText() {
    	Text text = new Text();
    	text = new Text("Vitality: 100");
    	text.setFont(loadCustomFont("/assets/fonts/TitanOne-Regular.ttf", 18));
    	text.setFill(Color.BLACK);  	
    	return text;
    }
    
    private Text createDamageText() {
    	Text text = new Text();
        text.setFont(loadCustomFont("/assets/fonts/TitanOne-Regular.ttf", 18));
        text.setFill(Color.RED);
        text.setVisible(true); // Initially set to visible or invisible, as per your requirement
        return text;
    }
    
    private Rectangle createTimerBox() {
    	Rectangle timerBox = new Rectangle(200,50);
    	timerBox.setArcWidth(20); // Rounded corners
        timerBox.setArcHeight(20);
        timerBox.setFill(Color.WHITE);
        timerBox.setStroke(Color.BLACK);
        timerBox.setVisible(true);
        timerBox.setWidth(100); 
	    timerBox.setHeight(50); 
	    timerBox.setLayoutX((root.getScene().getWidth() - timerBox.getWidth()) / 2);
	    timerBox.setLayoutY(45); 
	    return timerBox;
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
    	
    	gameLoopTimer.stop();
    	enemySpawnCount = 0;
    	generalTimer = 0.0;
    	abort = false;
    	lastShimmerSpawnTime = 0;
    	
    	root.getChildren().clear();
    	ArrayList<Enemy> enemiesToRemove = new ArrayList<>();
    	ArrayList<Item> itemsToRemove = new ArrayList<>();
    	for (Enemy enemy: Enemy.getEnemies()) enemy.reset();
    	for (Item item: Item.getItems()) item.reset();
    	
    	Item.getItems().removeAll(itemsToRemove);
    	Enemy.getEnemies().removeAll(enemiesToRemove);
    	Enemy.clearEnemies();
    	Item.clearItems();

    }
    
    public GameScreen getScreen() {
    	return this;
    }
    
    public int getLocalHighVitality() {
		return localHighVitality;
	}
    
	public void setLocalHighVitality(int vitality) {
		localHighVitality = vitality;
	}
	
	public int getGlobalHighVitality() {
		return globalHighVitality;
	}
    
}
