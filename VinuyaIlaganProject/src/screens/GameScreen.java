package screens;

import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;

import application.Settings;
import application.Sound;
import player.Inventory;
import player.Star;
import enemies.Enemy;
import enemies.Laser;
import enemies.Rocket;
import enemies.Bird;
import items.*;

public class GameScreen {
	// Preload images for game elements
	private static final Image rocketImage = new Image("assets/sprites/rocket.png");
	private static final Image shimmerImage = new Image("assets/sprites/shimmer.gif");
	private static final Image boostImage = new Image("assets/sprites/boost.png");
	private static final Image shieldImage = new Image("assets/sprites/shield.png");
	private static final Image HUD = new Image("assets/sprites/hud.png");
	private static final Image background = new Image("assets/background/main.gif");
	
	private Settings settings;
	private ImageView view_hud;
	private ImageView shieldImageView;
	private Stage primaryStage;
	private Group root;
    private Star star;
    private Inventory inventory;
    private Sound sound;
    private Sound bgmusic;
    private Sound noise;
    private Text timerText;
    private Text generalTimerText;
    private Text countdownText;
    private Text damageText;
    private Rectangle whiteCover;
    private AnimationTimer gameLoopTimer;
    private Timeline vitalityAnimation;
    private double laserInterval = 1.0;
    private double outOfBoundsTimer = 5.0;
    private double generalTimer = 0.0;
    private double highScore;
    private int globalHighVitality;
    private int localHighVitality = 100;
    private boolean hasLaser = false;
    private boolean abort = false;

    private Text vitalityText;
    private int enemySpawnCount = 0;
    private final int MAX_ENEMY_PHASE1 = 100;
    private final long NANOSECONDS_PER_SECOND = 500_000_000;
    
    private long lastSpawnTime = 0;
    private final long spawnInterval = NANOSECONDS_PER_SECOND; 
    
    private long lastBoostSpawnTime = 0;
    private final long BOOST_SPAWN_INTERVAL = 40_000_000_000L; // 40 seconds in nanoseconds
    
    private long lastShieldSpawnTime = 0;
    private final long SHIELD_SPAWN_INTERVAL = 70_000_000_000L; // 1 minutes and 10 seconds in nanoseconds
    
    private long lastShimmerSpawnTime = 0;
    private final long SHIMMER_SPAWN_INTERVAL = 20_000_000_000L;
    
    public GameScreen(Stage primaryStage, double highScore, int highVitality, Settings settings) {
		// Initialize the game screen with all UI elements
    	this.primaryStage = primaryStage;
		this.highScore = highScore;
		this.globalHighVitality = highVitality;
		this.settings = settings;
		
		bgmusic = new Sound();
		sound = new Sound();
		noise = new Sound();
		
		// Play the wind noise and background music
		
		if(!settings.isMusicMuted())playMusic(1);
		
		if(!settings.isSfxMuted()) {
			noise.setFile(8);
			noise.play();
			noise.loop(8);
		}
		
		root = new Group();
		Scene scene = new Scene(root, 600, 800, Color.WHITE);
		
		// Set up the HUD
		ImageView view_bg = new ImageView(background);
		view_bg.setFitHeight(800);
        view_bg.setPreserveRatio(true);
        root.getChildren().add(view_bg);
        
        view_hud = new ImageView(HUD);
        view_hud.setFitWidth(150);
        view_hud.setPreserveRatio(true);
        view_hud.setLayoutX((root.getScene().getWidth() - view_hud.getFitWidth()) / 2);
        view_hud.setLayoutY(45); 
        
        root.getChildren().add(view_hud);
        
        // Forces the boost to spawn at a later time
        this.lastBoostSpawnTime = System.nanoTime();
        
        // Commented out to force a shield to spawn at the start of each game 
        //this.lastShieldSpawnTime = System.nanoTime();
		
        // Set up the player and inventory
		star = new Star(scene.getWidth(), scene.getHeight(), this, settings);	
		this.inventory = star.viewInventory();
		root.getChildren().add(star.getObject());
		root.getChildren().add(star.getStarImage());
		
		// Initialize text for the out of bounds count down text
		timerText = createTimerText();	
		countdownText = createCountdownText();
		countdownText.setLayoutX(((scene.getWidth() - countdownText.getLayoutBounds().getWidth()) / 2) - 20);
		countdownText.setLayoutY((scene.getHeight() / 2) - countdownText.getLayoutBounds().getHeight());
		positionInventoryImage();
        
		// Initialize text for the HUD
		
        generalTimerText = createGeneralTimerText();
		generalTimerText.setVisible(true);
		
		vitalityText = createVitalityText();
		double centerX = (scene.getWidth() - vitalityText.getLayoutBounds().getWidth()) / 2;
		vitalityText.setLayoutX(centerX);
		vitalityText.setLayoutY(view_hud.getLayoutY() + 60); 
		
		damageText = createDamageText();
		double damageTextX = vitalityText.getLayoutX() + vitalityText.getLayoutBounds().getWidth() + 90; 
		damageText.setLayoutX(damageTextX);
		damageText.setLayoutY(vitalityText.getLayoutY() - 5);
		
	    root.getChildren().addAll(countdownText, generalTimerText, vitalityText, damageText);
		scene.setOnKeyPressed(event -> star.handleKeyPress(event.getCode()));
		scene.setOnKeyReleased(event -> star.handleKeyRelease(event.getCode()));
		
		// Set up flash animation
		whiteCover = new Rectangle(0, 0, scene.getWidth(), scene.getHeight());
        whiteCover.setFill(Color.WHITE);
        whiteCover.setOpacity(0);
        whiteCover.setVisible(false);
        root.getChildren().add(whiteCover);
		
        // Game logic
		gameLoopTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
            	long currentTime = System.nanoTime();
            	
            	// Always put the HUD in front
            	view_hud.toFront();
                vitalityText.toFront();
                generalTimerText.toFront();
                inventory.getImage().toFront();
                inventory.getBoostImage().toFront();
                inventory.getShieldImage().toFront();
            	
                // Conditions for spawning enemies and items
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
            	
            	if (currentTime - lastShieldSpawnTime >= SHIELD_SPAWN_INTERVAL && !abort) {
                    spawnShield();
                    lastShieldSpawnTime = currentTime;
                }
            	
            	// Update the enemies
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
            	
            	// Update the items
            	ArrayList<Item> itemsToRemove = new ArrayList<>();
            	for (Item item : Item.getItems()) {
            		
            		// Conditions for different collisions with 
        	        if (item instanceof Shimmer && star.isCollidedWith((Shimmer) item)) {
        	        	
        	        	if (!settings.isSfxMuted()) playSoundEffect(4);

        	            
        	        	star.setVitality(((Shimmer) item).hasCollided(star), getScreen());
        	        	root.getChildren().remove(item.getImage()); 
        	        	root.getChildren().remove(item.getObject()); 
        	            itemsToRemove.add(item);
        	            System.out.println("Player obtained SHIMMER! Current health: " + star.getVitality());
        	        }
        	        
        	        if (item instanceof Boost && star.isCollidedWith((Boost) item)) {
        	        	  
        	        	if (!settings.isSfxMuted()) playSoundEffect(10);
        	        	
        	        	star.viewInventory().addBoost();
        	        	root.getChildren().remove(item.getImage()); 
        	        	root.getChildren().remove(item.getObject()); 
        	            itemsToRemove.add(item);
        	        }
        	        
        	        if (item instanceof Shield && star.isCollidedWith((Shield) item)) {
      	        	  
        	        	if (!settings.isSfxMuted()) playSoundEffect(10);
        	            
        	        	star.viewInventory().addShield();
        	        	root.getChildren().remove(item.getImage());
        	        	root.getChildren().remove(item.getObject()); 
        	            itemsToRemove.add(item);
        	        }
        	        
        	        if (item.isOutOfBounds()) {
        	        	itemsToRemove.add(item);
        	        }
        	    }
            	
            	// Remove the enemies and items in remove holders
        	    Item.getItems().removeAll(itemsToRemove);
            	Enemy.getEnemies().removeAll(enemiesToRemove);
            	
            	// Update the position of the items
            	for (Item item : Item.getItems()) {
                    if (item instanceof Shimmer) {
                        ((Shimmer) item).updatePosition();
                    }

                    if (item instanceof Boost) {
                        ((Boost) item).updatePosition();
                    }
                    
                    if (item instanceof Shield) {
                    	((Shield) item).updatePosition();
                    }
                }
            	
            	// Update the vitality and damage text
            	vitalityText.setText(Integer.toString(star.getVitality()));
                damageText.setText(star.getDamageText());
                
                // Create a concurrent thread for the damage pop up
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
                
                // If the player is at low health, make the shine blink
                if (star.getVitality() < 40) {
                    startLowHealthAnimation();
                } else if (star.getVitality() >= 40 || star.getVitality() == 0) {
                    stopLowHealthAnimation();
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
	
    // Getter for the scene
	public Scene getScene() {
		return root.getScene();
	}
	
	// Getter for the root node
	public Group getRoot() {
		return root;
	}
	
	// Method to spawn enemies
	private void spawnEnemy() {
		// After 10 seconds, spawn rockets and allow lasers to spawn
		if (generalTimer > 10) {
			if (!hasLaser && laserInterval < 0) {
				triggerLaser();
				
				// Reset laser interval
				laserInterval = 25.0;
				hasLaser = true;
			}
			spawnRocket();
		}
		
		Bird bird = new Bird(getScene().getWidth(), getScene().getHeight(), this);
	    root.getChildren().add(bird.getImage());
	    Enemy.addEnemy(bird);
	    enemySpawnCount++;
    }
	
	// Methods for spawning game elements
	
	private void spawnRocket() {
		 ImageView rocketImageView = new ImageView(rocketImage);
	     Rocket rocket = new Rocket(getScene().getWidth(), getScene().getHeight(), this, rocketImageView); // Pass the preloaded image
	     root.getChildren().add(rocketImageView);
	     Enemy.addEnemy(rocket);
	}

	private void spawnShimmer() {
		ImageView shimmerImageView = new ImageView(shimmerImage);
	    Shimmer shimmer = new Shimmer(getScene().getWidth(), getScene().getHeight(), this, shimmerImageView, settings);
	    root.getChildren().add(shimmerImageView);
	    Item.addItem(shimmer);
	    System.out.println("Item spawned: SHIMMER");
	}
	
	private void spawnBoost() {
		ImageView boostImageView = new ImageView(boostImage);
	    Boost boost = new Boost(root.getScene().getWidth(), root.getScene().getHeight(), this, boostImageView);
	    root.getChildren().add(boost.getImage());
	    Item.addItem(boost);
	    System.out.println("Item spawned: BOOST");
	}
	
	private void spawnShield() {
		shieldImageView = new ImageView(shieldImage);
	    Shield shield = new Shield(root.getScene().getWidth(), root.getScene().getHeight(), this, shieldImageView);
	    root.getChildren().add(shield.getImage());
	    Item.addItem(shield);
	    System.out.println("Item spawned: SHIELD");
	}
	
	// Get the image of the shield
	public ImageView getShieldImage() {
		return shieldImageView;
	}
	
	// Method to stop game once player has reached a shine of 0
	private void handleCollisions() { 
		if (star.getVitality() < 1){
			stopGame();
		}
	}

	// Method for stopping the game
	private void stopGame() {
        if (!abort) {
        	
        	// Mute the music and noise
        	if (!settings.isMusicMuted()) stopMusic();
	        if (!settings.isSfxMuted()) noise.stop();
        	        	
	        // Hide HUD
        	generalTimerText.setVisible(false);
        	view_hud.setVisible(false);
            vitalityText.setVisible(false);
            inventory.getImage().setVisible(false);
            inventory.getBoostImage().setVisible(false);
            inventory.getShieldImage().setVisible(false);
            
            // Bring player to a halt
            star.stopMovement();
            star.setGameActive(false);
            abort = true;
            
            // Inititate slow down animation
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
                
                if (item instanceof Shield) {
                	((Shield) item).initiateSlowdown();
                }
            }
            
            // Play game over sound effect
            if (!settings.isMusicMuted() && !settings.isSfxMuted()) playSoundEffect(3);
            
            // Fade to white
            whiteCover.setVisible(true);
            whiteCover.toFront();
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), whiteCover);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.setOnFinished(event -> showGameOverScreen());
            fadeTransition.play();

        }
    }
	
	// Position the inventory page in HUD
	public void positionInventoryImage() {
	    ImageView inventoryImage = inventory.getImage();
	    ImageView boostImage = inventory.getBoostImage();
	    ImageView shieldImage = inventory.getShieldImage();

	    double hudX = view_hud.getLayoutX();
	    double hudY = view_hud.getLayoutY();

	    // Position the inventory image to the left of the HUD and slightly up
	    inventoryImage.setLayoutX(hudX - inventoryImage.getFitWidth() - 10); // 10 is a small gap
	    inventoryImage.setLayoutY(hudY + 30); // Positioning up by 30
	    inventoryImage.setFitWidth(50);
	    inventoryImage.setPreserveRatio(true);

	    // Position boost and shield images at the same location as the inventory image
	    boostImage.setLayoutX(inventoryImage.getLayoutX());
	    boostImage.setLayoutY(inventoryImage.getLayoutY());
	    boostImage.setFitWidth(50);
	    boostImage.setPreserveRatio(true);

	    shieldImage.setLayoutX(inventoryImage.getLayoutX());
	    shieldImage.setLayoutY(inventoryImage.getLayoutY());
	    shieldImage.setFitWidth(50);
	    shieldImage.setPreserveRatio(true);

	    // Add images to the root node
	    root.getChildren().addAll(inventoryImage, boostImage, shieldImage);

	    // Bring the images to the front
	    inventoryImage.toFront();
	    boostImage.toFront();
	    shieldImage.toFront();
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
	
	// Move over to the game over screen
	private void showGameOverScreen() {
		GameOver gameOverScreen = new GameOver(root.getScene().getWidth(), root.getScene().getHeight(), primaryStage, this, settings);
		primaryStage.setScene(gameOverScreen.getScene());
	}

	// Set the spawn count
	public void setSpawnCount(int value) {
		enemySpawnCount = value;
	}
	
	// Trigger or active a laser
	public void triggerLaser() {
		Laser laser = new Laser(root.getScene().getWidth(), root.getScene().getHeight(), this, settings); // root is your root container
        laser.activate(star); // player is your Star instance
	}
	
	// Getters for different values
	
	public int getSpawnCount() {
		return enemySpawnCount;
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
	
	// Handle the general timer used in showing the time in HUD and spawning
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
	    	generalTimerText.setLayoutY((root.getScene().getHeight() / 2) + 100);
		}
		
		if (!root.getChildren().contains(view_hud)) {
	        root.getChildren().add(view_hud);
	    }
	    if (!root.getChildren().contains(generalTimerText)) {
	        root.getChildren().add(generalTimerText);
	    }
		
	    generalTimerText.setLayoutX(((root.getScene().getWidth() - generalTimerText.getLayoutBounds().getWidth()) / 2) + 10);
	    vitalityText.setLayoutX(((root.getScene().getWidth() - vitalityText.getLayoutBounds().getWidth()) / 2) + 3);
	    generalTimerText.setText(convertSecondToTimeFormat(generalTimer));
	}
	
	// Show an out of bounds warning 
    private void handleOutOfBoundsMessages() {
        if (star.isOutOfBounds()) {
            if (!root.getChildren().contains(timerText)) {
                root.getChildren().add(timerText);
            }
            outOfBoundsTimer -= 0.016;

            timerText.setText(String.format("RETURN TO PLAY AREA IN %n%.0f", outOfBoundsTimer));
            timerText.setVisible(true);
            
            // Kill the player if player stays out of bounds for too long
            if (outOfBoundsTimer <= 0) {
            	star.setVitality(0);
                timerText.setVisible(false);
            }
        } else {
            root.getChildren().remove(timerText);
            outOfBoundsTimer = 5.0;
        }
    }

    // Methods for setting up the different text on screen
    
    private Text createTimerText() {
        Text text = new Text("Return in 5 seconds");
        text.setFont(loadCustomFont("/assets/fonts/TitanOne-Regular.ttf", 40));
        text.setFill(Color.CRIMSON);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutX(((root.getScene().getWidth() - text.getLayoutBounds().getWidth()) / 2) - 50);
        text.setLayoutY(root.getScene().getHeight() / 2);
        text.setVisible(false);
        return text;
    }
    
    private Text createGeneralTimerText() {
        Text text = new Text();
        text.setFont(loadCustomFont("/assets/fonts/TitanOne-Regular.ttf", 18));
        text.setFill(Color.BLACK);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutY(185);
        text.setVisible(true);
        text.toFront();
        text.setLayoutX(((root.getScene().getWidth() - text.getLayoutBounds().getWidth()) / 2) - 15);
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
    	text = new Text("");
    	text.setFont(loadCustomFont("/assets/fonts/TitanOne-Regular.ttf", 48));
    	text.setFill(Color.BLACK);  	
    	return text;
    }
    
    private Text createDamageText() {
    	Text text = new Text();
        text.setFont(loadCustomFont("/assets/fonts/TitanOne-Regular.ttf", 40));
        text.setFill(Color.DARKSALMON);
        text.setVisible(true); // Initially set to visible or invisible, as per your requirement
        return text;
    }
    
    // Methods for dealing with low health animation for shine text
    
    private void startLowHealthAnimation() {
        if (vitalityAnimation == null) {
        	// Rapidly blinks for black to red and back to red again
            vitalityAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(vitalityText.fillProperty(), Color.BLACK)),
                new KeyFrame(Duration.millis(400), new KeyValue(vitalityText.fillProperty(), Color.RED))
            );
            vitalityAnimation.setAutoReverse(true);
            vitalityAnimation.setCycleCount(Timeline.INDEFINITE);
        }
        vitalityAnimation.play();
    }
    
    private void stopLowHealthAnimation() {
        if (vitalityAnimation != null) {
            vitalityAnimation.stop();
            vitalityText.setFill(Color.BLACK); // Reset to default color
        }
    }
    
    // Methods for dealing with music and sound effects
    
    public void playMusic(int i) {
    	bgmusic.setFile(i);
    	bgmusic.setVolume(0.85f);
    	bgmusic.play();
    	bgmusic.loop(i);
    }
    
    public void stopMusic() {
    	bgmusic.stop();
    }
    
    public void playSoundEffect(int i) {
    	sound.setFile(i);
    	sound.play();
    }
     
    // Getter for the current screen instance
    public GameScreen getScreen() {
    	return this;
    }
    
    // Methods for dealing with vitality high scores
    
    public int getLocalHighVitality() {
		return localHighVitality;
	}
    
	public void setLocalHighVitality(int vitality) {
		localHighVitality = vitality;
	}
	
	public int getGlobalHighVitality() {
		return globalHighVitality;
	}
	
	// Utility function to convert double to seconds format
	private static String convertSecondToTimeFormat(double time) {
	    int minutes = (int) time / 60;
	    int seconds = (int) time % 60;
	    int milliseconds = (int) ((time - (int)time) * 1000);
	    
	    return String.format("%02d:%02d.%03d", minutes, seconds, milliseconds);
	}
	
	// Getter for the Star object
	public Star getStar() {
		return star;
	}
	
    // Reset all game states to get ready for garbage collection
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
}
