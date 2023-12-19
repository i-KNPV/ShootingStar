package enemies;

import screens.GameScreen;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Rocket extends Enemy{
	private double SPEED = 8.0;
	private static final int DAMAGE = 50;
	private double originalSpeed;
	private ImageView image;
	protected static final double RADIUS = 20.0;
	
	public Rocket(double sceneWidth, double sceneHeight, GameScreen scene, ImageView rocketImage) {
		super(sceneWidth, sceneHeight, scene);
		this.image = rocketImage;
		this.getObject().setFill(Color.RED);
		
		image.setFitWidth(RADIUS * 4); 
		image.setFitHeight(RADIUS * 4);
		image.setPreserveRatio(true);
		image.setVisible(true);
		
		updateImagePosition();
	}
	
	@Override
	protected double getSpeed() {
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
	
	private void updateImagePosition() {
    	image.setLayoutX(objectX - RADIUS);
        image.setLayoutY(objectY - RADIUS);
    }
	
	public ImageView getImage() {
		return image;
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
    	SPEED = 8.0;
    }	
}
