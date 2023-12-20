package enemies;

import screens.GameScreen;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Rocket extends Enemy{
	private double SPEED = 8.0; // Original speed of the rocket
	private static final int DAMAGE = 50; // Damage caused by the rocket
	private double originalSpeed; 
	private ImageView image;
	protected static final double RADIUS = 20.0; // Radius for the rocket's size
	
	public Rocket(double sceneWidth, double sceneHeight, GameScreen scene, ImageView rocketImage) {
		super(sceneWidth, sceneHeight, scene);
		this.image = rocketImage;
		this.getObject().setFill(Color.RED);
		
		// Set up the rocket's image
		image.setFitWidth(RADIUS * 4); 
		image.setFitHeight(RADIUS * 4);
		image.setPreserveRatio(true);
		image.setVisible(true);
		
		// Update the position of the image
		updateImagePosition();
	}
	
	// Get the current speed of the rocket
	@Override
	protected double getSpeed() {
		return SPEED;
	}
	
	// Get the damage value
	@Override
	public int getDamage() {
		return DAMAGE;
	}
	
	// Update the position of the rocket
	@Override
	public void updatePosition() {
        if (!collided) { // Check if the star has collided with zero vitality left
            objectY -= getSpeed();
            object.setCenterX(objectX);
            object.setCenterY(objectY);
            updateImagePosition();
        } else { 
        	if (slowingDown) { // Check if this rocket is slowing down
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
	
	// Update the position of the rocket
	private void updateImagePosition() {
    	image.setLayoutX(objectX - RADIUS);
        image.setLayoutY(objectY - RADIUS);
    }
	
	// Get the ImageView of the rocket
	public ImageView getImage() {
		return image;
	}
	
	// Initiate the slowdown of the rocket
	public void initiateSlowdown() {
        if (!slowingDown) {
            slowingDown = true;
            slowdownTimer = SLOWDOWN_DURATION;
            originalSpeed = getSpeed(); 
        }
    }
	
	// Reset the rocket's state
	@Override
	public void reset() {
    	super.reset();
    	SPEED = 8.0;
    }	
}
