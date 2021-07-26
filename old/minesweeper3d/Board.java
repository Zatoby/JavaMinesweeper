// Tobias Hernandez Perez, 2CI

package minesweeper3d;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Locale;

public class Board extends Application {
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

    String difficulty = "easy";

    public Label bombsLeftText ;

    Text timer;
    Timeline timeline;
    int mins = 0, secs = 0, millis = 0;

    public void startEasy() {
        difficulty = "easy";
        start(new Stage());
    }

    public void startMedium() {
        difficulty = "medium";
        start(new Stage());
    }

    public void startHard() {
        difficulty = "hard";
        start(new Stage());
    }

    @Override
    public void start(Stage stage) {
        setDifficulty();
        bombsLeftText = new Label("" + bombNum);

        BorderPane border = new BorderPane();
        GridPane gridPane = new GridPane();

        timer = new Text("00:00");
        timer.setFill(Paint.valueOf("#fff"));
        timer.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                change(timer);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);


        BorderPane controlPane = setControlPane(stage);

        border.setTop(controlPane);
        border.setCenter(gridPane);

        generateGenerationMap(gridPane, stage);

        stage.getIcons().add(new Image("file:pics/easy.png"));

        Scene scene = new Scene(border, sceneWidth, sceneHeight);
        stage.setResizable(false);
        stage.setScene(scene);

        scene.setOnKeyPressed(event -> {
            String codeString = event.getCode().toString();
            System.out.println(codeString);
        });


        stage.show();
        stage.getIcons().add(new Image("file:restart.png"));
    }

    public void setDifficulty() {
        //PopUpFx.readLine("Difficulty Level: easy/medium/hard");
        switch (difficulty.toLowerCase(Locale.ROOT).trim()) {
            case "easy":
                rowLength = 8;
                colLength = 10;
                bombNum = 10;
                sceneWidth = 400;
                sceneHeight = 375;
                break;
            case "medium":
                rowLength = 14;
                colLength = 18;
                bombNum = 40;
                sceneWidth = 540;
                sceneHeight = 475;
                break;
            case "hard":
                rowLength = 20;
                colLength = 25;
                bombNum = 99;
                sceneWidth = 750;
                sceneHeight = 655;
                break;
            default:
                //throw new IllegalArgumentException("Error at input: " + difficulty + " Only easy/medium/hard are allowed.");
                break;
        }

        b = new Button[rowLength][colLength];
        intMap = new int[rowLength][colLength];
        clickedMap = new boolean[rowLength][colLength];
    }

    void change(Text text) {
        /*if(millis == 1000) {
            secs++;
            millis = 0;
        }
         */
        if(secs == 60) {
            mins++;
            secs = 0;
        }
        text.setText((((mins/10) == 0) ? "0" : "") + mins + ":"
                + (((secs/10) == 0) ? "0" : "") + secs++);
                //+ ":"
                //+ (((millis/10) == 0) ? "00" : (((millis/100) == 0) ? "0" : "")) + millis++);
    }

    public BorderPane setControlPane(Stage stage) {
        BorderPane controlPane = new BorderPane();
        GridPane difficultyPane = setDifficultyPane(stage);
        GridPane restartPane = setRestartPane(stage);
        GridPane infoPane = setInfoPane();

        controlPane.setLeft(difficultyPane);
        controlPane.setCenter(restartPane);
        controlPane.setRight(infoPane);

        controlPane.setPadding(new Insets(10));
        controlPane.setStyle("-fx-background-color: #4a752c;");


        return controlPane;
    }

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

    public GridPane setDifficultyPane(Stage stage) {
        GridPane difficultyPane = new GridPane();
        int imgSize = 20;

        Image easyImg = new Image(getClass().getResourceAsStream("pics/difficulty/easy.png"));
        ImageView easyImgView = new ImageView(easyImg);
        easyImgView.setFitWidth(imgSize);
        easyImgView.setFitHeight(imgSize);

        Image mediumImg = new Image(getClass().getResourceAsStream("pics/difficulty/medium.png"));
        ImageView mediumImgView = new ImageView(mediumImg);
        mediumImgView.setFitWidth(imgSize);
        mediumImgView.setFitHeight(imgSize);

        Image hardImg = new Image(getClass().getResourceAsStream("pics/difficulty/hard.png"));
        ImageView hardImgView = new ImageView(hardImg);
        hardImgView.setFitWidth(imgSize);
        hardImgView.setFitHeight(imgSize);


        Button easyBut = new Button("");
        easyBut.setGraphic(easyImgView);
        easyBut.setOnAction(e -> openEasy(stage));
        easyBut.setStyle("-fx-background-color: #4a752c; -fx-border-color: transparent");

        Button mediumBut = new Button("");
        mediumBut.setGraphic(mediumImgView);
        mediumBut.setOnAction(e -> openMedium(stage));
        mediumBut.setStyle("-fx-background-color: #4a752c; -fx-border-color: transparent");

        Button hardBut = new Button("");
        hardBut.setGraphic(hardImgView);
        hardBut.setOnAction(e -> openHard(stage));
        hardBut.setStyle("-fx-background-color: #4a752c; -fx-border-color: transparent");


        difficultyPane.setHgap(1);
        difficultyPane.setVgap(1);
        difficultyPane.setAlignment(Pos.CENTER);


        difficultyPane.add(easyBut, 0, 0);
        difficultyPane.add(mediumBut, 1, 0);
        difficultyPane.add(hardBut, 2, 0);

        return difficultyPane;
    }

    public GridPane setRestartPane(Stage stage) {
        GridPane restartPane = new GridPane();
        int imgSize = 30;

        Image restartImg = new Image(getClass().getResourceAsStream("pics/restart.png"));
        ImageView restartImgView = new ImageView(restartImg);
        restartImgView.setFitWidth(imgSize);
        restartImgView.setFitHeight(imgSize);

        Button restart = new Button();
        restart.setGraphic(restartImgView);
        restart.setOnAction(e -> restartGame(stage));
        restart.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
        restart.setPadding(new Insets(2));
        restart.setStyle("-fx-background-color: #4a752c; -fx-text-fill: #303030; -fx-border-color: transparent");


        restartPane.setAlignment(Pos.CENTER);
        restartPane.setStyle("-fx-background-color: #4a752c;");

        restartPane.add(restart, 3, 0);

        return restartPane;
    }

    public void restartGame(Stage stage) {
        System.out.println( "Restarting..." );
        stage.close();
        switch (difficulty.toLowerCase(Locale.ROOT).trim()) {
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
        Platform.runLater( () -> new Board().startEasy());
    }

    public void openMedium(Stage stage) {
        System.out.println( "Loading Medium..." );
        stage.close();
        Platform.runLater( () -> new Board().startMedium());
    }

    public void openHard(Stage stage) {
        System.out.println( "Loading Hard..." );
        stage.close();
        Platform.runLater( () -> new Board().startHard());
    }


    public void generateGenerationMap(GridPane gridPane, Stage stage) {
        for (int row = 0; row < intMap.length; row++) {
            for (int col = 0; col < intMap[row].length; col++) {
                b[row][col] = new Button();
                Button currBut = b[row][col];
                if (difficulty.equals("easy")) {
                    currBut.setMinSize(40, 40);
                } else {
                    currBut.setMinSize(30, 30);
                }

                if ((row+col) % 2 == 0) {
                    currBut.setStyle("-fx-background-radius: 0; -fx-background-color: #aad751");
                } else {
                    currBut.setStyle("-fx-background-radius: 0; -fx-background-color: #a2d149");
                }

                int finalRow = row;
                int finalCol = col;

                currBut.setOnMouseClicked(e -> generateIntMap(gridPane, stage, finalRow, finalCol));
                gridPane.add(currBut, col, row);
            }
        }
    }

    public boolean isSafe(int randRow, int randCol, int safeRow, int safeCol) {
        int left = safeCol+1;
        int right = safeCol-1;
        int top = safeRow-1;
        int bottom = safeRow+1;

        if (randRow == safeRow && randCol == safeCol) return false;
        if (randRow == safeRow && randCol == right) return false;
        if (randRow == safeRow && randCol == left) return false;
        if (randRow == top && randCol == safeCol) return false;
        if (randRow == bottom && randCol == safeCol) return false;
        if (randRow == top && randCol == right) return false;
        if (randRow == top && randCol == left) return false;
        if (randRow == bottom && randCol == right) return false;
        if (randRow == bottom && randCol == left) return false;

        return true;
    }

    public void generateIntMap(GridPane gridPane, Stage stage, int safeRow, int safeCol) {
        timeline.play();

        while (bombCounter != bombNum) {
            int randRow = (int)(Math.random() * ((rowLength - 1) + 1));
            int randCol = (int)(Math.random() * ((colLength - 1) + 1));

            if (intMap[randRow][randCol] != -1 && isSafe(randRow, randCol, safeRow, safeCol)) {
                intMap[randRow][randCol] = -1;
                addNums(randRow, randCol);
            }
        }

        //outputs intmap for debuging purposes
        for (int row = 0; row < intMap.length; row++) {
            System.out.println();
            for (int col = 0; col < intMap[row].length; col++) {
                System.out.print(intMap[row][col] + "  ");
            }
        }

        generateButtonMap(gridPane);

        Button clickedButton = b[safeRow][safeCol];
        int buttonNum = intMap[safeRow][safeCol];

        //if (buttonNum != 0) clickedButton.setText("" + buttonNum);
        showClickedButton(clickedButton, safeRow, safeCol, buttonNum);
        //clickedButton.setOnMouseClicked(null);
        /*if ((safeRow + safeCol) % 2 == 0) {
            clickedButton.setStyle("-fx-background-radius: 0; -fx-background-color: #e5c29f");
        } else {
            clickedButton.setStyle("-fx-background-radius: 0; -fx-background-color: #d7b899");
        }
         */




        clickedButton.setFont(Font.font("Roboto", FontWeight.BOLD, 15));
        clickedMap[safeRow][safeCol] = true;
        //b[safeRow][safeCol].setOnKeyReleased(e -> showButtonText(b[safeRow][safeCol], safeRow, safeCol, stage, e));
    }



    public void addNums(int row, int col) {
        bombCounter++;

        int right = col + 1;
        int left = col - 1;
        int bot = row + 1;
        int top = row - 1;

        if (right < colLength && intMap[row][right] != -1) intMap[row][right]++; //right
        if (left >= 0 && intMap[row][left] != -1) intMap[row][left]++; //left
        if (right < colLength && bot < rowLength && intMap[bot][right] != -1) intMap[bot][right]++; //bottom right
        if (right < colLength && top >= 0 &&  intMap[top][right] != -1) intMap[top][right]++; //top right
        if (left >= 0 && bot < rowLength && intMap[bot][left] != -1) intMap[bot][left]++; //bottom left
        if (left >= 0 && top >= 0 && intMap[top][left] != -1) intMap[top][left]++; //top left
        if (bot < rowLength && intMap[bot][col] != -1) intMap[bot][col]++; //bottom
        if (top >= 0 && intMap[top][col] != -1) intMap[top][col]++; //top
    }

    public void generateButtonMap(GridPane gridPane) {
        for (int row = 0; row < intMap.length; row++) {
            for (int col = 0; col < intMap[row].length; col++) {
                b[row][col] = new Button();
                Button currBut = b[row][col];
                if (difficulty.equals("easy")) {
                    currBut.setMinSize(40, 40);
                } else {
                    currBut.setMinSize(30, 30);
                }

                if ((row+col) % 2 == 0) {
                    currBut.setStyle("-fx-background-radius: 0; -fx-background-color: #aad751");
                } else {
                    currBut.setStyle("-fx-background-radius: 0; -fx-background-color: #a2d149");
                }

                int finalRow = row;
                int finalCol = col;
                //b[row][col].setOnAction(e -> b[finalRow][finalCol].setText(""+intMap[finalRow][finalCol]));
                //currBut.setOnMouseClicked(e -> gameController(b[finalRow][finalCol], finalRow, finalCol, e));
                currBut.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> gameController(b[finalRow][finalCol], finalRow, finalCol, e));


                gridPane.add(currBut, col, row);
            }
        }
    }

    public void gameController(Button clickedButton, int row, int col, MouseEvent e) {
        //clickedButton.getOnMouseClicked().equals(null)
        MouseButton button = e.getButton();
        int buttonNum = intMap[row][col];

        if (!isAlive || (clickedButton.getText().equals("F") && button==MouseButton.PRIMARY)) return;


        if ((clickedMap[row][col] && !clickedButton.getText().equals("F") && intMap[row][col]!=0 && button==MouseButton.MIDDLE)
                || (e.isPrimaryButtonDown() && e.isSecondaryButtonDown())) {
            showButtonMiddleMouse(row, col);
            return;
        }

        if (placeFlag(clickedButton, button, row, col)) return;

        if (!clickedMap[row][col] && !clickedButton.getText().equals("F") && button==MouseButton.PRIMARY) {
            showClickedButton(clickedButton, row, col, buttonNum);
        }

        /*if (intMap[row][col] == 0 && row >= 0 && row < rowLength && col >= 0 && col < colLength) {
            //showButtonTextOnZero(b, clickedButton, row, col);


            int right = col+1;
            int left = col-1;
            int bot = row+1;
            int top = row-1;

            if (right < colLength && intMap[row][right] != -1) showButtonText(b, b[row][right], row, right); //right
            if (left >= 0 && intMap[row][left] != -1) showButtonText(b, b[row][left], row, left); //left
            showButtonText(b, b[bot][right], bot, right); //bottom right
            showButtonText(b, b[top][right], top, right); //top right
            showButtonText(b, b[bot][left], bot, left); //bottom left
            showButtonText(b, b[top][left], top, left); //top left
            showButtonText(b, b[bot][col], bot, col); //bottom


            /*




            if (right < colLength && bot < rowLength && intMap[bot][right] != -1)

            if (right < colLength && top >= 0 &&  intMap[top][right] != -1)

            if (left >= 0 && bot < rowLength && intMap[bot][left] != -1)

            if (left >= 0 && top >= 0 && intMap[top][left] != -1)

            if (top >= 0 && intMap[top][col] != -1)

            if (bot < rowLength && intMap[bot][col] != -1)


        }*/
    }

    public void showClickedButton(Button clickedButton, int row, int col, int buttonNum) {
        clickedButtons++;
        clickedMap[row][col] = true;


        int right = col+1;
        int left = col-1;
        int bot = row+1;
        int top = row-1;
        if (intMap[row][col] == 0) {
            if (right < colLength && !clickedMap[row][right] && !b[row][right].getText().equals("F")) showClickedButton(b[row][right], row, right, intMap[row][right]);
            if (left >= 0 && !clickedMap[row][left] && !b[row][left].getText().equals("F")) showClickedButton(b[row][left], row, left, intMap[row][left]);
            if (top >= 0 && !clickedMap[top][col] && !b[top][col].getText().equals("F")) showClickedButton(b[top][col], top, col, intMap[top][col]);
            if (bot < rowLength && !clickedMap[bot][col] && !b[bot][col].getText().equals("F")) showClickedButton(b[bot][col], bot, col, intMap[bot][col]);
            if (top >= 0 && right < colLength && !clickedMap[top][right] && !b[top][right].getText().equals("F")) showClickedButton(b[top][right], top, right, intMap[top][right]);
            if (top >= 0 && left >= 0 && !clickedMap[top][left] && !b[top][left].getText().equals("F")) showClickedButton(b[top][left], top, left, intMap[top][left]);
            if (bot < rowLength && right < colLength && !clickedMap[bot][right] && !b[bot][right].getText().equals("F")) showClickedButton(b[bot][right], bot, right, intMap[bot][right]);
            if (bot < rowLength && left >= 0 && !clickedMap[bot][left] && !b[bot][left].getText().equals("F")) showClickedButton(b[bot][left], bot, left, intMap[bot][left]);
        }

        if (buttonNum != 0) clickedButton.setText(""+ buttonNum);
        //clickedButton.setOnMouseClicked(null);
        clickedButton.setFont(Font.font("Roboto", FontWeight.BOLD, 15));
        if ((row + col)%2==0) {
            clickedButton.setStyle("-fx-background-radius: 0; -fx-background-color: #e5c29f");
        } else {
            clickedButton.setStyle("-fx-background-radius: 0; -fx-background-color: #d7b899");
        }
        switch (buttonNum) {
            case 1:
                clickedButton.setTextFill(Paint.valueOf("#1976d2"));
                break;
            case 2:
                clickedButton.setTextFill(Paint.valueOf("#388e3c"));
                break;
            case 3:
                clickedButton.setTextFill(Paint.valueOf("#d32f2f"));
                break;
            case 4:
                clickedButton.setTextFill(Paint.valueOf("#7b1fa2"));
                break;
            case 5:
                clickedButton.setTextFill(Paint.valueOf("#ff8f00"));
                break;
            case 6:
                clickedButton.setTextFill(Paint.valueOf("#008081"));
                break;
            case 7:
                clickedButton.setTextFill(Paint.valueOf("#424242"));
                break;
            case 8:
                clickedButton.setTextFill(Paint.valueOf("#808080"));
                break;
            default:
                break;
        }

        if (buttonNum == -1) {
            lossAlert(clickedButton);
        }
        //System.out.println(clickedButtons);

        winAlert();
    }

    public void showButtonMiddleMouse(int row, int col) {
        int right = col+1;
        int left = col-1;
        int bot = row+1;
        int top = row-1;
        int surroundingFlags = 0;

        if (right < colLength && b[row][right].getText().equals("F")) surroundingFlags++;
        if (left >= 0 && b[row][left].getText().equals("F")) surroundingFlags++;
        if (top >= 0 && b[top][col].getText().equals("F")) surroundingFlags++;
        if (bot < rowLength && b[bot][col].getText().equals("F")) surroundingFlags++;
        if (top >= 0 && right < colLength && b[top][right].getText().equals("F")) surroundingFlags++;
        if (top >= 0 && left >= 0 && b[top][left].getText().equals("F")) surroundingFlags++;
        if (bot < rowLength && right < colLength && b[bot][right].getText().equals("F")) surroundingFlags++;
        if (bot < rowLength && left >= 0 && b[bot][left].getText().equals("F")) surroundingFlags++;

        if (intMap[row][col] == surroundingFlags){
            if (right < colLength && !clickedMap[row][right] && !b[row][right].getText().equals("F")) showClickedButton(b[row][right], row, right, intMap[row][right]);
            if (left >= 0 && !clickedMap[row][left] && !b[row][left].getText().equals("F")) showClickedButton(b[row][left], row, left, intMap[row][left]);
            if (top >= 0 && !clickedMap[top][col] && !b[top][col].getText().equals("F")) showClickedButton(b[top][col], top, col, intMap[top][col]);
            if (bot < rowLength && !clickedMap[bot][col] && !b[bot][col].getText().equals("F")) showClickedButton(b[bot][col], bot, col, intMap[bot][col]);
            if (top >= 0 && right < colLength && !clickedMap[top][right] && !b[top][right].getText().equals("F")) showClickedButton(b[top][right], top, right, intMap[top][right]);
            if (top >= 0 && left >= 0 && !clickedMap[top][left] && !b[top][left].getText().equals("F")) showClickedButton(b[top][left], top, left, intMap[top][left]);
            if (bot < rowLength && right < colLength && !clickedMap[bot][right] && !b[bot][right].getText().equals("F")) showClickedButton(b[bot][right], bot, right, intMap[bot][right]);
            if (bot < rowLength && left >= 0 && !clickedMap[bot][left] && !b[bot][left].getText().equals("F")) showClickedButton(b[bot][left], bot, left, intMap[bot][left]);
        }
    }

    public boolean placeFlag(Button clickedButton, MouseButton button, int row, int col) {
        Image flagImg = new Image(getClass().getResourceAsStream("pics/flag.png"));
        ImageView flagImgView = new ImageView(flagImg);
        flagImgView.setFitWidth(20);
        flagImgView.setFitHeight(20);

        if(button==MouseButton.SECONDARY) {
            if (clickedButton.getText().equals("F")) {
                flags--;
                clickedButton.setText("");
                clickedButton.setGraphic(null);
                bombsLeftText.setText("" + (bombNum-flags));
            } else if (!clickedMap[row][col]){
                flags++;
                clickedButton.setGraphic(flagImgView);
                clickedButton.setText("F");
                bombsLeftText.setText("" + (bombNum-flags));
                //bombsLeft.setText(""+(bombNum-flags));
            }

            return true;
        }
        return false;
    }

    public void lossAlert(Button clickedButton) {
        isAlive = false;
        timeline.stop();
        clickedButton.setText("X");
        Alert lossAlert = new Alert(Alert.AlertType.CONFIRMATION);
        lossAlert.setContentText("You lost! \nWappler");
        lossAlert.showAndWait();
        clickedButton.setTextFill(Paint.valueOf("ff0000"));

        for (int r = 0; r < intMap.length; r++) {
            for (int c = 0; c < intMap[r].length; c++) {
                if (intMap[r][c] == -1) {
                    Button mine = b[r][c];
                    mine.setText("o");
                    mine.setTextFill(Paint.valueOf("000"));
                    mine.setStyle("-fx-background-radius: 0; -fx-background-color: #f00");
                    mine.setPadding(new Insets(0, 0, 4, 0));
                }
                else {
                        /*b[r][c].setText(""+intMap[r][c]);
                        if ((r+c)%2==0) {
                            b[r][c].setStyle("-fx-background-color: #e5c29f");
                        } else {
                            b[r][c].setStyle("-fx-background-color: #d7b899");
                        }*/
                }
            }
        }
        clickedButton.setText("X");
    }

    public void winAlert() {
        if (clickedButtons == rowLength*colLength-bombNum) {
            isAlive = false;
            timeline.stop();
            Alert winAlert = new Alert(Alert.AlertType.CONFIRMATION);
            winAlert.setContentText("YOU WON!");
            winAlert.showAndWait();
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}
