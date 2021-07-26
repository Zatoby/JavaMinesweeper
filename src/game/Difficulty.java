package game;

import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.Locale;

public class Difficulty {
    /**
     * defines the game
     */
    private final Game game;

    /**
     * Constructor for Difficulty
     * @param game current Game
     */
    public Difficulty(Game game) {
        this.game = game;
    }

    /**
     * Restarts the game with the same difficulty
     */
    public void restartGame(Stage stage) {
        System.out.println( "Restarting..." );
        game.mainStage.close();
        if (!game.isAlive) game.mainStage.close();
        switch (game.difficultyString.toLowerCase(Locale.ROOT).trim()) {
            case "easy" -> Platform.runLater(() -> openEasy(stage));
            case "medium" -> Platform.runLater(() -> openMedium(stage));
            case "hard" -> Platform.runLater(() -> openHard(stage));
        }
        game.settings.settings.close();
        game.winStage.close();
    }

    /**
     * opens a board with easy difficulty (8 * 10 | 10 Bombs)
     */
    public void openEasy(Stage stage) {
        System.out.println( "Loading Easy..." );
        stage.close();
        Platform.runLater( () -> new Game().startEasy());
    }

    /**
     * opens a board with medium difficulty (14 * 18 | 40 Bombs)
     */
    public void openMedium(Stage stage) {
        System.out.println( "Loading Medium..." );
        stage.close();
        Platform.runLater( () -> new Game().startMedium());
    }

    /**
     * opens a board with hard difficulty (20 * 25 | 99 Bombs)
     */
    public void openHard(Stage stage) {
        System.out.println( "Loading Hard..." );
        stage.close();
        Platform.runLater( () -> new Game().startHard());
    }
}
