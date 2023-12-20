package enemies;

import player.Star;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;
import player.Sprite;
import screens.GameScreen;

public class Enemy extends Sprite{
    protected boolean collided;
    protected int damage;
	protected ImageView image;
    protected static final double ENEMY_RADIUS = 20.0;
    protected static List<Enemy> enemies = new ArrayList<>();

    public Enemy(double sceneWidth, double sceneHeight, GameScreen scene) {
        super(sceneWidth, sceneHeight, scene); 
    }

    public boolean isCollidedWithStar(Star star) {
    	return star.getObject().getBoundsInParent().intersects(object.getBoundsInParent());
	}
    
    public void hasCollided(boolean state) {
    	for (int i = 0; i < enemies.size(); i++) {
    		enemies.get(i).collided = true;
    	}
    }
    
    public static List<Enemy> getEnemies(){
    	return enemies;
    }
    
    public static void clearEnemies() {
        enemies.clear();
    }
    
    public static void addEnemy(Enemy enemy) {
    	enemies.add(enemy);
    }
    
    public int getDamage() {
		return damage;
	}
    
    public boolean isOutOfBounds() {
    	return objectY < 0 - (ENEMY_RADIUS + 30);
    }
    
    public void reset() {
    	collided = false;
    	slowingDown = false;
    	SPEED = 3.0;
    }
    
    public ImageView getImage() {
		return image;
	}

}