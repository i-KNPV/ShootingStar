package enemies;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.PauseTransition;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import player.Star;
import application.Settings;
import application.Sound;
import screens.GameScreen;

public class Laser extends Enemy{
    private Rectangle warningRectangle;
    private ImageView view_laser;
    private Group root;
    private GameScreen screen;
    private Sound sound;
    private static final int DAMAGE = 100;
    private static final Duration WARNING_DURATION = Duration.seconds(5);
    private static final Duration LASER_ACTIVE_DURATION = Duration.seconds(3);
    private static final Duration DELAY_BEFORE_ACTIVE = Duration.seconds(0.6);
    private static final Duration DISPERSE_DURATION = Duration.seconds(0.5);
    private Settings settings;

    public Laser(double sceneWidth, double sceneHeight, GameScreen screen, Settings settings) {
        super(sceneWidth, sceneHeight, screen);
    	this.root = screen.getRoot();
        this.screen = screen;
        this.sound = new Sound();
        this.settings = settings;
        
        double randomX = Math.random() * (sceneWidth - 20);
        
        // Initialize warning rectangle
        warningRectangle = new Rectangle(20, sceneHeight, Color.RED);
        warningRectangle.setOpacity(0);
        warningRectangle.setX(randomX);
        warningRectangle.setY(0);

        // Initialize laser image
        Image laserImg = new Image("assets/sprites/laser.png");
        view_laser = new ImageView(laserImg);
        view_laser.setFitHeight(sceneHeight);
        view_laser.setPreserveRatio(true);
        view_laser.setOpacity(0);
        view_laser.setX(randomX);
        view_laser.setY(0);

        root.getChildren().addAll(warningRectangle, view_laser);
  
    }

    public void activate(Star player) {
    	playSound();
    	
        // Blinking warning
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), warningRectangle);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(0.4);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), warningRectangle);
        fadeOut.setFromValue(0.4);
        fadeOut.setToValue(0);

        SequentialTransition blinking = new SequentialTransition(fadeIn, fadeOut);
        blinking.setCycleCount((int) (WARNING_DURATION.toMillis() / 1000));
        blinking.play();

        // Delay before showing active laser rectangle
        blinking.setOnFinished(e -> {
            PauseTransition delay = new PauseTransition(DELAY_BEFORE_ACTIVE);
            delay.setOnFinished(event -> {
                root.getChildren().remove(warningRectangle);
                view_laser.setOpacity(1); // Show active laser

                PauseTransition pause = new PauseTransition(LASER_ACTIVE_DURATION);
                pause.setOnFinished(ev -> {
                    FadeTransition fadeOutLaser = new FadeTransition(DISPERSE_DURATION, view_laser);
                    fadeOutLaser.setFromValue(1.0);
                    fadeOutLaser.setToValue(0.0);
                    fadeOutLaser.setOnFinished(fadeEvent -> {
                        root.getChildren().remove(view_laser);
                        view_laser.setOpacity(0);
                        screen.setHasLaser(false);
                    });
                    fadeOutLaser.play();
                });
                pause.play();
            });
            delay.play();
        });

        // Collision detection with active laser
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (view_laser.getOpacity() > 0 && view_laser.getBoundsInParent().intersects(player.getObject().getBoundsInParent())) {
                	System.out.println("Star collided with Laser!");
                	player.handleCollisions(getLaser());
                    this.stop();
                }
            }
        }.start();
    }
    
    private void playSound() {
    	if (!settings.isSfxMuted())  {
    		sound.setFile(9);
        	sound.play();
    	}
    }
    
    @Override
    public boolean isCollidedWithStar(Star star) {
    	return true;
    }
    
    private Laser getLaser() {
    	return this;
    }
    
    public int getDamage() {
    	return DAMAGE;
    }
    
    public void reset() {
    	sound.stop();
    }
    
}
