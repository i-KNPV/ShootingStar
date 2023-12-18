package player;

public class Movement {
	private double velocityX;
	private double velocityY;
	private static final double SPEED = 6.0;
	public static final double WIND_FORCE = -2;
	private double speedMultiplier = 1.0;
	
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
    
    public void stopVertical() {
        velocityY = 0;
    }

    public void stopHorizontal() {
        velocityX = 0;
        
    }
    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }
    
    public void setSpeedMultiplier(double multiplier) {
        this.speedMultiplier = multiplier;
    }

    public double getSpeedMultiplier() {
        return this.speedMultiplier;
    }

}
