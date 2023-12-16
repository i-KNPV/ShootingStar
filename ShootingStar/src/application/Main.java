package application;

import javafx.application.Application;
import javafx.stage.Stage;
import screens.MainMenu;

public class Main extends Application {	
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        MainMenu mainMenu = new MainMenu(primaryStage, 0);
        primaryStage.setScene(mainMenu.getScene());
        primaryStage.setTitle("Shooting Star [Main Menu][alpha]");
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
    }  
}
