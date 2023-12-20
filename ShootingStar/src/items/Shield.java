package items;

import application.Sound;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import player.Star;
import screens.GameScreen;

public class Shield extends Item{
	private ImageView image;
	private Circle object;
	protected double objectX;
    protected double objectY;
    private final static double ITEM_RADIUS = 20.0;
    private double SPEED;
    private double originalSpeed;
    
    public Shield(double sceneWidth, double sceneHeight, GameScreen scene, ImageView image) {
		super(sceneWidth, sceneHeight, scene);
		
		this.image = image;
		this.objectX = generateRandomX();
	    this.objectY = sceneHeight; 
		this.object = new Circle(objectX, objectY, ITEM_RADIUS, Color.PINK);
		this.object.setVisible(false);
		this.SPEED = 2.0;
		
		image.setFitWidth(ITEM_RADIUS * 2.5); 
		image.setFitHeight(ITEM_RADIUS * 2.5);
		image.setPreserveRatio(true);
		image.setVisible(true);
		
		updateImagePosition();
	}
    
    public Circle getObject() {
        return object;
    }
	
	public void hasCollided(Star star) {
	    this.collided = true;
	    star.viewInventory().addBoost();
	}
	
	public void updatePosition() {
        if (!collided) {
            if (slowingDown) {
                double frameDuration = 0.016;
                slowdownTimer -= frameDuration;
                updateImagePosition();

                if (slowdownTimer > 0) {
                    double slowdownRate = originalSpeed / SLOWDOWN_DURATION * frameDuration;
                    SPEED = Math.max(SPEED - slowdownRate, 0);
                } else {
                    SPEED = 0; 
                    slowingDown = false;
                }
            }

            objectY -= SPEED; 
            object.setCenterX(objectX);
            object.setCenterY(objectY);
            updateImagePosition();
        } 
    }
	
	private void updateImagePosition() {
    	image.setLayoutX(objectX - ITEM_RADIUS); 
        image.setLayoutY(objectY - ITEM_RADIUS);
    }
	
	public void initiateSlowdown() {
        if (!slowingDown) {
            slowingDown = true;
            slowdownTimer = SLOWDOWN_DURATION;
            originalSpeed = getSpeed();
        }
    }
	
	public boolean isOutOfBounds(double sceneHeight) {
		return objectY + ITEM_RADIUS < 0 || objectY - ITEM_RADIUS > sceneHeight;
	}
	
	
	@Override
    public void reset() {
        super.reset();
        SPEED = 2.0; 
    }
	
	public ImageView getImage() {
		return image;
	}
}
