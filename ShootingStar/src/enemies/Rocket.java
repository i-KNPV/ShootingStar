package enemies;

import screens.GameScreen;
import javafx.scene.paint.Color;

public class Rocket extends Enemy{
	private double SPEED = 8.0;
	private static final int DAMAGE = 50;
	private double originalSpeed;
	
	public Rocket(double sceneWidth, double sceneHeight, GameScreen scene) {
		super(sceneWidth, sceneHeight, scene);
		this.getObject().setFill(Color.RED);
	}
	
	@Override
	protected double getSpeed() {
		return SPEED;
	}
	
	@Override
	public int getDamage() {
		return DAMAGE;
	}
	
	@Override
	public void updatePosition() {
        if (!collided) {
            

            objectY -= getSpeed(); // Use dynamic speed
            object.setCenterX(objectX);
            object.setCenterY(objectY);
        } else {
        	if (slowingDown) {
                double frameDuration = 0.016; // Assuming 60 FPS
                slowdownTimer -= frameDuration;

                if (slowdownTimer > 0) {
                    double slowdownRate = originalSpeed / SLOWDOWN_DURATION * frameDuration;
                    SPEED = Math.max(SPEED - slowdownRate, 0);
                } else {
                    SPEED = 0;
                    slowingDown = false;
                }
                
                objectY -= SPEED; // Use dynamic speed
                object.setCenterX(objectX);
                object.setCenterY(objectY);
            }
        }
    }
	
	public void initiateSlowdown() {
        if (!slowingDown) {
            slowingDown = true;
            slowdownTimer = SLOWDOWN_DURATION;
            originalSpeed = getSpeed(); // Capture the current speed of the sprite
        }
    }
	
	@Override
	public void reset() {
    	super.reset();
    	SPEED = 8.0;
    }	
}
