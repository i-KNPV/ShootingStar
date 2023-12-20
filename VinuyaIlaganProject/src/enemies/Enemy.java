package enemies;

import player.Star;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;
import player.Sprite;
import screens.GameScreen;

public class Enemy extends Sprite{
    protected boolean collided; // Flag to indicate collision state (star was hit and has 0 shine left)
    protected int damage; // Damage inflicted by the enemy
	protected ImageView image; // Image representing the enemy
    protected static final double ENEMY_RADIUS = 20.0; // Radius for enemy size
    protected static List<Enemy> enemies = new ArrayList<>(); // List to store all enemy instances
    
    public Enemy(double sceneWidth, double sceneHeight, GameScreen scene) {
        super(sceneWidth, sceneHeight, scene); 
    }
    
    // Check if the enemy has collided with the player (Star object)
    public boolean isCollidedWithStar(Star star) {
    	return star.getObject().getBoundsInParent().intersects(object.getBoundsInParent());
	}
    
    // Set the collision state for all enemies
    public void hasCollided(boolean state) {
    	for (int i = 0; i < enemies.size(); i++) {
    		enemies.get(i).collided = true;
    	}
    }
    
    // Get the list of all enemy instances
    public static List<Enemy> getEnemies(){
    	return enemies;
    }
    
    // Clear the list of enemies
    public static void clearEnemies() {
        enemies.clear();
    }
    
    // Add an enemy to the list
    public static void addEnemy(Enemy enemy) {
    	enemies.add(enemy);
    }
    
    // Get the damage value 
    public int getDamage() {
		return damage;
	}
    
    // Check if the enemy is out of the game's boundaries + extra freedom
    public boolean isOutOfBounds() {
    	return objectY < 0 - (ENEMY_RADIUS + 30);
    }
    
    // Reset the enemy's state
    public void reset() {
    	collided = false;
    	slowingDown = false;
    	SPEED = 3.0;
    }
    
    // get the ImageView of the enemy
    public ImageView getImage() {
		return image;
	}

}