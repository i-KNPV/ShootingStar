package items;

import application.Settings;
import application.Sound;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import player.Star;
import screens.GameScreen;

public class Shimmer extends Item{
	private ImageView image;
	private Sound sound;
	private Circle object;
	protected double objectX;
    protected double objectY;
    private final static double ITEM_RADIUS = 20.0; // Size of the shimmer hit box
    private final static int HEALTH = 50; // Amount of shine it gives the player
    private double SPEED;
    private double originalSpeed;
    private Settings settings;
	
	public Shimmer(double sceneWidth, double sceneHeight, GameScreen scene, ImageView image, Settings settings) {
		super(sceneWidth, sceneHeight, scene);
		this.settings = settings;
		
		// Set up the object
		this.image = image;
		this.sound = new Sound();
		this.objectX = generateRandomX();
	    this.objectY = sceneHeight; 
		this.object = new Circle(objectX, objectY, ITEM_RADIUS, Color.PINK);
		this.object.setVisible(false);
		this.SPEED = 2.0;
		
		// Play the constant sound effect if not muted
		// This is to let the player be alert of a Shimmer in their screen ready to be obtained
		if (!settings.isSfxMuted()) {
			sound.setFile(7);
			sound.play();
			sound.loop(7);
		}
		
		// Set up the shimmer item's image
		image.setFitWidth(ITEM_RADIUS * 2.5); 
		image.setFitHeight(ITEM_RADIUS * 2.5);
		image.setPreserveRatio(true);
		image.setVisible(true);
		
		// Update the position of the image
		updateImagePosition();
	}
	
	// Get the Circle object representing the hit box of the Shimmer
	public Circle getObject() {
        return object;
    }
	
	// Handle collision with the Star player
	public int hasCollided(Star star) {
	    this.collided = true;
	    stopSoundEffect();
	    return star.getVitality() + HEALTH;
	}
	
	// Update the position of the shimmer item
	public void updatePosition() {
        if (!collided) {
            if (slowingDown) { // Check if the shimmer is slowing down
                double frameDuration = 0.016;
                slowdownTimer -= frameDuration;
                updateImagePosition();
                
                // Slow down rate calculation
                if (slowdownTimer > 0) {
                    double slowdownRate = originalSpeed / SLOWDOWN_DURATION * frameDuration;
                    SPEED = Math.max(SPEED - slowdownRate, 0);
                } else {
                    SPEED = 0; 
                    slowingDown = false;
                }
            }
            
            // Check if the item is out of bounds
            // If yes, stop playing the constant sound
            if (isOutOfBounds(sceneHeight)) {
                stopSoundEffect();
            }
            
            // Update the position
            objectY -= SPEED; 
            object.setCenterX(objectX);
            object.setCenterY(objectY);
            updateImagePosition();
        }
    }
	
	// Update the position of the shimmer item image
	private void updateImagePosition() {
    	image.setLayoutX(objectX - ITEM_RADIUS); 
        image.setLayoutY(objectY - ITEM_RADIUS);
    }
	
	// Initiate the slow down of the shimmer item
	public void initiateSlowdown() {
        if (!slowingDown) {
            slowingDown = true;
            slowdownTimer = SLOWDOWN_DURATION;
            originalSpeed = getSpeed();
        }
    }
	
	// Check if the shimmer is out of bounds
	public boolean isOutOfBounds(double sceneHeight) {
		return objectY + ITEM_RADIUS < 0 || objectY - ITEM_RADIUS > sceneHeight;
	}
	
	// Reset the shimmer item's state
	@Override
    public void reset() {
        super.reset();
        SPEED = 2.0; 
    }
	
	// Stop the sound effect
	public void stopSoundEffect() {
		if (!settings.isSfxMuted()) {
			sound.stop();
		}
	}
	
	// Get the ImageView of the shimmer item
	public ImageView getImage() {
		return image;
	}

}
