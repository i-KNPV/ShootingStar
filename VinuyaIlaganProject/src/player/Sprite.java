package player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import enemies.Enemy;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import screens.GameScreen;

public class Sprite {
    protected Circle object;
    protected GameScreen scene;
    protected int damage;
    protected double objectX;
    protected double objectY;
    protected final double sceneWidth;
    protected final double sceneHeight;
    protected boolean collided;
    public boolean slowingDown;
    public double slowdownTimer = 0;
    private double originalSpeed;
    protected double SPEED;
    public static List<Enemy> enemies = new ArrayList<>(); // List of enemy sprites
    public static final double SLOWDOWN_DURATION = 2.0;
    protected static final double ENEMY_RADIUS = 20.0;

    public Sprite(double sceneWidth, double sceneHeight, GameScreen scene) {
        // Set up this sprite
    	this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.scene = scene;
        this.objectX = generateRandomX();
        this.objectY = sceneHeight + ENEMY_RADIUS;
        this.object = new Circle(objectX, objectY, ENEMY_RADIUS, Color.RED);
        this.collided = false;
        this.slowingDown = false;
    }
    
    
    // Get the Circle object
    public Circle getObject() {
        return object;
    }
    
    // Get the current speed
    protected double getSpeed() {
    	return SPEED;
    }
    
    // Generate a random X-coordinate within the scene
    protected double generateRandomX() {
		Random random = new Random();
		return random.nextDouble() * sceneWidth;
	}
    
    // Update the sprite's position
    public void updatePosition() {
        if (!collided) {
        
            objectY -= SPEED; 
            object.setCenterX(objectX);
            object.setCenterY(objectY);
        } else {
        	if (slowingDown) { // Check if sprite is slowing down
                double frameDuration = 0.016; 
                slowdownTimer -= frameDuration;
                
                // Slow down rate calculation
                if (slowdownTimer > 0) {
                    double slowdownRate = originalSpeed / SLOWDOWN_DURATION * frameDuration;
                    SPEED = Math.max(SPEED - slowdownRate, 0);
                } else {
                    SPEED = 0;
                    slowingDown = false;
                }
                
                objectY -= SPEED; 
                object.setCenterX(objectX);
                object.setCenterY(objectY);
            }
        }
    }
    
    // Initiate the slow down process
    public void initiateSlowdown() {
        if (!slowingDown) {
            slowingDown = true;
            slowdownTimer = SLOWDOWN_DURATION;
            originalSpeed = getSpeed();
        }
    }
   
}
