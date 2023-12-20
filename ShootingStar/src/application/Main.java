package application;

import javafx.application.Application;
import javafx.stage.Stage;
import screens.MainMenu;

public class Main extends Application {
    private Settings settings = new Settings();
	
	public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
    	// Initialize the music and sound effects settings
    	settings.setMusicMuted(false);
    	settings.setSfxMuted(false);
    	
    	// Create the main menu and push to primary stage
        MainMenu mainMenu = new MainMenu(primaryStage, 0, 0, false, settings);
        primaryStage.setScene(mainMenu.getScene());
        primaryStage.setTitle("Shooting Star [Main Menu][alpha]");
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
    }  
}
