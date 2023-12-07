package enemies;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.scene.Group;

public class RocketSpawner {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private double sceneWidth;
    private double sceneHeight;
    private Group root;
    
    
    public RocketSpawner(Group root, double sceneWidth, double sceneHeight) {
    	this.sceneWidth = sceneWidth;
    	this.sceneHeight = sceneHeight;
    	this.root = root;
    }
    
    public void startSpawning() {
        scheduler.scheduleAtFixedRate(this::spawnRocket, 0, 1, TimeUnit.SECONDS);
    }

    private void spawnRocket() {
        Rocket newRocket = new Rocket(sceneWidth, sceneHeight);
        root.getChildren().add(newRocket.getObject());
    }

    public void stopSpawning() {
        scheduler.shutdown();
    }
}