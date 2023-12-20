package screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;

public class Tutorial {
    private Stage primaryStage;
    private Scene tutorialScene;
    private MainMenu menu;

    private List<TutorialPage> tutorialPages;
    private int currentPageIndex;

    public Tutorial(Stage primaryStage, MainMenu mainMenu) {
    	this.menu = mainMenu;
        this.primaryStage = primaryStage;
        

        // Load the background image
        Image backgroundImage = new Image("assets/background/spacebg.gif");
        ImageView view_bg = new ImageView(backgroundImage);
        view_bg.setRotate(90);
        view_bg.setPreserveRatio(true);

        // Initialize tutorial pages
        tutorialPages = new ArrayList<>();
        tutorialPages.add(new TutorialPage(Collections.singletonList(
                new ImageWithPath("assets/tutorial/1.png", 600, 800))));
        tutorialPages.add(new TutorialPage(Collections.singletonList(
                new ImageWithPath("assets/tutorial/2.png", 600, 800))));
        tutorialPages.add(new TutorialPage(Arrays.asList(
                new ImageWithPath("assets/tutorial/3.png", 600, 800))));
        tutorialPages.add(new TutorialPage(Collections.singletonList(
                new ImageWithPath("assets/tutorial/4.png", 600, 800))));
        tutorialPages.add(new TutorialPage(Collections.singletonList(
                new ImageWithPath("assets/tutorial/5.png", 600, 800))));

        // Create content for the first page
        VBox contentPane = createPageContent(tutorialPages.get(0));

        // Layout for the tutorial screen
        VBox tutorialLayout = new VBox(10);
        tutorialLayout.setAlignment(Pos.CENTER);
        tutorialLayout.getChildren().addAll(contentPane);

        // StackPane to layer background behind other elements
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(view_bg, tutorialLayout);

        this.tutorialScene = new Scene(stackPane, 600, 800);
        this.currentPageIndex = 0; // Initialize the current page index

        // Set up key event handling
        tutorialScene.setOnKeyPressed(event -> handleKeyPress(event.getCode()));
    }

    public Scene getScene() {
        return tutorialScene;
    }

    private void showMainMenu() {
        System.out.println("Switching back to the Main Menu");
        menu.showChildren();
        primaryStage.setTitle("Shooting Star [Main Menu] [alpha]");
        primaryStage.setScene(menu.getScene());
    }

    private void showNextTutorial() {
        currentPageIndex++;
        if (currentPageIndex < tutorialPages.size()) {
            // Create content for the next page
            VBox contentPane = createPageContent(tutorialPages.get(currentPageIndex));

            // StackPane is the root of your scene, so directly set the content
            StackPane stackPane = (StackPane) tutorialScene.getRoot();
            stackPane.getChildren().set(1, contentPane);
        } else {
            // If there are no more pages, return to the main menu
            showMainMenu();
        }
    }

    private void showPreviousTutorial() {
        currentPageIndex--;
        if (currentPageIndex >= 0) {
            // Create content for the previous page
            VBox contentPane = createPageContent(tutorialPages.get(currentPageIndex));

            // StackPane is the root of your scene, so directly set the content
            StackPane stackPane = (StackPane) tutorialScene.getRoot();
            stackPane.getChildren().set(1, contentPane);
        } else {
            // If trying to go back from the first page, do nothing or handle as needed
            currentPageIndex = 0;
        }
    }

    private VBox createPageContent(TutorialPage page) {
        // Create ImageViews for tutorial images
        List<ImageWithPath> imagesWithPath = page.getImagesWithPath();
        List<ImageView> imageViews = new ArrayList<>();

        for (ImageWithPath imageWithPath : imagesWithPath) {
            Image image = new Image(imageWithPath.getImagePath());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(imageWithPath.getWidth()); // Set the desired width
            imageView.setFitHeight(imageWithPath.getHeight()); // Set the desired height
            imageViews.add(imageView);
        }

        // Create an HBox to arrange the images horizontally
        HBox imagesHBox = new HBox(10);
        imagesHBox.setAlignment(Pos.CENTER);
        imagesHBox.getChildren().addAll(imageViews);

        // Create a VBox to stack the buttonsHBox and imagesHBox vertically
        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(10));

        // Add the HBox containing buttons to the VBox
        contentBox.getChildren().add(imagesHBox);

        return contentBox;
    }
    
    private void handleKeyPress(KeyCode code) {
        switch (code) {
            case LEFT:
                showPreviousTutorial();
                break;
            case RIGHT:
                showNextTutorial();
                break;
		default:
			break;
        }
    }

    private static class TutorialPage {

        private List<ImageWithPath> imagesWithPath;

        public TutorialPage(List<ImageWithPath> imagesWithPath) {
            this.imagesWithPath = imagesWithPath;
        }

        public List<ImageWithPath> getImagesWithPath() {
            return imagesWithPath;
        }
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
