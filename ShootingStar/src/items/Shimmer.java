package items;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import player.Star;
import screens.GameScreen;

public class Shimmer extends Item{
	private Circle object;
	protected double objectX;
    protected double objectY;
    private final static double ITEM_RADIUS = 13.0;
    private final static int HEALTH = 40;
    private double SPEED;
    private double originalSpeed;
	
	public Shimmer(double sceneWidth, double sceneHeight, GameScreen scene) {
		super(sceneWidth, sceneHeight, scene);
		this.objectX = generateRandomX();
	    this.objectY = sceneHeight; // Position it at the bottom of the screen
		this.object = new Circle(objectX, objectY, ITEM_RADIUS, Color.PINK);
		this.SPEED = 2.0;
	}
	
	public Circle getObject() {
        return object;
    }
	
	public int hasCollided(Star star) {
	    this.collided = true;
	    return star.getVitality() + HEALTH;
	}
	
	public void updatePosition() {
        if (!collided) {
            if (slowingDown) {
                double frameDuration = 0.016; // Assuming 60 FPS
                slowdownTimer -= frameDuration;

                if (slowdownTimer > 0) {
                    double slowdownRate = originalSpeed / SLOWDOWN_DURATION * frameDuration;
                    SPEED = Math.max(SPEED - slowdownRate, 0);
                } else {
                    SPEED = 0; // Stop completely after slowdown duration
                    slowingDown = false;
                }
            }

            objectY -= SPEED; // Use dynamic speed
            object.setCenterX(objectX);
            object.setCenterY(objectY);
        }
    }
	
	public void initiateSlowdown() {
        if (!slowingDown) {
            slowingDown = true;
            slowdownTimer = SLOWDOWN_DURATION;
            originalSpeed = getSpeed(); // Capture the current speed of the sprite
        }
    }


}
