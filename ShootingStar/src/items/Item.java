package items;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.Circle;
import player.Sprite;
import player.Star;
import screens.GameScreen;

public class Item extends Sprite{
    protected static List<Item> items = new ArrayList<>();
    private double originalSpeed;
	
	public Item(double sceneWidth, double sceneHeight, GameScreen scene) {
		super(sceneWidth, sceneHeight, scene);
		
	}
	
	public Circle getObject() {
        return object;
    }
	
	public void hasCollided(Item item) {
    	item.collided = true;
    }
	
	public static List<Item> getItems(){
    	return items;
    }
    
    public static void addItem(Item item) {
    	items.add(item);
    }
	
	 public void updatePosition() {
	        if (!collided) {
	           
	            objectY -= SPEED; // Use dynamic speed
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
	
	 public boolean isCollidedWithStar(Star star) {
	    return star.getObject().getBoundsInParent().intersects(object.getBoundsInParent());
	}
	 
	 public boolean isOutOfBounds() {
	    return objectY < 0 - ENEMY_RADIUS;
	 }
}
