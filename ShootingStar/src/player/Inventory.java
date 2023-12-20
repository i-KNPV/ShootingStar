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
    private double boostSpeedMultiplier = 2.0;
    
    public final int EMPTY = 0;
    public final int BOOST = 1;
    public final int SHIELD = 2;
    
    public Inventory() {
        holdingItem = EMPTY;
        
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

    public void addBoost() {
        holdingItem = BOOST;
        updateItemDisplay();
    }

    public void addShield() {
        holdingItem = SHIELD;
        updateItemDisplay();
    }

    public void applyBoost(Movement movement) {
        movement.setSpeedMultiplier(boostSpeedMultiplier);
    }

    public void clearInventory() {
        holdingItem = EMPTY;
        updateItemDisplay();
    }

    private void updateItemDisplay() {
        // Hide both images initially
        boostImage.setVisible(false);
        shieldImage.setVisible(false);

        // Show the appropriate image based on the current item
        if (holdingItem == BOOST) {
            boostImage.setVisible(true);
        } else if (holdingItem == SHIELD) {
        	System.out.println("Showing SHIELD image");
            shieldImage.setVisible(true);
        }
    }

    public void resetBoost(Movement movement) {
        movement.setSpeedMultiplier(1.0); // Reset to normal speed
    }

    public int getInventory() {
        return holdingItem;
    }

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
