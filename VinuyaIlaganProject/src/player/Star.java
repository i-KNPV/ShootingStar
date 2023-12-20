package player;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import screens.GameScreen;
import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import java.util.HashSet;
import application.Settings;
import application.Sound;
import enemies.Enemy;
import items.Item;

public class Star {
    private Settings settings;
	private Sound soundEffect;
	private Sound lowHealthSound;
	private Sound constantTwinkle;
	public Inventory inventory;
    private ImageView starImage;
    private ImageView shieldImage;
    private Circle object;
    private GameScreen screen;
    private Movement movement;
    private int vitality; 
    public double objectX;
    public double objectY;
    private double sceneWidth;
    private double sceneHeight;
    private double invincibilityTime = 0;
    private double vitalityDecrementTimer = 0;
    private boolean collided;
    private boolean isInvincible = false;
    private boolean hasShield = false;
    private boolean gameActive = true;
    private boolean isLowHealthSoundPlaying = false;
    private String damageText = "";
    private HashSet<KeyCode> pressedKeys = new HashSet<>();

    private static final double INVINCIBILITY_DURATION = 1.0;
    private static final double VITALITY_DECREMENT_VALUE = 1.0;
    private static final double OBJECT_RADIUS = 13.0;
    private static final Image NORMAL = new Image("assets/sprites/star.png");
    private static final Image HURT = new Image("assets/sprites/star_hurt.png");
    private static final Image DEAD = new Image("assets/sprites/star_death.png");
    private static final Image SHIELD = new Image("assets/sprites/shield.png");
    
    public Star(double sceneWidth, double sceneHeight, GameScreen screen, Settings settings) {
    	this.screen = screen;
    	this.settings = settings;
    	inventory = new Inventory();
        inventory.clearInventory();
        
        // Initialize the sound 
    	soundEffect = new Sound();
    	lowHealthSound = new Sound();
    	constantTwinkle = new Sound();
	    constantTwinkle.setFile(5);
	    constantTwinkle.setVolume(0.85f);
	    if (!settings.isSfxMuted()) {
	    	constantTwinkle.play();
	    	constantTwinkle.loop(5);
        }
    	
	    // Initialize the object of the Star (to be used its hit box)
    	this.objectX = sceneWidth / 2;
    	this.objectY = sceneHeight / 2;
    	this.object = new Circle(objectX, objectY, OBJECT_RADIUS, Color.YELLOW);
    	this.object.setVisible(false);
    	
    	// Initialize the movement / controls of the Star
    	this.movement = new Movement();
    	this.sceneWidth = sceneWidth;
    	this.sceneHeight = sceneHeight;
    	
    	this.collided = false;
    	this.vitality = 100;
    	
    	// Set up the image of the star
    	starImage = new ImageView(NORMAL);
        starImage.setFitWidth(OBJECT_RADIUS * 4.5);
        starImage.setFitHeight(OBJECT_RADIUS * 4.5);
        starImage.setPreserveRatio(true);
        starImage.setVisible(true);
        updateImagePosition(starImage); // Update the image star to be aligned to Circle
        
        // Set up the shield image for the star
        shieldImage = new ImageView(SHIELD);
        shieldImage.setFitWidth(OBJECT_RADIUS * 6);
        shieldImage.setFitHeight(OBJECT_RADIUS * 6);
        shieldImage.setPreserveRatio(true);
        shieldImage.setVisible(false);
        updateImagePosition(shieldImage); // Update the shield image to be aligned to star image
    }
    
    // Return the hit box
    public Circle getObject() {
    	return object;
    }
    
    // Handle key press events
    public void handleKeyPress(KeyCode keyCode) {
    	pressedKeys.add(keyCode);
    	
    	// Activate boost effect if player has it in inventory
    	if (keyCode == KeyCode.SPACE && inventory.getInventory() == inventory.BOOST) {
            inventory.applyBoost(movement);
            inventory.clearInventory();
        }
    	
    	// Activate shield if player has it in inventory
    	if (keyCode == KeyCode.SPACE && inventory.getInventory() == inventory.SHIELD) {
            hasShield = true;
            showActiveShield();
            if (!settings.isSfxMuted()) { 
            	soundEffect.setFile(11);
				soundEffect.play();
				
            } 
            inventory.clearInventory();
        }
    }
    
    // Handle key release events
    public void handleKeyRelease(KeyCode keyCode) {
    	pressedKeys.remove(keyCode);
    	if (keyCode == KeyCode.SPACE) {
            inventory.resetBoost(movement);    
        }
    }
    
    // Update the player's movement depending on key press
    public void updateMovement() {
    	if (pressedKeys.contains(KeyCode.UP)) {
            movement.moveUp();
        } else if (pressedKeys.contains(KeyCode.DOWN)) {
            movement.moveDown();
        } else {
            movement.stopVertical();
        }

        if (pressedKeys.contains(KeyCode.LEFT)) {
            movement.moveLeft();
        } else if (pressedKeys.contains(KeyCode.RIGHT)) {
            movement.moveRight();
        } else {
            movement.stopHorizontal();
        }
    }
    
    // Update the position of the player's image and shield image based on current coordinate
    private void updateImagePosition(ImageView image) {
    	double centerX = object.getCenterX();
        double centerY = object.getCenterY();
        
        if (image.equals(shieldImage)) {
            double offsetX = shieldImage.getFitWidth() / 2;
            double offsetY = shieldImage.getFitHeight() / 2;
            image.setLayoutX(centerX - offsetX);
            image.setLayoutY(centerY - offsetY);
        } else {
            double offsetX = OBJECT_RADIUS * 2.25;
            double offsetY = OBJECT_RADIUS * 2.25;
            image.setLayoutX(centerX - offsetX);
            image.setLayoutY(centerY - offsetY);
        }
    }
    
    // Handle collisions with enemies 
    public void handleCollisions(Enemy enemy) {
    	if (!isInvincible && enemy.isCollidedWithStar(this)) {
    		if (hasShield) { // If player has shield active, negate damage
    			 if (!settings.isSfxMuted()) {
    				 soundEffect.setFile(12);
    				 soundEffect.play();
    			 }
    			hasShield = false;
    			
    			isInvincible = true;
                invincibilityTime = 0; 
                
                if (shieldImage != null) {
                	hideActiveShield();
                }
    		} else { // Damage the player
        		int damage = enemy.getDamage();
        		
        		if (!settings.isSfxMuted()) {
	        		soundEffect.setFile(0);
	        		soundEffect.play();
        		}
        		
        		System.out.println("Player has been hit! Damage inflicted: " + damage);
        		
        		vitality -= damage;
        		isInvincible = true;
                invincibilityTime = 0; 
                damageText = "-" + damage;
                
                // Switch to HURT sprite
                starImage.setImage(HURT);
                
                // Start a pause transition
                PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
                pause.setOnFinished(e -> starImage.setImage(NORMAL)); // Switch back to NORMAL sprite
                pause.play();
    		}
    	}
    	
    	// Set the conditions if player is dead	
    	if (vitality < 1) {
    		constantTwinkle.stop();
    		collided = true;
    		starImage.setImage(DEAD);
            enemy.hasCollided(true);
            stopMovement();
            vitality = 0;
    	}
	}
    
    // Get the damage text
    public String getDamageText() {
        return damageText;
    }
    
    // Clear the damage text
    public void clearDamageText() {
        damageText = "";
    }
    
    // Return if the star has been collided and dead
	public boolean isCollided() {
		return collided;
	}
	
	// Method for checking if item has collided with star's hit box
	public boolean isCollidedWith(Item item) {
	    Circle object = item.getObject();
	    return object.getBoundsInParent().intersects(this.object.getBoundsInParent());
	}
    
	// Update the player's position based on movement
    public void updatePosition() {
    	if (!collided) {
	    	objectX += movement.getVelocityX();
	    	objectY += movement.getVelocityY() + Movement.WIND_FORCE;
	
	    	// Boundary checking
	    	double minX = OBJECT_RADIUS;
	        double minY = -100; 
	        double maxX = sceneWidth - OBJECT_RADIUS;
	        double maxY = sceneHeight - OBJECT_RADIUS;
	
	        if (objectX < minX) objectX = minX;
	        if (objectY < minY) objectY = minY;
	        if (objectX > maxX) objectX = maxX;
	        if (objectY > maxY) objectY = maxY;
	        
	        // Update position of the object
	        object.setCenterX(objectX);
	    	object.setCenterY(objectY);
	    	updateImagePosition(starImage);
	    	
	    	// Update the shield overlay if active
	    	if (hasShield) {
	    		updateImagePosition(shieldImage);
	    	}
	    	
    	} else {
    		// Stop the star to a halt if dead
    		movement.stopVertical();
    		movement.stopHorizontal();
    	}

    	if (gameActive) {
    		// Give player invincibility frames 
    		if (isInvincible) {
                invincibilityTime += 0.016; // increase time by 1/60th of a second
                if (invincibilityTime >= INVINCIBILITY_DURATION) {
                    isInvincible = false;
                    invincibilityTime = 0;
                }
            } else { // Always decrease the player's shine by 1 per seconds
            	vitalityDecrementTimer += 0.016;
            	if (vitalityDecrementTimer >= VITALITY_DECREMENT_VALUE) {
            		vitality--;
            		vitalityDecrementTimer = 0;
            	}
            }
    	}
    	
    	// Play the low health sound alarm
    	if (vitality < 40 && !isLowHealthSoundPlaying && vitality > 0) {
	    	lowHealthSound.setFile(2);
	    	if (!settings.isSfxMuted()) {
	    		lowHealthSound.play();
	    		lowHealthSound.loop(2);
    		}
            isLowHealthSoundPlaying = true;
            System.out.println("Player at critical health! (Shine < 40)");
        } else if ((vitality >= 40 || vitality == 0) && isLowHealthSoundPlaying) {
        	if (!settings.isSfxMuted()) lowHealthSound.stop();
            isLowHealthSoundPlaying = false;
        }
    }
    
    // Show the shield image when the player has a shield active
    private void showActiveShield() {
    	System.out.println("Player applied shield.");
    	shieldImage.setVisible(true);
    	screen.getRoot().getChildren().add(shieldImage);
    }
    
    // Hide the shield image
    private void hideActiveShield() {
    	System.out.println("The shield broke!");
    	shieldImage.setVisible(false);
    	if (shieldImage != null) {
    		screen.getRoot().getChildren().remove(shieldImage);
    	}
    	
    }
    
    // Set gameActive depending on isActive state
    public void setGameActive(boolean isActive) {
        this.gameActive = isActive;
    }
    
    // Check if the user is out of bounding box 
	public boolean isOutOfBounds() {
		return objectY < 0;
	}
	
	// Screech the star to a halt
	public void stopMovement() {
		movement.stopVertical();
		movement.stopHorizontal();
	}
	
	// Get the vitality / shine of the star
	public int getVitality() {
		return vitality;
	}
	
	// Set the vitality / shine of the star (for Shimmer)
	public void setVitality(int newValue, GameScreen screen) {
		if(screen.getLocalHighVitality() < newValue) screen.setLocalHighVitality(newValue);
		vitality = newValue;
	}
	
	// Set the vitality / shine of the star (for General)
	public void setVitality(int newValue) {
		vitality = newValue;
	}
	
	// Return the image of this Star
	public ImageView getStarImage() {
		return starImage;
	}
	
	// Return the image of the player's inventory
	public Inventory viewInventory() {
		return inventory;
	}
	
	// Get the movement 
	public Movement getMovement() {
		return movement;
	}
	
	// Reset the star's state
	public void reset() {
		vitality = 100;
		collided = false;
		gameActive = true;
		isInvincible = false;
		invincibilityTime = 0;
	    vitalityDecrementTimer = 0;
	}
	
	

}
