package enemies;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.util.Random;
import player.Star;

public class Rocket {
	private Circle object;
	private double objectX;
	private double objectY;
	private double velocityY;
	private final double sceneWidth;
	private final double sceneHeight;
	private boolean collided;
	private Duration survivalTime;
	
	private static final double ENEMY_RADIUS = 20.0;
	private static final double SPEED = 3.0;
	
	public Rocket(double sceneWidth, double sceneHeight) {
		this.sceneWidth = sceneWidth;
		this.sceneHeight = sceneHeight;
		this.objectX = generateRandomX();
		this.objectY = sceneHeight + ENEMY_RADIUS;
		this.velocityY = -SPEED;
		this.object = new Circle(objectX, objectY, ENEMY_RADIUS, Color.RED);
		this.collided = false;
		this.survivalTime = Duration.ZERO;
	}
	
	public Circle getObject() {
		return object;
	}
	
	public void updatePosition() {
		if(!collided) {
			objectY += velocityY;
			
			if (objectY < 0 - ENEMY_RADIUS) {
				resetEnemy();
			}
			
			object.setCenterX(objectX);
			object.setCenterY(objectY);
		}
	}
	
	public boolean isCollidedWithStar(Star star) {
		if (star.getObject().getBoundsInParent().intersects(object.getBoundsInParent())) {
			collided = true;
			return true;
		}
		
		return false;
	}
	
	public void updateSurvivalTime(javafx.util.Duration duration) {
        if (!collided) {
            survivalTime = duration;
        }
    }
	
	public javafx.util.Duration getSurvivalTime() {
        return survivalTime;
    }
	
	public boolean isCollided() {
		return collided;
	}
	
	private double generateRandomX() {
		Random random = new Random();
		return random.nextDouble() * sceneWidth;
	}
	
	private void resetEnemy() {
		objectX = generateRandomX();
        objectY = sceneHeight + ENEMY_RADIUS; // Start enemy at bottom of the screen
        object.setCenterX(objectX);
        object.setCenterY(objectY);
        collided = false;
        survivalTime = Duration.ZERO;
	}
	
	
}
