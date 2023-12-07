package enemies;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import player.Star;
import java.util.Random;


import java.util.ArrayList;
import java.util.List;


public class Enemy {
    protected Circle object;
    protected double objectX;
    protected double objectY;
    protected final double sceneWidth;
    protected final double sceneHeight;
    protected boolean collided;

    protected static final double ENEMY_RADIUS = 20.0;
    protected double SPEED = 3.0;
    protected static List<Enemy> enemies = new ArrayList<>();

    public Enemy(double sceneWidth, double sceneHeight) {
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.objectX = generateRandomX();
        this.objectY = sceneHeight + ENEMY_RADIUS;
        this.object = new Circle(objectX, objectY, ENEMY_RADIUS, Color.RED);
        this.collided = false;
    }

    public Circle getObject() {
        return object;
    }
    
    protected double getSpeed() {
    	return SPEED;
    }
    
    public void updatePosition() {
		if(!collided) {
			double speed = getSpeed();
			objectY -= speed;
			
			if (objectY < 0 - ENEMY_RADIUS) {
				resetEnemy();
			}
			
			object.setCenterX(objectX);
			object.setCenterY(objectY);
		}
	}

    public boolean isCollidedWithStar(Star star) {
    	return star.getObject().getBoundsInParent().intersects(object.getBoundsInParent());
	}
    
    public void hasCollided(boolean state) {
    	for (int i = 0; i < enemies.size(); i++) {
    		enemies.get(i).collided = true;
    	}
    }
    
    private double generateRandomX() {
		Random random = new Random();
		return random.nextDouble() * sceneWidth;
	}
    
    protected void resetEnemy() {
		objectX = generateRandomX();
        objectY = sceneHeight + ENEMY_RADIUS; // Start enemy at bottom of the screen
        object.setCenterX(objectX);
        object.setCenterY(objectY);
        collided = false;
	}
    
    public static List<Enemy> getEnemies(){
    	return enemies;
    }
    
    public static void addEnemy(Enemy enemy) {
    	enemies.add(enemy);
    }

}