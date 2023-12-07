package player;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

import enemies.Enemy;


public class Star {

    private double objectX;
    public double objectY;
    private Circle object;
    private Movement movement;
    private double sceneWidth;
    private double sceneHeight;
    private boolean collided;
    
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
    	if (enemy.isCollidedWithStar(this)) {
            collided = true;
            enemy.hasCollided(true);
            stopMovement(); // Stop Star's movement upon collision
    	}
	}
    
	public boolean isCollided() {
		return collided;
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
    }
    
	public boolean isOutOfBounds() {
		return objectY < 0;
	}
	
	public void stopMovement() {
		movement.stopVertical();
		movement.stopHorizontal();
	}
}
