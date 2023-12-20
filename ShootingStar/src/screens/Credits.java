// Credits.java
package screens;

import application.Sound;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

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

        

//        // Add a back button with an image
//        Button backButton = new Button();
//        backButton.setOnAction(event -> showMainMenu());

        // Create an ImageView for the button
        // Image backImage = new Image("assets/buttons/return.png", 150, 150, true, true);
//        Image backImage = new Image("assets/buttons/return.png");
//        ImageView backImageView = new ImageView(backImage);
//        
//        backImageView.setFitWidth(200);
//        backImageView.setPreserveRatio(true);

//        // Set the graphic of the button to the ImageView
//        backButton.setGraphic(backImageView);

//        // Set the alignment of the back button
//        StackPane.setAlignment(backButton, Pos.BOTTOM_LEFT);
//
//        // Set margins for the back button to adjust its position
//        StackPane.setMargin(backButton, new Insets(0, 10, 10, 10)); // Adjust values as needed
        
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
//            	for (Node child : creditsLayout.getChildren()) {
//                    if (child != view_bg) {
//                        child.setVisible(false);
//                    }
//                }
//            	for (Node child : starsLayout.getChildren()) {
//                    if (child != view_bg) {
//                        child.setVisible(true);
//                    }
//                }
            	break;
        }
    }
    
    public void showStars() {
    	
    }
    
    
    public Scene getScene() {
        return creditsScene;
    }

    private void showMainMenu() {
        System.out.println("Switching back to the Main Menu");
        menu.showChildren();
        primaryStage.setTitle("Shooting Star [Main Menu] [alpha]");
        primaryStage.setScene(menu.getScene());
    }

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
