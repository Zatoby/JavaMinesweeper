package game;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Locale;

public class Difficulty {
    private Game game;
    private Button[][] b;

    public Difficulty(Game game) {
        this.game = game;
        this.b = game.b;
    }

    public void restartGame(Stage stage) {
        System.out.println( "Restarting..." );
        stage.close();
        switch (game.difficultyString.toLowerCase(Locale.ROOT).trim()) {
            case "easy":
                Platform.runLater( () -> openEasy(stage));
                break;
            case "medium":
                Platform.runLater( () -> openMedium(stage));
                break;
            case "hard":
                Platform.runLater( () -> openHard(stage));
                break;
        }
    }

    public void openEasy(Stage stage) {
        System.out.println( "Loading Easy..." );
        stage.close();
        Platform.runLater( () -> new Game().startEasy());
    }

    public void openMedium(Stage stage) {
        System.out.println( "Loading Medium..." );
        stage.close();
        Platform.runLater( () -> new Game().startMedium());
    }

    public void openHard(Stage stage) {
        System.out.println( "Loading Hard..." );
        stage.close();
        Platform.runLater( () -> new Game().startHard());
    }
}
