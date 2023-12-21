// Credits.java
package screens;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Credits {
    private Stage primaryStage;
    private Scene creditsScene;
    private MainMenu menu;
    private StackPane creditsLayout;
    

    public Credits(Stage primaryStage, MainMenu mainMenu) {
    	this.menu = mainMenu;
        this.primaryStage = primaryStage;
        
        Image backgroundImage = new Image("assets/background/spacebg.gif");
        ImageView view_bg = new ImageView(backgroundImage);
        view_bg.setRotate(90);
        view_bg.setPreserveRatio(true);

        // Create a layout for the credits screen
        
        creditsLayout = new StackPane();

        VBox creditsBox = new VBox(20);
        creditsBox.setAlignment(Pos.CENTER);
        creditsBox.setVisible(true);

        // Add an image with specified width and height
        ImageWithPath developers = new ImageWithPath("assets/credits/Developers.png", 1000, 800);
        ImageView imageView = createImageView(developers);
        
        VBox starsBox = new VBox(20);
        starsBox.setAlignment(Pos.CENTER);
        
        ImageWithPath russgif = new ImageWithPath("assets/credits/RUSS.gif", 360, 360);
        ImageWithPath kylegif = new ImageWithPath("assets/credits/KYLE.gif", 360, 360);
        ImageView russ = createImageView(russgif);
        ImageView kyle = createImageView(kylegif);
        starsBox.getChildren().addAll(russ, kyle);

       
        creditsLayout.getChildren().addAll(view_bg, imageView);

        // Create the credits scene
        creditsScene = new Scene(creditsLayout, 600, 800);
        
        // Set up key event handling
        creditsScene.setOnKeyPressed(event -> handleKeyPress(event.getCode(), starsBox, creditsBox));
    }

    private void handleKeyPress(KeyCode code,VBox creditsBox,VBox starsBox) {
        switch (code) {
            case ESCAPE:
            	showMainMenu();
                break;
            case SPACE:
            	showMainMenu();
            	break;
            case RIGHT: // the secret
            	System.out.println("uwu");
            	creditsBox.setVisible(false);
                starsBox.setVisible(true);
            	break;
		default:
			break;
        }
    }
    
    public Scene getScene() {
        return creditsScene;
    }

    private void showMainMenu() {
        System.out.println("Switching back to the Main Menu");
        menu.showChildren();
        primaryStage.setTitle("Shooting Star");
        primaryStage.setScene(menu.getScene());
    }

    @SuppressWarnings("unused")
	private javafx.scene.control.Label createCreditLabel(String text) {
        javafx.scene.control.Label label = new javafx.scene.control.Label(text);
        label.setStyle("-fx-font-size: 16; -fx-text-fill: white;");
        return label;
    }

    private ImageView createImageView(ImageWithPath imageWithPath) {
        Image image = new Image(imageWithPath.getImagePath());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(imageWithPath.getWidth());
        imageView.setFitHeight(imageWithPath.getHeight());
        imageView.setPreserveRatio(true);
        return imageView;
    }

    private static class ImageWithPath {
        private String imagePath;
        private double width;
        private double height;

        public ImageWithPath(String imagePath, double width, double height) {
            this.imagePath = imagePath;
            this.width = width;
            this.height = height;
        }

        public String getImagePath() {
            return imagePath;
        }

        public double getWidth() {
            return width;
        }

        public double getHeight() {
            return height;
        }
    }
}
