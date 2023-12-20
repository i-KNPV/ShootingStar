package items;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import player.Sprite;
import player.Star;
import screens.GameScreen;

public class Item extends Sprite{
    protected static List<Item> items = new ArrayList<>(); // List to keep track of all item instances
    private double originalSpeed;
    protected ImageView image;
	
	public Item(double sceneWidth, double sceneHeight, GameScreen scene) {
		super(sceneWidth, sceneHeight, scene);
		
	}
	
	// Get the Circle object representing the item's hit box
	public Circle getObject() {
        return object;
    }
	
	// Set the collision state of an item
	public void hasCollided(Item item) {
    	item.collided = true;
    }
	
	// Get the list of all active items on screen
	public static List<Item> getItems(){
    	return items;
    }
    
	// Add an item to the list
    public static void addItem(Item item) {
    	items.add(item);
    }
    
    // Clear the list of items
    public static void clearItems() {
        items.clear();
    }
	
    // Update the position of the item
	public void updatePosition() {
        if (!collided) {
           
            objectY -= SPEED; 
            object.setCenterX(objectX);
            object.setCenterY(objectY);
        } else {
        	if (slowingDown) { // Check if the item is slowing down
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
	
	// Initiate the slow down of the item
	public void initiateSlowdown() {
	    if (!slowingDown) {
	        slowingDown = true;
	        slowdownTimer = SLOWDOWN_DURATION;
	        originalSpeed = getSpeed(); 
	    }
	}
	
	// Check collision with the Star player
	public boolean isCollidedWithStar(Star star) {
	    return star.getObject().getBoundsInParent().intersects(object.getBoundsInParent());
	}
	 
	// Check if the item is out of bounds
	public boolean isOutOfBounds() {
	    return objectY < 0 - ENEMY_RADIUS;
	}
	
	// Reset the item's state
	public void reset() {
        collided = false;
        slowingDown = false;
        SPEED = 2.0;
	}
	
	// Get the ImageView of the item
	public ImageView getImage() {
		return image;
	}

}
