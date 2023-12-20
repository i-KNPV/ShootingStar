package items;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import player.Star;
import screens.GameScreen;

public class Boost extends Item{
	private ImageView image;
	private Circle object;
	protected double objectX;
    protected double objectY;
    private final static double ITEM_RADIUS = 20.0; // Size of the boost's hit box
    private double SPEED;
    private double originalSpeed;
    
    public Boost(double sceneWidth, double sceneHeight, GameScreen scene, ImageView image) {
		super(sceneWidth, sceneHeight, scene);
		
		// Set up the object (to be used as a hit box)
		this.image = image;
		this.objectX = generateRandomX();
	    this.objectY = sceneHeight; 
		this.object = new Circle(objectX, objectY, ITEM_RADIUS, Color.PINK);
		this.object.setVisible(false);
		this.SPEED = 2.0;
		
		// Set up the boost's image
		image.setFitWidth(ITEM_RADIUS * 2.5); 
		image.setFitHeight(ITEM_RADIUS * 2.5);
		image.setPreserveRatio(true);
		image.setVisible(true);
		
		// Update the position of the image
		updateImagePosition();
	}
    
    // Get the Circle object
    public Circle getObject() {
        return object;
    }
	
    // Handle collision with the Star player
	public void hasCollided(Star star) {
	    this.collided = true;
	    star.viewInventory().addBoost();
	}
	
	// Update the position of the boost item
	public void updatePosition() {
        if (!collided) {
            if (slowingDown) { // Check if the item is slowing down
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
            
            // Update the position
            objectY -= SPEED; 
            object.setCenterX(objectX);
            object.setCenterY(objectY);
            updateImagePosition();
        }
    }
	
	// Update the position of the boost
	private void updateImagePosition() {
    	image.setLayoutX(objectX - ITEM_RADIUS); 
        image.setLayoutY(objectY - ITEM_RADIUS);
    }
	
	// Initiate the slow down of this boost item
	public void initiateSlowdown() {
        if (!slowingDown) {
            slowingDown = true;
            slowdownTimer = SLOWDOWN_DURATION;
            originalSpeed = getSpeed();
        }
    }
	
	// Check if this boost item is out of bounds
	public boolean isOutOfBounds(double sceneHeight) {
		return objectY + ITEM_RADIUS < 0 || objectY - ITEM_RADIUS > sceneHeight;
	}
	
	// Reset the boost item's state
	@Override
    public void reset() {
        super.reset();
        SPEED = 2.0; 
    }
	
	// Get the ImageView of the boost item
	public ImageView getImage() {
		return image;
	}
}
