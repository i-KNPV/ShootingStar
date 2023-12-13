package screens;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class GameOver {
    private Group root;
    private Scene scene;
    private Text gameOverText;

    public GameOver(double width, double height) {
        root = new Group();
        scene = new Scene(root, width, height, Color.BLACK);

        gameOverText = new Text("Game Over");
        gameOverText.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        gameOverText.setFill(Color.RED);
        gameOverText.setTextAlignment(TextAlignment.CENTER);
        gameOverText.setLayoutX(width / 2 - gameOverText.getLayoutBounds().getWidth() / 2);
        gameOverText.setLayoutY(height / 2);
        root.getChildren().add(gameOverText);
    }

    public Scene getScene() {
        return scene;
    }
}
