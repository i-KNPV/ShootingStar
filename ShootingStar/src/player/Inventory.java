package player;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Inventory {
    private static final Image SPRITE = new Image("assets/sprites/inventory.png");
    private static final Image BOOST_SPRITE = new Image("assets/sprites/boost.png");
    private static final Image SHIELD_SPRITE = new Image("assets/sprites/shield.png");

    private ImageView image;
    private ImageView boostImage;
    private ImageView shieldImage;
    private int holdingItem; 
    private double boostSpeedMultiplier = 2.0; // Speed multiplier for boost item
    
    // Constants to represent different inventory states
    public final int EMPTY = 0;
    public final int BOOST = 1;
    public final int SHIELD = 2;
    
    public Inventory() {
        holdingItem = EMPTY; // Initialize inventory as empty
        
        // Setup inventory and item images
        image = new ImageView(SPRITE); 
        image.setFitWidth(50);
        image.setPreserveRatio(true);
        image.setVisible(true);

        boostImage = new ImageView(BOOST_SPRITE);
        boostImage.setFitWidth(50);
        boostImage.setPreserveRatio(true);
        boostImage.setVisible(false);

        shieldImage = new ImageView(SHIELD_SPRITE);
        shieldImage.setFitWidth(50);
        shieldImage.setPreserveRatio(true);
        shieldImage.setVisible(false);
    }
    
    // Add a boost item to the inventory
    public void addBoost() {
    	System.out.println("Player grabbed BOOST!");
        holdingItem = BOOST;
        updateItemDisplay();
    }
    
    // Add a shield item to the inventory
    public void addShield() {
    	System.out.println("Player grabbed SHIELD!");
        holdingItem = SHIELD;
        updateItemDisplay();
    }
    
    // Apply the effect of a boost item
    public void applyBoost(Movement movement) {
    	System.out.println("Player applied speed boost.");
        movement.setSpeedMultiplier(boostSpeedMultiplier);
    }
    
    // Clear the inventory
    public void clearInventory() {
    	System.out.println("Inventory cleared");
        holdingItem = EMPTY;
        updateItemDisplay();
    }
    
    // Update the item display based on the inventory state
    private void updateItemDisplay() {
        // Hide both images initially
        boostImage.setVisible(false);
        shieldImage.setVisible(false);

        // Show the appropriate image based on the current item
        if (holdingItem == BOOST) {
            boostImage.setVisible(true);
        } else if (holdingItem == SHIELD) {
            shieldImage.setVisible(true);
        }
    }
    
    // Reset the effect of a boost item
    public void resetBoost(Movement movement) {
        movement.setSpeedMultiplier(1.0); // Reset to normal speed
    }
    
    // Get the current item in the inventory
    public int getInventory() {
        return holdingItem;
    }
    
    // Getters for image views
    
    public ImageView getImage() {
        return image;
    }

    public ImageView getBoostImage() {
        return boostImage;
    }

    public ImageView getShieldImage() {
        return shieldImage;
    }
}
