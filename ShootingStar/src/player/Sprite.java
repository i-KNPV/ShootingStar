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
    protected double objectX;
    protected double objectY;
    protected final double sceneWidth;
    protected final double sceneHeight;
    protected boolean collided;
    protected GameScreen scene;
    protected int damage;
    
    public boolean slowingDown;
    public double slowdownTimer = 0;
    private double originalSpeed;
    public static final double SLOWDOWN_DURATION = 2.0;


    protected static final double ENEMY_RADIUS = 20.0;
    protected double SPEED;
    public static List<Enemy> enemies = new ArrayList<>();

    public Sprite(double sceneWidth, double sceneHeight, GameScreen scene) {
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.scene = scene;
        this.objectX = generateRandomX();
        this.objectY = sceneHeight + ENEMY_RADIUS;
        this.object = new Circle(objectX, objectY, ENEMY_RADIUS, Color.RED);
        this.collided = false;
        this.slowingDown = false;
    }

    public Circle getObject() {
        return object;
    }
    
    protected double getSpeed() {
    	return SPEED;
    }
    
    protected double generateRandomX() {
		Random random = new Random();
		return random.nextDouble() * sceneWidth;
	}
    
    public void updatePosition() {
        if (!collided) {
           
            objectY -= SPEED; 
            object.setCenterX(objectX);
            object.setCenterY(objectY);
        } else {
        	if (slowingDown) {
                double frameDuration = 0.016; 
                slowdownTimer -= frameDuration;

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
    
    public void initiateSlowdown() {
        if (!slowingDown) {
            slowingDown = true;
            slowdownTimer = SLOWDOWN_DURATION;
            originalSpeed = getSpeed();
        }
    }
   
}
