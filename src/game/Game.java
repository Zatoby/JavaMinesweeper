// Tobias Hernandez Perez, 2CI

package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Game extends Application {
    public Controller controller;
    public Difficulty difficulty = new Difficulty(this);

    BorderPane controlPane = new BorderPane();

    public int rowLength;
    public int colLength;

    int sceneWidth;
    int sceneHeight;

    public Button[][] b;
    public int[][] intMap;
    public boolean[][] clickedMap;

    public int bombNum;
    public int bombCounter = 0;
    public int clickedButtons = 0;
    public int flags = 0;
    public boolean isAlive = true;
    public boolean isInSettings = false;

    String difficultyString = "Easy";
    Path file = Paths.get("resources/style.txt");
    public Map<String, String> style = new TreeMap<>();
    private BufferedReader in;
    public BufferedWriter out;
    private String line;

    public Label bombsLeftText;

    Text timer;
    Timeline timeline;
    int mins = 0, secs = 0, millis = 0;

    int highscore;

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
     * @param stage the interface
     */
    @Override
    public void start(Stage stage) {
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
        Generation generation = new Generation(this, controller);
        generation.generateGenerationMap(gridPane, stage);

        //stage.getIcons().add(new Image("file:pics/easy.png"));

        Scene scene = new Scene(border, sceneWidth, sceneHeight);

        stage.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode()==KeyCode.ESCAPE) new Settings(event, this);
            if (event.getCode()==KeyCode.R) difficulty.restartGame(stage);
        });

        stage.setResizable(false);
        stage.setScene(scene);



        stage.show();
    }

    /**
     * reads a document containing the style of the game
     */
    private void readStyle() {
        try {
            in = Files.newBufferedReader(file, Charset.forName("UTF-8"));
            line = in.readLine();

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
            out = Files.newBufferedWriter(Paths.get("resources/style.txt"));
            String data = "";

            Set styleSet = style.entrySet();
            Iterator itr = styleSet.iterator();
            while(itr.hasNext()) {
                Map.Entry entry = (Map.Entry) itr.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();

                data += '#' + key + '\n' + value + '\n';
            }

            out.write(data);
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
        timer.setFill(Paint.valueOf("#fff"));
        timer.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> change(timer)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
    }

    /**
     * changes the timer text every second
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
        text.setText((((secs/100) == 0) ? "00" : "") + secs++);
        //(((mins/10) == 0) ? "0" : "") + mins + ":"
        //                +
        //+ ":"
        //+ (((millis/10) == 0) ? "00" : (((millis/100) == 0) ? "0" : "")) + millis++);
    }

    /**
     * defines the attributes for the difficulties
     */
    public void setDifficulty() {
        //PopUpFx.readLine("Difficulty Level: easy/medium/hard");
        int butSizeE = Integer.parseInt(style.get("Button Size Easy"));
        int butSizeM = Integer.parseInt(style.get("Button Size Medium"));
        int butSizeH = Integer.parseInt(style.get("Button Size Hard"));

        switch (difficultyString.toLowerCase(Locale.ROOT).trim()) {
            case "easy":
                rowLength = 8;
                colLength = 10;
                bombNum = 10;
                sceneWidth = butSizeE*colLength;
                sceneHeight = butSizeE*rowLength+55;
                break;
            case "medium":
                rowLength = 14;
                colLength = 18;
                bombNum = 40;
                sceneWidth = butSizeM*colLength;
                sceneHeight = butSizeM*rowLength+55;
                break;
            case "hard":
                rowLength = 20;
                colLength = 25;
                bombNum = 99;
                sceneWidth = butSizeH*colLength;
                sceneHeight = butSizeH*rowLength+55;
                break;
            default:
                throw new IllegalArgumentException("Error at: " + difficulty + " illegal difficulty.");
        }

        b = new Button[rowLength][colLength];
        intMap = new int[rowLength][colLength];
        clickedMap = new boolean[rowLength][colLength];
    }


    /**
     * sets the a pane containing the difficulty selection
     * @param stage
     * @return difficulty pane
     */
    public GridPane setDifficultyPane(Stage stage) {
        GridPane difficultyPane = new GridPane();
        double size = 25;


        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(new Double[]{
                size/2, 0.0,
                size, size,
                0.0, size,
        });

        Rectangle square = new Rectangle(size, size);

        Polygon pentagon = new Polygon();
        pentagon.getPoints().addAll(new Double[]{
                0.0, 0.0,
                size/2, size/2.75,
                size/2.75, size,
                -size/2.75, size,
                -size/2, size/2.75,
        });

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
     * @return info pane
     */
    public GridPane setInfoPane() {
        GridPane infoPane = new GridPane();

        Image flagImg = new Image(getClass().getResourceAsStream("pics/flag.png"));
        ImageView flagImgView = new ImageView(flagImg);
        flagImgView.setFitWidth(20);
        flagImgView.setFitHeight(20);
        bombsLeftText.setGraphic(flagImgView);
        bombsLeftText.setFont(Font.font("Roboto", FontWeight.BOLD, 15));

        infoPane.setHgap(10);
        infoPane.setVgap(10);
        infoPane.setAlignment(Pos.CENTER);

        Image timerImg = new Image(getClass().getResourceAsStream("pics/stopwatch.png"));
        ImageView timerImgView = new ImageView(timerImg);
        timerImgView.setFitWidth(20);
        timerImgView.setFitHeight(20);

        infoPane.add(bombsLeftText, 0, 0);
        infoPane.add(timerImgView, 1, 0);
        infoPane.add(timer, 2, 0);

        bombsLeftText.setStyle("-fx-text-fill: #fff");

        return infoPane;
    }

    /**
     * sets a pane containing the restart button
     * @param stage
     * @return restart pane
     */
    public GridPane setRestartPane(Stage stage) {
        GridPane restartPane = new GridPane();
        int imgSize = 30;

        Image restartImg = new Image(getClass().getResourceAsStream("pics/restart.png"));
        ImageView restartImgView = new ImageView(restartImg);
        restartImgView.setFitWidth(imgSize);
        restartImgView.setFitHeight(imgSize);

        Button restart = new Button();
        restart.setGraphic(restartImgView);
        restart.setOnAction(e -> difficulty.restartGame(stage));
        restart.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
        restart.setPadding(new Insets(2));
        restart.setStyle("-fx-background-color: transparent; -fx-text-fill: #303030; -fx-border-color: transparent");


        restartPane.setAlignment(Pos.CENTER);
        restartPane.setStyle("-fx-background-color: transparent;");

        restartPane.add(restart, 3, 0);

        return restartPane;
    }


    /**
     * defines what happens on a loss (when clicking on a bomb)
     * can´t click any more tiles or place flags
     * stops the timer
     * @param clickedButton the bomb that was clicked
     */
    public void lossAlert(Button clickedButton) {
        isAlive = false;
        timeline.stop();
        clickedButton.setText("");
        //Alert lossAlert = new Alert(Alert.AlertType.CONFIRMATION);
        //lossAlert.setContentText("You lost! \nWappler");
        //lossAlert.showAndWait();
        //clickedButton.setTextFill(Paint.valueOf("ff0000"));

        for (int r = 0; r < intMap.length; r++) {
            for (int c = 0; c < intMap[r].length; c++) {
                int red = (int)(Math.random() * ((255 - 1) + 1));
                int green = (int)(Math.random() * ((255 - 1) + 1));
                int blue = (int)(Math.random() * ((255 - 1) + 1));

                if (intMap[r][c] == -1 && b[r][c].getText()!="F") {
                    Button mine = b[r][c];
                    mine.setTextFill(Paint.valueOf("000"));
                    mine.setStyle("-fx-background-radius: 0; -fx-background-color: rgb(" + red + "," + green + "," + blue + ")");
                    mine.setGraphic(new Circle(Integer.parseInt(style.get("Button Size "+difficultyString))/4,
                            Paint.valueOf("rgb(" + (red-50) + "," + (green-50) + "," + (blue-50) + ")")));
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
        int size = 100;
        isAlive = false;
        timeline.stop();
        Alert winAlert = new Alert(Alert.AlertType.CONFIRMATION);


        try {
            int score = Integer.parseInt(timer.getText().replace(":", ""));

            if (difficultyString.equalsIgnoreCase("Easy")) setHighscoreEasy(score);;
            if (difficultyString.equalsIgnoreCase("Medium")) setHighscoreMedium(score);
            if (difficultyString.equalsIgnoreCase("Hard")) setHighscoreHard(score);


            setWinStage(size);

            /*
            winAlert.setContentText("YOU WON!\n" +
                    "Your Time: " + score + "\n" +
                    "Your Best: " + highscore);
            winAlert.showAndWait();
             */
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setWinStage(int size) {
        Stage winStage = new Stage();
        HBox box = new HBox();

        box.setPadding(new Insets(10));

        Image timerImg = new Image(getClass().getResourceAsStream("pics/stopwatch.png"));
        ImageView timerImgView = new ImageView(timerImg);
        timerImgView.setFitWidth(20);
        timerImgView.setFitHeight(20);

        Text score = new Text(timer.getText());
        score.setFill(Paint.valueOf("000"));
        score.setFont(Font.font("Roboto", FontWeight.BOLD, 14));


        Label highscoreLabel = new Label("" + highscore);
        highscoreLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 14));


        box.setStyle(style.get("Top Background"));
        box.setSpacing(10);
        box.getChildren().addAll(timerImgView, score, highscoreLabel);

        Scene winScene = new Scene(box, size, size);
        winStage.setScene(winScene);
        winStage.show();
    }

    /**
     * writes the new highscore of the easy difficulty in the corresponding file
     * @param score the current score
     * @throws IOException
     */
    private void setHighscoreEasy(int score) throws IOException {
        BufferedReader highscoreIn = Files.newBufferedReader(Paths.get("resources/highscoreEasy.txt"));
        String line = highscoreIn.readLine();

        highscore = Integer.parseInt(line);

        if (score < highscore || highscore ==0) {
            BufferedWriter highscoreOut = Files.newBufferedWriter(Paths.get("resources/highscoreEasy.txt"));
            highscoreOut.write(score+"");
            highscoreOut.close();
        }
    }

    /**
     * writes the new highscore of the medium difficulty in the corresponding file
     * @param score the current score
     * @throws IOException
     */
    private void setHighscoreMedium(int score) throws IOException {
        BufferedReader highscoreIn = Files.newBufferedReader(Paths.get("resources/highscoreMedium.txt"));
        String line = highscoreIn.readLine();

        highscore = Integer.parseInt(line);

        if (score < highscore || highscore ==0) {
            BufferedWriter highscoreOut = Files.newBufferedWriter(Paths.get("resources/highscoreMedium.txt"));
            highscoreOut.write(score+"");
            highscoreOut.close();
        }
    }

    /**
     * writes the new highscore of the hard difficulty in the corresponding file
     * @param score the current score
     * @throws IOException
     */
    private void setHighscoreHard(int score) throws IOException {
        BufferedReader highscoreIn = Files.newBufferedReader(Paths.get("resources/highscoreHard.txt"));
        String line = highscoreIn.readLine();

        highscore = Integer.parseInt(line);

        if (score < highscore || highscore ==0) {
            BufferedWriter highscoreOut = Files.newBufferedWriter(Paths.get("resources/highscoreHard.txt"));
            highscoreOut.write(score+"");
            highscoreOut.close();
        }
    }


    /**
     * launches the game
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
