package items;

import application.Sound;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import player.Star;
import screens.GameScreen;

public class Shimmer extends Item{
	private ImageView image;
	private Sound sound;
	private Circle object;
	protected double objectX;
    protected double objectY;
    private final static double ITEM_RADIUS = 20.0;
    private final static int HEALTH = 40;
    private double SPEED;
    private double originalSpeed;
	
	public Shimmer(double sceneWidth, double sceneHeight, GameScreen scene) {
		super(sceneWidth, sceneHeight, scene);
		
		this.sound = new Sound();
		this.objectX = generateRandomX();
	    this.objectY = sceneHeight; 
		this.object = new Circle(objectX, objectY, ITEM_RADIUS, Color.PINK);
		this.object.setVisible(false);
		this.SPEED = 2.0;
		
		sound.setFile(7);
		sound.play();
		sound.loop(7);
		
		Image sprite = new Image("assets/sprites/shimmer.gif");
		image = new ImageView(sprite);
		image.setFitWidth(ITEM_RADIUS * 2.5); 
		image.setFitHeight(ITEM_RADIUS * 2.5);
		image.setPreserveRatio(true);
		image.setVisible(true);
		
		updateImagePosition();
	}
	
	public Circle getObject() {
        return object;
    }
	
	public int hasCollided(Star star) {
	    this.collided = true;
	    stopSoundEffect();
	    return star.getVitality() + HEALTH;
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
            
            if (isOutOfBounds(sceneHeight)) {
                stopSoundEffect();
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
	
	public void stopSoundEffect() {
		sound.stop();
	}
	
	public ImageView getImage() {
		return image;
	}

}
