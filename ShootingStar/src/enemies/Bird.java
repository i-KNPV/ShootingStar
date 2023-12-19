package enemies;
import screens.GameScreen;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Bird extends Enemy{
	private double SPEED = 2.0;
	private static final int DAMAGE = 25;
	private double originalSpeed;
	private static final Image sprite = new Image("assets/sprites/bird.png");
	protected static final double RADIUS = 20.0;
	private double timer = 0;
	
	public Bird(double sceneWidth, double sceneHeight, GameScreen scene) {
		super(sceneWidth, sceneHeight, scene);
		this.getObject().setFill(Color.ORANGE);
		
		image = new ImageView(sprite);
		image.setFitWidth(RADIUS * 4); 
		image.setFitHeight(RADIUS * 4);
		image.setPreserveRatio(true);
		image.setVisible(true);
		
		updateImagePosition();
	}
	@Override
	protected double getSpeed() {
        if (!slowingDown) {
            SPEED = calculateSpeed();
        }
        return SPEED;
	}
	
	@Override
	public int getDamage() {
		return DAMAGE;
	}
	
	@Override
	public void updatePosition() {
        if (!collided) {
            
            objectY -= getSpeed();
            object.setCenterX(objectX);
            object.setCenterY(objectY);
            updateImagePosition();
        } else {
        	if (slowingDown) {
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
                updateImagePosition();
            }
        	
        	

        }
    }
	
	private double calculateSpeed() {
		timer = scene.getGeneralTimer();
        double speedIncreaseRate = 0.033; 
        
        double increasedSpeed = SPEED + (speedIncreaseRate * timer);
        return Math.min(increasedSpeed, 5.0); // Cap the speed at 5
	}
	
	private void updateImagePosition() {
    	image.setLayoutX(objectX - RADIUS); 
        image.setLayoutY(objectY - RADIUS);
    }
	
	public void initiateSlowdown() {
        if (!slowingDown) {
            slowingDown = true;
            slowdownTimer = SLOWDOWN_DURATION;
            originalSpeed = getSpeed(); 
        }
    }
	
	@Override
	public void reset() {
    	super.reset();
    	SPEED = 2.0;
    }	
}
