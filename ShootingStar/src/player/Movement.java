package player;

public class Movement {
	private double velocityX;
	private double velocityY;
	private static final double SPEED = 5.0;
	public static final double WIND_FORCE = -2;
	
    public void moveLeft() {
        velocityX = -SPEED;
    }

    public void moveRight() {
        velocityX = SPEED;
    }

    public void moveUp() {
        velocityY = WIND_FORCE * SPEED;
    }

    public void moveDown() {
        velocityY = SPEED;
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
}
