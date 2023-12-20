package player;

public class Movement {
	private double velocityX; // Horizontal velocity of the object
    private double velocityY; // Vertical velocity of the object
    private static final double SPEED = 6.0; // Base speed for movement
    public static final double WIND_FORCE = -2; // Constant wind force affecting vertical movement
    private double speedMultiplier = 1.0; // Multiplier to adjust speed dynamically
	
	// Methods to move the object depending on key press
	
    public void moveLeft() {
        velocityX = -SPEED * speedMultiplier;
    }

    public void moveRight() {
        velocityX = SPEED * speedMultiplier;
    }

    public void moveUp() {
        velocityY = WIND_FORCE * SPEED * speedMultiplier;
    }

    public void moveDown() {
        velocityY = SPEED * speedMultiplier;
    }
    
    // Methods to halt the object's vertical and horizontal movement
    
    public void stopVertical() {
        velocityY = 0;
    }

    public void stopHorizontal() {
        velocityX = 0;
        
    }
    
    // Methods to get the current horizontal and vertical velocity
    
    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }
    
    // Set the speed multiplier
    public void setSpeedMultiplier(double multiplier) {
        this.speedMultiplier = multiplier;
    }
    
    // Get the speed multiplier
    public double getSpeedMultiplier() {
        return this.speedMultiplier;
    }

}
