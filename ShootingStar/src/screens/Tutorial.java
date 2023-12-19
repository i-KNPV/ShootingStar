package screens;

import application.Sound;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

public class Tutorial {
    private Stage primaryStage;
    private Scene tutorialScene;
    private double highScore;

    private List<TutorialPage> tutorialPages;
    private int currentPageIndex;

    public Tutorial(Stage primaryStage, double highScore) {
        this.primaryStage = primaryStage;
        this.highScore = highScore;

        // Load the background image
        Image backgroundImage = new Image("assets/background/spacebg.gif");
        ImageView view_bg = new ImageView(backgroundImage);
        view_bg.setRotate(90);
        view_bg.setPreserveRatio(true);

        Font titleFont = Font.loadFont(getClass().getResourceAsStream("/assets/fonts/TitanOne-Regular.ttf"), 25);
        Font font = Font.loadFont(getClass().getResourceAsStream("/assets/fonts/TitanOne-Regular.ttf"), 15);
        
        // Create buttons for the tutorial screen
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> showMainMenu());

        Button nextButton = new Button("Next");
        nextButton.setOnAction(event -> showNextTutorial());

        Label overviewTitle = new Label("Overview");
        overviewTitle.setFont(titleFont);
        overviewTitle.setTextFill(Color.WHITE);

        // Initialize tutorial pages
        tutorialPages = new ArrayList<>();
        
        tutorialPages.add(new TutorialPage("Play as an unwavering meteor hurtling towards Earth with the goal of reaching the surface. Most meteoroids that go near the Earth never survive the descent, only becoming shooting stars that leave behind fleeting wondrous trails in the sky.", Collections.singletonList(new ImageWithPath("assets/sprites/star.png", 150, 150))));
        
        tutorialPages.add(new TutorialPage("However, you as the player are equipped with determination to defy the odds and make an impact that will shake the world to its core. With the world against you, do you have what it takes to transcend your fate as a mere shooting star?", Collections.singletonList(new ImageWithPath("assets/sprites/star.png", 150, 150))));
        
        tutorialPages.add(new TutorialPage("UP ARROW KEY -  Moves the star up\n"
            + "DOWN ARROW KEY - Moves the star down\n"
            + "RIGHT ARROW KEY - Up  Moves the star to the right\n"
            + "LEFT ARROW KEY - Moves the star to the left\n"
            + "SPACE - Activates item\n"
            + "ESC - Pauses the game",
            Arrays.asList(
                    new ImageWithPath("assets/tutorial/ArrowKeys.png", 200, 150),
                    new ImageWithPath("assets/tutorial/Space.png", 150, 100),
                    new ImageWithPath("assets/tutorial/Escape.png", 150, 100))));
        
        tutorialPages.add(new TutorialPage("OBJECTIVES:\n\n - To survive and reach the Earth’s surface\n"
        		+ " - To dodge obstacles and enemies hindering Bob’s descent\n"
        		+ " - To gather as much Shimmer scattered around the atmosphere as possible\n", 
        		Collections.singletonList(new ImageWithPath("assets/tutorial/Destroy.gif", 300, 80))));
        
        tutorialPages.add(new TutorialPage("OBSTACLES:\n\n - Wind - Pushes the star to one direction, making it harder to control.\n"
        		+ " - Bird - Decreases your vitality by 10.\n"
        		+ " - Rocket - Decreases your vitality by 50.\n", Collections.emptyList()));

        // Create content for the first page
        VBox contentPane = createPageContent(tutorialPages.get(0));

        // Layout for the tutorial screen
        VBox tutorialLayout = new VBox(10);
        tutorialLayout.setAlignment(Pos.CENTER);
        tutorialLayout.getChildren().addAll(overviewTitle, contentPane);

        // StackPane to layer background behind other elements
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(view_bg, tutorialLayout);

        this.tutorialScene = new Scene(stackPane, 600, 800);
        this.currentPageIndex = 0; // Initialize the current page index
    }

    public Scene getScene() {
        return tutorialScene;
    }

    private void showMainMenu() {
        System.out.println("Switching back to the Main Menu");
        MainMenu mainMenu = new MainMenu(primaryStage, 0, 0, true);
        primaryStage.setTitle("Shooting Star [Main Menu] [alpha]");
        primaryStage.setScene(mainMenu.getScene());
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

    private VBox createPageContent(TutorialPage page) {
        Font font = Font.loadFont(getClass().getResourceAsStream("/assets/fonts/TitanOne-Regular.ttf"), 18);

        // Create Text for tutorial text
        Text overview = new Text(page.getText());
        overview.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        overview.setFont(font);
        overview.setFill(Color.WHITE);
        overview.setWrappingWidth(400);

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

        // Create a VBox to stack the text and HBox vertically
        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(10));

        // Add the Text to the VBox
        contentBox.getChildren().add(overview);

        // Add the HBox containing images to the VBox
        contentBox.getChildren().add(imagesHBox);

        // Include buttons in the content
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> showMainMenu());

        Button nextButton = new Button("Next");
        nextButton.setOnAction(event -> showNextTutorial());

        // Add buttons to the VBox
        contentBox.getChildren().addAll(backButton, nextButton);

        return contentBox;
    }

    private static class TutorialPage {

        private String text;
        private List<ImageWithPath> imagesWithPath;

        public TutorialPage(String text, List<ImageWithPath> imagesWithPath) {
            this.text = text;
            this.imagesWithPath = imagesWithPath;
        }

        public String getText() {
            return text;
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
