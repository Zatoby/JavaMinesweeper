// Tobias Hernandez Perez, 2CI

package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;

/**
 * Main class
 * starts the game and initializes the other classes
 *
 * @author Tobias Hernandez Perez
 */
public class Game extends Application {
    /**
     * path to the style file
     */
    private final Path file = Paths.get("resources/style.txt");
    /**
     * minutes seconds and milliseconds
     * (currently only secs are being used)
     */
    private final int mins = 0;
    private final int millis = 0;
    /**
     * Defines a controller class
     */
    public Controller controller;
    /**
     * Defines a settings class
     */
    public Settings settings;
    /**
     * Initializes a new difficulty class
     */
    public Difficulty difficulty = new Difficulty(this);
    /**
     * the number of rows the board has
     */
    public int rowLength;
    /**
     * the number of columns the board has
     */
    public int colLength;
    /**
     * two dimensional array containing the buttons of the board
     */
    public Button[][] b;
    /**
     * two dimensional array containing the bombs and corresponding integers around then
     */
    public int[][] intMap;
    /**
     * two dimensional array containing whether or not a tile has been clicked or not
     */
    public boolean[][] clickedMap;

    /**
     * amount of bombs on board
     */
    public int bombNum;
    /**
     * amount of bombs left
     */
    public int bombCounter = 0;
    /**
     * amount of clicked buttons
     */
    public int clickedButtons = 0;
    /**
     * amount of placed flags
     */
    public int flags = 0;

    /**
     * if the player is currently alive or not
     */
    public boolean isAlive = true;
    /**
     * whether the settings menu is open or not
     */
    public boolean isInSettings = false;
    /**
     * string map containing the color information of the game
     */
    public Map<String, String> style = new TreeMap<>();
    /**
     * amount of bombs left as label
     */
    public Label bombsLeftText;
    /**
     * timer icon
     */
    public ImageView timerImgView;
    /**
     * initializes win stage
     */
    public Stage winStage = new Stage();
    /**
     * control/top pane where you can choose the difficulty, see your time etc.
     */
    BorderPane controlPane = new BorderPane();
    /**
     * the difficulty of the game as string
     */
    String difficultyString = "Easy";
    /**
     * timer as text
     */
    Text timer;
    /**
     * timer timeline
     */
    Timeline timeline;
    /**
     * root stage
     */
    Stage mainStage;
    /**
     * the width of the game window
     */
    private int sceneWidth;
    /**
     * the height of the game window
     */
    private int sceneHeight;
    private int secs = 1;
    /**
     * high score of current difficulty as integer
     */
    private int highscore;

    /**
     * launches the game
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts an easy map
     */
    public void startEasy() {
        difficultyString = "Easy";
        start(new Stage());
    }

    /**
     * Starts a medium map
     */
    public void startMedium() {
        difficultyString = "Medium";
        start(new Stage());
    }

    /**
     * Starts a hard map
     */
    public void startHard() {
        difficultyString = "Hard";
        start(new Stage());
    }

    /**
     * Starts the program
     *
     * @param stage the interface
     */
    @Override
    public void start(Stage stage) {
        mainStage = stage;
        readStyle();

        setDifficulty();
        bombsLeftText = new Label("" + bombNum);

        BorderPane border = new BorderPane();
        GridPane gridPane = new GridPane();

        timer();

        BorderPane controlPane = setControlPane(stage);

        border.setTop(controlPane);
        border.setCenter(gridPane);

        controller = new Controller(this);
        settings = new Settings(this, controller);
        Generation generation = new Generation(this, controller);
        generation.generateGenerationMap(gridPane);

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("pics/flag.png")));
        stage.getIcons().add(icon);

        Scene scene = new Scene(border, sceneWidth, sceneHeight);

        stage.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ESCAPE && !isInSettings) {
                isInSettings = true;
                settings.settings.show();
            }
            if (event.getCode() == KeyCode.R) difficulty.restartGame(stage);
        });

        stage.setResizable(false);
        stage.setScene(scene);


        stage.show();

        stage.setOnCloseRequest(e -> System.exit(1));
    }

    /**
     * reads a document containing the style of the game
     */
    private void readStyle() {
        try {
            BufferedReader in = Files.newBufferedReader(file, StandardCharsets.UTF_8);
            String line = in.readLine();

            while (line != null) {
                if (line.trim().startsWith("#")) {
                    String key = line.substring(1).trim();
                    line = in.readLine();

                    style.put(key, line);
                }
                line = in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * writes changes to the style document
     */
    public void writeNewStyle() {
        try {
            BufferedWriter out = Files.newBufferedWriter(Paths.get("resources/style.txt"));
            StringBuilder data = new StringBuilder();

            Set<Entry<String, String>> styleSet = style.entrySet();
            for (Entry<String, String> stringStringEntry : styleSet) {
                String key = (String) ((Entry) stringStringEntry).getKey();
                String value = (String) ((Entry) stringStringEntry).getValue();

                data.append('#').append(key).append('\n').append(value).append('\n');
            }

            out.write(data.toString());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sets the timer
     */
    private void timer() {
        timer = new Text("000");
        timer.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> change(timer)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
    }

    /**
     * changes the timer text every second
     *
     * @param text changing text
     */
    void change(Text text) {
        /*if(millis == 1000) {
            secs++;
            millis = 0;
        }
         */
        /*if(secs == 60) {
            mins++;
            secs = 0;
        }*/
        text.setText((((secs / 100) == 0) ? "00" : "") + secs++);
        //(((mins/10) == 0) ? "0" : "") + mins + ":"
        //                +
        //+ ":"
        //+ (((millis/10) == 0) ? "00" : (((millis/100) == 0) ? "0" : "")) + millis++);

        //System.out.println(timer.getText());
    }

    /**
     * defines the attributes for the difficulties
     */
    public void setDifficulty() {
        int butSizeE = Integer.parseInt(style.get("Button Size Easy"));
        int butSizeM = Integer.parseInt(style.get("Button Size Medium"));
        int butSizeH = Integer.parseInt(style.get("Button Size Hard"));

        switch (difficultyString.toLowerCase(Locale.ROOT).trim()) {
            case "easy" -> {
                rowLength = 8;
                colLength = 10;
                bombNum = 10;
                sceneWidth = butSizeE * colLength;
                sceneHeight = butSizeE * rowLength + 55;
            }
            case "medium" -> {
                rowLength = 14;
                colLength = 18;
                bombNum = 40;
                sceneWidth = butSizeM * colLength;
                sceneHeight = butSizeM * rowLength + 55;
            }
            case "hard" -> {
                rowLength = 20;
                colLength = 25;
                bombNum = 99;
                sceneWidth = butSizeH * colLength;
                sceneHeight = butSizeH * rowLength + 55;
            }
            default -> throw new IllegalArgumentException("Error at: " + difficulty + " illegal difficulty.");
        }

        b = new Button[rowLength][colLength];
        intMap = new int[rowLength][colLength];
        clickedMap = new boolean[rowLength][colLength];
    }

    /**
     * sets the a pane containing the difficulty selection
     *
     * @return difficulty pane
     */
    public GridPane setDifficultyPane(Stage stage) {
        GridPane difficultyPane = new GridPane();
        double size = 25;


        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(size / 2, 0.0,
                size, size,
                0.0, size);

        Rectangle square = new Rectangle(size, size);

        Polygon pentagon = new Polygon();
        pentagon.getPoints().addAll(0.0, 0.0,
                size / 2, size / 2.75,
                size / 2.75, size,
                -size / 2.75, size,
                -size / 2, size / 2.75);

        triangle.setFill(Paint.valueOf("#1976d9"));
        square.setFill(Paint.valueOf("#fe8e3c"));
        pentagon.setFill(Paint.valueOf("#d32f2f"));

        triangle.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> difficulty.openEasy(stage));
        square.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> difficulty.openMedium(stage));
        pentagon.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> difficulty.openHard(stage));

        difficultyPane.setHgap(10);
        difficultyPane.setVgap(10);
        difficultyPane.setAlignment(Pos.CENTER);

        difficultyPane.add(triangle, 0, 0);
        difficultyPane.add(square, 1, 0);
        difficultyPane.add(pentagon, 2, 0);

        return difficultyPane;
    }

    /**
     * sets the control (top) pane
     *
     * @param stage interface
     * @return control pane
     */
    public BorderPane setControlPane(Stage stage) {
        controlPane = new BorderPane();
        GridPane difficultyPane = setDifficultyPane(stage);
        GridPane restartPane = setRestartPane(stage);
        GridPane infoPane = setInfoPane();

        controlPane.setLeft(difficultyPane);
        controlPane.setCenter(restartPane);
        controlPane.setRight(infoPane);

        controlPane.setPadding(new Insets(10));
        controlPane.setStyle(style.get("Control Pane Background"));

        return controlPane;
    }

    /**
     * sets the information pane containing the bomb counter and timer
     *
     * @return info pane
     */
    public GridPane setInfoPane() {
        GridPane infoPane = new GridPane();

        Image flagImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("pics/flag.png")));
        ImageView flagImgView = new ImageView(flagImg);
        flagImgView.setFitWidth(20);
        flagImgView.setFitHeight(20);
        bombsLeftText.setGraphic(flagImgView);
        bombsLeftText.setFont(Font.font("Roboto", FontWeight.BOLD, 15));

        infoPane.setHgap(10);
        infoPane.setVgap(10);
        infoPane.setAlignment(Pos.CENTER);

        Image timerImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("pics/stopwatch.png")));
        timerImgView = new ImageView(timerImg);
        timerImgView.setFitWidth(20);
        timerImgView.setFitHeight(20);

        infoPane.add(bombsLeftText, 0, 0);
        infoPane.add(timerImgView, 1, 0);
        infoPane.add(timer, 2, 0);


        int[] invertedColor = getInvertedBackground(style.get("Control Pane Background"));

        bombsLeftText.setStyle("-fx-text-fill: " + "rgb(" + Arrays.toString(invertedColor).substring(1, Arrays.toString(invertedColor).length() - 1) + ")");
        timer.setFill(Color.rgb(invertedColor[0], invertedColor[1], invertedColor[2]));

        return infoPane;
    }

    /**
     * sets a pane containing the restart button
     *
     * @return restart pane
     */
    public GridPane setRestartPane(Stage stage) {
        GridPane restartPane = new GridPane();
        int imgSize = 30;

        Button restart = restartButton(stage, imgSize);


        restartPane.setAlignment(Pos.CENTER);
        restartPane.setStyle("-fx-background-color: transparent;");

        restartPane.add(restart, 3, 0);

        return restartPane;
    }

    /**
     * makes a restart button
     *
     * @param stage   current stage
     * @param imgSize size of the button
     * @return a restart button
     */
    private Button restartButton(Stage stage, int imgSize) {
        Image restartImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("pics/restart.png")));
        ImageView restartImgView = new ImageView(restartImg);
        restartImgView.setFitWidth(imgSize);
        restartImgView.setFitHeight(imgSize);

        Button restart = new Button();
        restart.setGraphic(restartImgView);
        restart.setOnAction(e -> difficulty.restartGame(stage));
        restart.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
        restart.setPadding(new Insets(2));
        restart.setStyle("-fx-background-color: transparent; -fx-text-fill: #303030; -fx-border-color: transparent");
        return restart;
    }

    /**
     * defines what happens on a loss (when clicking on a bomb)
     * can´t click any more tiles or place flags
     * stops the timer
     *
     * @param clickedButton the bomb that was clicked
     */
    public void lossAlert(Button clickedButton) {
        isAlive = false;
        timeline.stop();
        timeline = null;

        clickedButton.setText("");

        for (int r = 0; r < intMap.length; r++) {
            for (int c = 0; c < intMap[r].length; c++) {
                int red = (int) (Math.random() * ((255 - 1) + 1));
                int green = (int) (Math.random() * ((255 - 1) + 1));
                int blue = (int) (Math.random() * ((255 - 1) + 1));

                if (intMap[r][c] == -1 && !b[r][c].getText().equals("F")) {
                    Button mine = b[r][c];
                    mine.setTextFill(Paint.valueOf("000"));
                    mine.setStyle("-fx-background-radius: 0; -fx-background-color: rgb(" + red + "," + green + "," + blue + ")");
                    mine.setGraphic(new Circle(Integer.parseInt(style.get("Button Size " + difficultyString)) >> 2,
                            Paint.valueOf("rgb(" + (red - 50) + "," + (green - 50) + "," + (blue - 50) + ")")));
                    mine.setPadding(new Insets(1, 1, 1, 1));
                }
            }
        }
    }

    /**
     * defines what happens on a win
     * when all tiles, except the bombs were clicked you win
     * stops the timer
     */
    public void winAlert() {
        isAlive = false;
        timeline.stop();

        try {
            int score = Integer.parseInt(timer.getText().replace(":", ""));

            if (difficultyString.equalsIgnoreCase("Easy")) setHighscoreEasy(score);
            if (difficultyString.equalsIgnoreCase("Medium")) setHighscoreMedium(score);
            if (difficultyString.equalsIgnoreCase("Hard")) setHighscoreHard(score);

            setWinStage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * makes a new stage on a win
     */
    private void setWinStage() {
        int width = 200;
        int height = 130;
        VBox vBox = new VBox();
        HBox row1 = new HBox();
        HBox row2 = new HBox();

        vBox.setStyle(style.get("Control Pane Background"));
        vBox.setPadding(new Insets(30));

        row1.setPadding(new Insets(10));

        Image timerImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("pics/stopwatch.png")));
        ImageView timerImgView = new ImageView(timerImg);
        timerImgView.setFitWidth(20);
        timerImgView.setFitHeight(20);

        Image trophyImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("pics/trophy.png")));
        ImageView trophyImgView = new ImageView(trophyImg);
        trophyImgView.setFitWidth(20);
        trophyImgView.setFitHeight(20);

        Text score = new Text(timer.getText());
        score.setFill(Paint.valueOf("fff"));
        score.setFont(Font.font("Roboto", FontWeight.BOLD, 14));


        Label highscoreLabel = new Label("" + highscore);
        highscoreLabel.setTextFill(Paint.valueOf("fff"));
        highscoreLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 14));


        row1.setStyle(style.get("Top Background"));
        row1.setSpacing(10);
        row1.getChildren().addAll(timerImgView, score, trophyImgView, highscoreLabel);

        row2.getChildren().addAll(new Label(""), restartButton(winStage, 20));

        row2.setSpacing(50);
        vBox.getChildren().addAll(row1, row2);

        Scene winScene = new Scene(vBox, width, height);

        winStage.getIcons().add(trophyImg);

        winStage.setResizable(false);
        winStage.setScene(winScene);
        winStage.show();
    }

    /**
     * writes the new highscore of the easy difficulty in the corresponding file
     *
     * @param score the current score
     */
    private void setHighscoreEasy(int score) throws IOException {
        BufferedReader highscoreIn = Files.newBufferedReader(Paths.get("resources/highscoreEasy.txt"));
        String line = highscoreIn.readLine();

        highscore = Integer.parseInt(line);

        if (score < highscore || highscore == 0) {
            BufferedWriter highscoreOut = Files.newBufferedWriter(Paths.get("resources/highscoreEasy.txt"));
            highscoreOut.write(score + "");
            highscoreOut.close();
        }
    }

    /**
     * writes the new highscore of the medium difficulty in the corresponding file
     *
     * @param score the current score
     */
    private void setHighscoreMedium(int score) throws IOException {
        BufferedReader highscoreIn = Files.newBufferedReader(Paths.get("resources/highscoreMedium.txt"));
        String line = highscoreIn.readLine();

        highscore = Integer.parseInt(line);

        if (score < highscore || highscore == 0) {
            BufferedWriter highscoreOut = Files.newBufferedWriter(Paths.get("resources/highscoreMedium.txt"));
            highscoreOut.write(score + "");
            highscoreOut.close();
        }
    }

    /**
     * writes the new highscore of the hard difficulty in the corresponding file
     *
     * @param score the current score
     */
    private void setHighscoreHard(int score) throws IOException {
        BufferedReader highscoreIn = Files.newBufferedReader(Paths.get("resources/highscoreHard.txt"));
        String line = highscoreIn.readLine();

        highscore = Integer.parseInt(line);

        if (score < highscore || highscore == 0) {
            BufferedWriter highscoreOut = Files.newBufferedWriter(Paths.get("resources/highscoreHard.txt"));
            highscoreOut.write(score + "");
            highscoreOut.close();
        }
    }

    /**
     * calculates the inverted color of the style
     * @param style color to be inverted
     * @return inverted color values as int array
     */
    public int[] getInvertedBackground(String style) {
        StringBuilder stripped = new StringBuilder();

        for (char ch : style.toCharArray()) {
            if (Character.isDigit(ch) || ch == ',' || ch == '.') stripped.append(ch);
        }

        String[] split = stripped.toString().split(",");

        int r = (int) (255 - Double.parseDouble(split[0]));
        int g = (int) (255 - Double.parseDouble(split[1]));
        int b = (int) (255 - Double.parseDouble(split[2]));

        return new int[]{r, g, b};
    }
}
