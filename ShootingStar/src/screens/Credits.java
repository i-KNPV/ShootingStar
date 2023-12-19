// Credits.java
package screens;

import application.Sound;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    public Credits(Stage primaryStage) {
        this.primaryStage = primaryStage;

        Image backgroundImage = new Image("assets/background/spacebg.gif");
        ImageView view_bg = new ImageView(backgroundImage);
        view_bg.setRotate(90);
        view_bg.setPreserveRatio(true);

        // Create a layout for the credits screen
        StackPane creditsLayout = new StackPane();

        VBox creditsBox = new VBox(20);
        creditsBox.setAlignment(Pos.CENTER);

//        // Add credits content
//        creditsBox.getChildren().add(createCreditLabel("Game Developed By: Your Team"));
//        creditsBox.getChildren().add(createCreditLabel("Graphics Design: Graphic Designer"));
//        creditsBox.getChildren().add(createCreditLabel("Music and Sound Effects: Sound Designer"));

        // Add an image with specified width and height
        ImageWithPath imageWithPath = new ImageWithPath("assets/credits/Developers.png", 1000, 800);
        ImageView imageView = createImageView(imageWithPath);
        // creditsBox.getChildren().add(imageView);


        // Add a back button with an image
        Button backButton = new Button();
        backButton.setOnAction(event -> showMainMenu());

        // Create an ImageView for the button
        // Image backImage = new Image("assets/buttons/return.png", 150, 150, true, true);
        Image backImage = new Image("assets/buttons/return.png");
        ImageView backImageView = new ImageView(backImage);
        
        backImageView.setFitWidth(200);
        backImageView.setPreserveRatio(true);

        // Set the graphic of the button to the ImageView
        backButton.setGraphic(backImageView);

        // Set the alignment of the back button
        StackPane.setAlignment(backButton, Pos.BOTTOM_LEFT);

        // Set margins for the back button to adjust its position
        StackPane.setMargin(backButton, new Insets(0, 10, 10, 10)); // Adjust values as needed
        
        creditsLayout.getChildren().addAll(view_bg, imageView, backButton);

        // Create the credits scene
        creditsScene = new Scene(creditsLayout, 600, 800);
    }

    public Scene getScene() {
        return creditsScene;
    }

    private void showMainMenu() {
        System.out.println("Switching back to the Main Menu");
        MainMenu mainMenu = new MainMenu(primaryStage, 0, 0);
        primaryStage.setTitle("Shooting Star [Main Menu] [alpha]");
        primaryStage.setScene(mainMenu.getScene());
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
