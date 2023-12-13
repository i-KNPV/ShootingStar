package enemies;
import screens.GameScreen;
import javafx.scene.paint.Color;
import java.util.concurrent.TimeUnit;

public class Bird extends Enemy{
	private double SPEED = 2.0;
	private static final double UPPER_WAIT = 1.0;
	private static final double LOWER_WAIT = 0.25;
	private static final int DAMAGE = 25;
	private double originalSpeed;
	
	public Bird(double sceneWidth, double sceneHeight, GameScreen scene) {
		super(sceneWidth, sceneHeight, scene);
		this.getObject().setFill(Color.ORANGE);
	}
	@Override
	protected double getSpeed() {
        if (!slowingDown) {
            SPEED = calculateSpeed();
        }
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
	
	private double calculateSpeed() {
		double timer = scene.getGeneralTimer();
        double speedIncreaseRate = 0.033; // Adjust this rate according to your preference
        
        // Speed increases linearly with the generalTimer, capped at 5 after 60 seconds
        double increasedSpeed = SPEED + (speedIncreaseRate * timer);
        return Math.min(increasedSpeed, 5.0); // Cap the speed at 5
	}
	
	public void initiateSlowdown() {
        if (!slowingDown) {
            slowingDown = true;
            slowdownTimer = SLOWDOWN_DURATION;
            originalSpeed = getSpeed(); // Capture the current speed of the sprite
        }
    }
	
	public long getUpperWait() {
		return TimeUnit.SECONDS.toNanos((long) UPPER_WAIT);
	}
	
	public long getLowerWait() {
		return TimeUnit.SECONDS.toNanos((long) LOWER_WAIT);
	}	
}
