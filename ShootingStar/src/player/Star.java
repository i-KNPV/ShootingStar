package player;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import java.util.HashSet;

import application.Sound;
import enemies.Enemy;
import items.Item;
import items.Shimmer;

public class Star {
	private Sound soundEffect;
	private Sound lowHealthSound;
	private Sound constantTwinkle;
	public Inventory inventory;
    private double objectX;
    public double objectY;
    private ImageView starImage;
    private Circle object;
    private Movement movement;
    private int vitality;
    private double sceneWidth;
    private double sceneHeight;
    private boolean collided;
    private boolean isInvincible = false;
    private boolean gameActive = true;
    private boolean isLowHealthSoundPlaying = false;
    private double invincibilityTime = 0;
    private double vitalityDecrementTimer = 0;
    private String damageText = "";
    private static final double INVINCIBILITY_DURATION = 1.0;
    private static final double VITALITY_DECREMENT_VALUE = 1.0;
    private static final double OBJECT_RADIUS = 13.0;
    private HashSet<KeyCode> pressedKeys = new HashSet<>();
 
    
    private static final Image NORMAL = new Image("assets/sprites/star.png");
    private static final Image HURT = new Image("assets/sprites/star_hurt.png");
    
    public Star(double sceneWidth, double sceneHeight) {
    	inventory = new Inventory();
        inventory.clearInventory();
    	
    	soundEffect = new Sound();
    	lowHealthSound = new Sound();
    	constantTwinkle = new Sound();
    	
    	constantTwinkle.setFile(5);
    	constantTwinkle.setVolume(0.85f);
    	constantTwinkle.play();
    	
    	this.objectX = sceneWidth / 2;
    	this.objectY = sceneHeight / 2;
    	this.object = new Circle(objectX, objectY, OBJECT_RADIUS, Color.YELLOW);
    	this.object.setVisible(false);
    	
    	this.movement = new Movement();
    	this.sceneWidth = sceneWidth;
    	this.sceneHeight = sceneHeight;
    	this.collided = false;
    	this.vitality = 100;
    	
    	starImage = new ImageView(NORMAL);
        starImage.setFitWidth(OBJECT_RADIUS * 4.5);
        starImage.setFitHeight(OBJECT_RADIUS * 4.5);
        starImage.setPreserveRatio(true);
        starImage.setVisible(true);
        updateImagePosition();
    }

    public Circle getObject() {
    	return object;
    }
    
    public void handleKeyPress(KeyCode keyCode) {
    	pressedKeys.add(keyCode);
    	if (keyCode == KeyCode.SPACE && inventory.getInventory() == 1) {
            inventory.applyBoost(movement);
            inventory.clearInventory();
        }
    }
    
    public void handleKeyRelease(KeyCode keyCode) {
    	pressedKeys.remove(keyCode);
    	if (keyCode == KeyCode.SPACE) {
            inventory.resetBoost(movement);    
        }
    }
    
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
    
    private void updateImagePosition() {
    	starImage.setLayoutX(objectX - OBJECT_RADIUS); 
        starImage.setLayoutY(objectY - OBJECT_RADIUS);
    }
    
    public void handleCollisions(Enemy enemy) {
    	if (!isInvincible && enemy.isCollidedWithStar(this)) {
    		int damage = enemy.getDamage();
    		
    		soundEffect.setFile(0);
    		soundEffect.play();
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
    	
    		
    	if (vitality < 1) {
    		constantTwinkle.stop();
    		collided = true;
    		starImage.setVisible(false);
            enemy.hasCollided(true);
            stopMovement();
            vitality = 0;
    	}
	}
    
    public String getDamageText() {
        return damageText;
    }

    public void clearDamageText() {
        damageText = "";
    }
    
	public boolean isCollided() {
		return collided;
	}
	
	public boolean isCollidedWith(Item item) {
	    Circle object = item.getObject();
	    return object.getBoundsInParent().intersects(this.object.getBoundsInParent());
	}

    
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
	    	updateImagePosition();
    	} else {
    		movement.stopVertical();
    		movement.stopHorizontal();
    	}
    	
    	if (gameActive) {
    		if (isInvincible) {
                invincibilityTime += 0.016; // increase time by 1/60th of a second
                if (invincibilityTime >= INVINCIBILITY_DURATION) {
                    isInvincible = false;
                    invincibilityTime = 0;
                }
            } else {
            	vitalityDecrementTimer += 0.016;
            	if (vitalityDecrementTimer >= VITALITY_DECREMENT_VALUE) {
            		vitality--;
            		vitalityDecrementTimer = 0;
            	}
            }
    	}
    	
    	if (vitality < 40 && !isLowHealthSoundPlaying && vitality > 0) {
    		lowHealthSound.setFile(2);
    		lowHealthSound.play();
    		lowHealthSound.loop(2);
            isLowHealthSoundPlaying = true;
        } else if ((vitality >= 40 || vitality == 0) && isLowHealthSoundPlaying) {
    		lowHealthSound.stop();
            isLowHealthSoundPlaying = false;
        }
    }
    
    public void setGameActive(boolean isActive) {
        this.gameActive = isActive;
    }
    
	public boolean isOutOfBounds() {
		return objectY < 0;
	}
	
	public void stopMovement() {
		movement.stopVertical();
		movement.stopHorizontal();
	}
	
	public int getVitality() {
		return vitality;
	}
	
	public void setVitality(int newValue) {
		vitality = newValue;
	}
	
	public ImageView getStarImage() {
		return starImage;
	}
	
	public Inventory viewInventory() {
		return inventory;
	}
	
	public Movement getMovement() {
		return movement;
	}

	
	public void reset() {
		vitality = 100;
		collided = false;
		gameActive = true;
		isInvincible = false;
		invincibilityTime = 0;
	    vitalityDecrementTimer = 0;
	}
	
	

}
