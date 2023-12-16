package enemies;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import player.Star;

public class Laser {
	private ImageView laserImage;
	private Rectangle warningRectangle;
	private Group root;
	private final double sceneHeight;
	private final double sceneWidth;
	private static final int DAMAGE = 100;
	private static final Duration WARNING_DURATION = Duration.seconds(5);
    private static final Duration LASER_DURATION = Duration.seconds(3);
    
    public Laser(double sceneWidth, double sceneHeight, Group root) {
    	this.sceneHeight = sceneHeight;
    	this.sceneWidth = sceneWidth;
    	this.root = root;
    	
    	warningRectangle = new Rectangle(sceneWidth, 10, Color.RED);
    	warningRectangle.setOpacity(0);
    	
    	Image laserImg = new Image("assets/sprites/laser.png");
        laserImage = new ImageView(laserImg);
        laserImage.setFitHeight(sceneHeight);
        laserImage.setFitWidth(10); // Adjust width as needed
        laserImage.setVisible(false);
        laserImage.setTranslateY(sceneHeight);
        
        root.getChildren().addAll(warningRectangle, laserImage);
    }
    
    public void activate(Star player) {
        // Blinking warning
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), warningRectangle);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(0.7);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), warningRectangle);
        fadeOut.setFromValue(0.7);
        fadeOut.setToValue(0);

        SequentialTransition blinking = new SequentialTransition(fadeIn, fadeOut);
        blinking.setCycleCount((int) (WARNING_DURATION.toMillis() / 1000));

        // Laser movement
        TranslateTransition laserMove = new TranslateTransition(LASER_DURATION, laserImage);
        laserMove.setFromY(sceneHeight);
        laserMove.setToY(-sceneHeight);

        laserMove.setOnFinished(e -> root.getChildren().remove(laserImage));

        // Collision detection
        laserMove.currentTimeProperty().addListener((obs, old, current) -> {
            if (laserImage.getBoundsInParent().intersects(player.getObject().getBoundsInParent())) {
                player.setVitality(player.getVitality() - DAMAGE);
            }
        });

        // Start transitions
        blinking.play();
        blinking.setOnFinished(e -> {
            warningRectangle.setOpacity(0);
            root.getChildren().remove(warningRectangle);
            laserImage.setVisible(true);
            laserMove.play();
        });
    }
}
