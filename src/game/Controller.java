package game;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.io.File;
import java.util.Map;
import java.util.Objects;

/**
 * Controller class, used to control the game logic
 * @author Tobias Hernandez Perez
 */
public class Controller {
    /**
     * defines the game
     */
    private final Game game;


    private final int[][] intMap;
    private final boolean[][] clicked;
    private final boolean[][] isFlag;
    private final Button[][] b;

    private final int rowLength;
    private final int colLength;
    private int flags;
    private final int bombNum;

    private final Label bombsLeftText;
    ImageView flagImgView;

    private final Map<String, String> style;

    MediaPlayer clickPlayer;


    /**
     * constructor to use the class from the game class
     * @param game game class
     */
    public Controller(Game game) {
        this.game = game;
        this.intMap = game.intMap;
        this.clicked = game.clickedMap;
        this.rowLength = game.rowLength;
        this.colLength = game.colLength;
        this.flags = game.flags;
        this.bombNum = game.bombNum;
        this.bombsLeftText = game.bombsLeftText;
        this.b = game.b;
        this.style = game.style;
        this.isFlag = new boolean[rowLength][colLength];

        String clickSound = "./resources/click.wav";
        //System.out.println(new File(clickSound).toURI().toString());
        //String clickSound = "resources/test.wav";
        Media clickMedia = new Media(new File(clickSound).toURI().toString());
        clickPlayer = new MediaPlayer(clickMedia);
    }

    /**
     * controls the class
     * @param clickedButton currently clicked button
     * @param row row that the button is in
     * @param col collum that the button is in
     * @param e mouse event
     */
    public void gameController(Button clickedButton, int row, int col, MouseEvent e) {
        MouseButton mouse = e.getButton();
        int buttonNum = intMap[row][col];

        if (!game.isAlive || (isFlag[row][col] && mouse!=MouseButton.SECONDARY)) return;


        if ((clicked[row][col] && !isFlag[row][col] && intMap[row][col]!=0 && mouse==MouseButton.MIDDLE)
                || (e.isPrimaryButtonDown() && e.isSecondaryButtonDown())) {
            showButtonMiddleMouse(row, col);
            return;
        }

        if (placeFlag(clickedButton, mouse, row, col)) return;

        if (!clicked[row][col] && !isFlag[row][col] && mouse==MouseButton.PRIMARY) {
            showClickedButton(clickedButton, row, col, buttonNum);
        }
    }

    /**
     * used to show what number the button is
     * if bomb: loss
     * if 0: *pop*
     * @param clickedButton currently clicked button
     * @param row row the button is in
     * @param col collum the button is in
     * @param buttonNum how many bombs are around the button
     */
    public void showClickedButton(Button clickedButton, int row, int col, int buttonNum) {
        //clickPlayer.setVolume(game.settings.volumeSlider.getValue());
        //clickPlayer.play();
        //clickPlayer.seek(Duration.millis(0));

        if (buttonNum != -1) game.clickedButtons++;
        clicked[row][col] = true;

        int right = col+1;
        int left = col-1;
        int bot = row+1;
        int top = row-1;

        if (intMap[row][col] == 0) {
            if (right < colLength && !clicked[row][right] && !isFlag[row][right]) showClickedButton(b[row][right], row, right, intMap[row][right]);
            if (left >= 0 && !clicked[row][left] && !isFlag[row][left]) showClickedButton(b[row][left], row, left, intMap[row][left]);
            if (top >= 0 && !clicked[top][col] && !isFlag[top][col]) showClickedButton(b[top][col], top, col, intMap[top][col]);
            if (bot < rowLength && !clicked[bot][col] && !isFlag[bot][col]) showClickedButton(b[bot][col], bot, col, intMap[bot][col]);
            if (top >= 0 && right < colLength && !clicked[top][right] && !isFlag[top][right]) showClickedButton(b[top][right], top, right, intMap[top][right]);
            if (top >= 0 && left >= 0 && !clicked[top][left] && !isFlag[top][left]) showClickedButton(b[top][left], top, left, intMap[top][left]);
            if (bot < rowLength && right < colLength && !clicked[bot][right] && !isFlag[bot][right]) showClickedButton(b[bot][right], bot, right, intMap[bot][right]);
            if (bot < rowLength && left >= 0 && !clicked[bot][left] && !isFlag[bot][left]) showClickedButton(b[bot][left], bot, left, intMap[bot][left]);
        }

        int size = Integer.parseInt(style.get("Button Size " + game.difficultyString))/2;
        if (buttonNum != 0) clickedButton.setText(""+ buttonNum);
        clickedButton.setFont(Font.font("Roboto", FontWeight.BOLD, size));
        if ((row + col)%2==0) {
            clickedButton.setStyle(style.get("Button 1 Clicked"));
        } else {
            clickedButton.setStyle(style.get("Button 2 Clicked"));
        }
        switch (buttonNum) {
            case 1:
                clickedButton.setTextFill(Paint.valueOf(style.get("1")));
                break;
            case 2:
                clickedButton.setTextFill(Paint.valueOf(style.get("2")));
                break;
            case 3:
                clickedButton.setTextFill(Paint.valueOf(style.get("3")));
                break;
            case 4:
                clickedButton.setTextFill(Paint.valueOf(style.get("4")));
                break;
            case 5:
                clickedButton.setTextFill(Paint.valueOf(style.get("5")));
                break;
            case 6:
                clickedButton.setTextFill(Paint.valueOf(style.get("6")));
                break;
            case 7:
                clickedButton.setTextFill(Paint.valueOf(style.get("7")));
                break;
            case 8:
                clickedButton.setTextFill(Paint.valueOf(style.get("8")));
                break;
            default:
                break;
        }

        if (buttonNum == -1) {
            game.lossAlert(clickedButton);
        }

        if (game.isAlive && game.clickedButtons == rowLength*colLength-bombNum) game.winAlert();
    }

    /**
     * when middle clicking (or left and right click at the same time) on a flipped tile
     * and the correct number of flags are around the button click all the non flags around it
     * @param row what row the middle clicked button is in
     * @param col what collum the middle clicked button is in
     */
    public void showButtonMiddleMouse(int row, int col) {
        int right = col+1;
        int left = col-1;
        int bot = row+1;
        int top = row-1;
        int surroundingFlags = 0;


        if (right < colLength && isFlag[row][right]) surroundingFlags++;
        if (left >= 0 && isFlag[row][left]) surroundingFlags++;
        if (top >= 0 && isFlag[top][col]) surroundingFlags++;
        if (bot < rowLength && isFlag[bot][col]) surroundingFlags++;
        if (top >= 0 && right < colLength && isFlag[top][right]) surroundingFlags++;
        if (top >= 0 && left >= 0 && isFlag[top][left]) surroundingFlags++;
        if (bot < rowLength && right < colLength && isFlag[bot][right]) surroundingFlags++;
        if (bot < rowLength && left >= 0 && isFlag[bot][left]) surroundingFlags++;


        if (intMap[row][col] == surroundingFlags){
            if (right < colLength && !clicked[row][right] && !isFlag[row][right]) showClickedButton(b[row][right], row, right, intMap[row][right]);
            if (left >= 0 && !clicked[row][left] && !isFlag[row][left]) showClickedButton(b[row][left], row, left, intMap[row][left]);
            if (top >= 0 && !clicked[top][col] && !isFlag[top][col]) showClickedButton(b[top][col], top, col, intMap[top][col]);
            if (bot < rowLength && !clicked[bot][col] && !isFlag[bot][col]) showClickedButton(b[bot][col], bot, col, intMap[bot][col]);
            if (top >= 0 && right < colLength && !clicked[top][right] && !isFlag[top][right]) showClickedButton(b[top][right], top, right, intMap[top][right]);
            if (top >= 0 && left >= 0 && !clicked[top][left] && !isFlag[top][left]) showClickedButton(b[top][left], top, left, intMap[top][left]);
            if (bot < rowLength && right < colLength && !clicked[bot][right] && !isFlag[bot][right]) showClickedButton(b[bot][right], bot, right, intMap[bot][right]);
            if (bot < rowLength && left >= 0 && !clicked[bot][left] && !isFlag[bot][left]) showClickedButton(b[bot][left], bot, left, intMap[bot][left]);
        }
    }

    /**
     * used to place a flag on right click
     * @param clickedButton button that was right clicked
     * @param button mouse button that was used
     * @param row what row the button is in
     * @param col what collum the button is in
     */
    public boolean placeFlag(Button clickedButton, MouseButton button, int row, int col) {
        Image flagImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("pics/flag.png")));
        flagImgView = new ImageView(flagImg);
        int size = Integer.parseInt(style.get("Button Size " + game.difficultyString))/2;
        flagImgView.setFitWidth(size);
        flagImgView.setFitHeight(size);

        if(button==MouseButton.SECONDARY) {
            if (isFlag[row][col]) {
                flags--;
                isFlag[row][col]=false;
                clickedButton.setGraphic(null);
                bombsLeftText.setText("" + (bombNum-flags));
            } else if (!clicked[row][col]){
                flags++;
                clickedButton.setGraphic(flagImgView);
                isFlag[row][col]=true;
                clickedButton.setFont(Font.font("Roboto", FontWeight.BOLD, size));
                bombsLeftText.setText("" + (bombNum-flags));
                //bombsLeft.setText(""+(bombNum-flags));
            }

            return true;
        }
        return false;
    }
}
