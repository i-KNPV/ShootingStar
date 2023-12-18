package player;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Inventory {
	  
    private static final Image SPRITE = new Image("assets/sprites/inventory.png");
	private ImageView image;
    private int holdingItem; 
    private double boostSpeedMultiplier = 2.0;
    
    private static final int EMPTY = 0;
    private static final int BOOST = 1;
    private static final int INVINCIBLE = 2;
    
	public Inventory() {
		holdingItem = EMPTY;
		
		image = new ImageView(SPRITE);
		image.setFitWidth(50);
		image.setPreserveRatio(true);
		image.setVisible(true);
	}
	
	public void addBoost() {
		holdingItem = BOOST;
	}
	
	public void addInvincible() {
		holdingItem = INVINCIBLE;
	}
	
	public void clearInventory() {
		holdingItem = EMPTY;
	}
	
	public void applyBoost(Movement movement) {
		movement.setSpeedMultiplier(boostSpeedMultiplier);
    }

	public void resetBoost(Movement movement) {
    	movement.setSpeedMultiplier(1.0); // Reset to normal speed
    }
    
    public int getInventory () {
    	return holdingItem;
    }
    
    public ImageView getImage() {
    	return image;
    }
}
