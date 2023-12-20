package enemies;
import screens.GameScreen;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Bird extends Enemy{
	private double SPEED = 2.0; // Original speed of the bird
	private double originalSpeed; 
	private double timer = 0;
	private static final Image sprite = new Image("assets/sprites/bird.png");
	private static final int DAMAGE = 25; // Damage dealt by the bird
	protected static final double RADIUS = 20.0; // Size of the bird
	
	public Bird(double sceneWidth, double sceneHeight, GameScreen scene) {
		super(sceneWidth, sceneHeight, scene);
		this.getObject().setFill(Color.ORANGE);
		
		// Set up the bird's image
		image = new ImageView(sprite);
		image.setFitWidth(RADIUS * 4); 
		image.setFitHeight(RADIUS * 4);
		image.setPreserveRatio(true);
		image.setVisible(true);
		
		// Update the position of the image
		updateImagePosition();
	}
	
	// Reset the bird's state
	@Override
	public void reset() {
    	super.reset();
    	SPEED = 2.0;
    }	
	
	// Get the current spped of the bird
	@Override
	protected double getSpeed() {
        if (!slowingDown) {
            SPEED = calculateSpeed();
        }
        return SPEED;
	}
	
	// Get the damage caused by the bird
	@Override
	public int getDamage() {
		return DAMAGE;
	}
	
	// Update the position of the bird across the screen
	@Override
	public void updatePosition() {
		// Check if the star has collided with zero vitality left
        if (!collided) {
            objectY -= getSpeed();
            object.setCenterX(objectX);
            object.setCenterY(objectY);
            updateImagePosition();
        } else { 
        	if (slowingDown) { // Check if the bird is slowing down
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
                updateImagePosition();
            }
        }
    }
	
	// Initiate the slow down sequence of the bird
	public void initiateSlowdown() {
        if (!slowingDown) {
            slowingDown = true;
            slowdownTimer = SLOWDOWN_DURATION;
            originalSpeed = getSpeed(); 
        }
    }
	
	// Calculate the speed of the bird
	private double calculateSpeed() {
		timer = scene.getGeneralTimer();
        double speedIncreaseRate = 0.033; // Rate at which speed increases
        
        double increasedSpeed = SPEED + (speedIncreaseRate * timer);
        return Math.min(increasedSpeed, 5.0); // Cap the speed at 5
	}
	
	// Update the position of the bird's image
	private void updateImagePosition() {
    	image.setLayoutX(objectX - RADIUS); 
        image.setLayoutY(objectY - RADIUS);
    }
}
