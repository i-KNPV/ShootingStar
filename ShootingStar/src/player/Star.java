package player;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

import enemies.Enemy;
import items.Shimmer;

public class Star {

    private double objectX;
    public double objectY;
    private Circle object;
    private Movement movement;
    private int vitality;
    private double sceneWidth;
    private double sceneHeight;
    private boolean collided;
    private boolean isInvincible = false;
    private boolean gameActive = true;
    private double invincibilityTime = 0;
    private double vitalityDecrementTimer = 0;
    private String damageText = "";
    private static final double INVINCIBILITY_DURATION = 1.0;
    private static final double VITALITY_DECREMENT_VALUE = 1.0;
    private static final double OBJECT_RADIUS = 20.0;
    private Set<KeyCode> pressedKeys = new HashSet<>();   
    
    public Star(double sceneWidth, double sceneHeight) {
    	this.objectX = sceneWidth / 2;
    	this.objectY = sceneHeight / 2;
    	this.object = new Circle(objectX, objectY, OBJECT_RADIUS, Color.YELLOW);
    	this.movement = new Movement();
    	this.sceneWidth = sceneWidth;
    	this.sceneHeight = sceneHeight;
    	this.collided = false;
    	this.vitality = 100;
    }

    public Circle getObject() {
    	return object;
    }
  
    
    public void handleKeyPress(KeyCode keyCode) {
    	pressedKeys.add(keyCode);
        updateMovement();
    }
    
    public void handleKeyRelease(KeyCode keyCode) {
    	pressedKeys.remove(keyCode);
        updateMovement();
    }
    
    private void updateMovement() {
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
    
    
    public void handleCollisions(Enemy enemy) {
    	if (!isInvincible && enemy.isCollidedWithStar(this)) {
    		int damage = enemy.getDamage();
    		vitality -= damage;
    		isInvincible = true; // Set invincibility
            invincibilityTime = 0; // Reset invincibility timer
            damageText = "-" + damage;
    	}
    		
    	if (vitality < 0) {
    		collided = true;
            enemy.hasCollided(true);
            stopMovement(); // Stop Star's movement upon collision
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
	
	public boolean isCollidedWithShimmer(Shimmer shimmer) {
	    Circle shimmerObject = shimmer.getObject();
	    return shimmerObject.getBoundsInParent().intersects(this.object.getBoundsInParent());
	}

    
    public void updatePosition() {
    	if (!collided) {
	    	objectX += movement.getVelocityX();
	    	objectY += movement.getVelocityY() + Movement.WIND_FORCE;
	
	    	// Boundary checking
	    	double minX = OBJECT_RADIUS;
	        double minY = -100; // Allows the star to go beyond the top of the window
	        double maxX = sceneWidth - OBJECT_RADIUS;
	        double maxY = sceneHeight - OBJECT_RADIUS;
	
	        if (objectX < minX) objectX = minX;
	        if (objectY < minY) objectY = minY;
	        if (objectX > maxX) objectX = maxX;
	        if (objectY > maxY) objectY = maxY;
	        
	        // Update position of the object
	        object.setCenterX(objectX);
	    	object.setCenterY(objectY);
    	} else {
    		movement.stopVertical();
    		movement.stopHorizontal();
    	}
    	
    	if (gameActive) {
    		if (isInvincible) {
                invincibilityTime += 0.016; // Assuming 60 FPS, increase time by ~1/60th of a second
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
}
