package game;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Map;

public class Generation {
    private final Game game;
    private final Controller controller;
    private final int[][] intMap;
    private final int rowLength;
    private final int colLength;
    private final Button[][] b;
    private final Map<String, String> style;

    private boolean isGenerating = false;


    /**
     * constructor of the generation class
     * @param game game class
     * @param controller controller class
     */
    public Generation(Game game, Controller controller) {
        this.game = game;
        this.controller = controller;
        this.intMap = game.intMap;
        this.rowLength = game.rowLength;
        this.colLength = game.colLength;
        this.b = game.b;
        this.style = game.style;
    }

    /**
     * generates a map only for visual purposes
     */
    public void generateGenerationMap(GridPane gridPane) {
        for (int row = 0; row < game.intMap.length; row++) {
            for (int col = 0; col < game.intMap[row].length; col++) {
                b[row][col] = new Button();
                Button currBut = b[row][col];
                switch (game.difficultyString) {
                    case "Easy" -> currBut.setMinSize(Integer.parseInt(style.get("Button Size Easy")), Integer.parseInt(style.get("Button Size Easy")));
                    case "Medium" -> currBut.setMinSize(Integer.parseInt(style.get("Button Size Medium")), Integer.parseInt(style.get("Button Size Medium")));
                    case "Hard" -> currBut.setMinSize(Integer.parseInt(style.get("Button Size Hard")), Integer.parseInt(style.get("Button Size Hard")));
                    default -> throw new IllegalArgumentException("Error at " + game.difficultyString + " illegal difficulty.");
                }

                if ((row+col) % 2 == 0) {
                    currBut.setStyle(style.get("Button 1"));
                } else {
                    currBut.setStyle(style.get("Button 2"));
                }

                int finalRow = row;
                int finalCol = col;

                currBut.setOnMouseClicked(e -> generateIntMap(gridPane, finalRow, finalCol));
                gridPane.add(currBut, col, row);
            }
        }
    }

    /**
     * makes sure that the button is a 0
     * @param safeRow row that has to be safe
     * @param safeCol collum that has to be safe
     * @return if the button is safe
     */
    public boolean isSafe(int randRow, int randCol, int safeRow, int safeCol) {
        int left = safeCol+1;
        int right = safeCol-1;
        int top = safeRow-1;
        int bottom = safeRow+1;

        boolean cols = randCol == safeCol || randCol == right || randCol == left;

        if (randRow == safeRow)
            if (cols) return false;
        if (randRow == top)
            if (cols) return false;
        if (randRow == bottom)
            return !cols;

        return true;
    }

    /**
     * generates the back end map
     */
    public void generateIntMap(GridPane gridPane, int safeRow, int safeCol) {
        //used to prevent a bug that broke the game
        if (isGenerating) return;
        isGenerating = true;

        game.timeline.play();

        while (game.bombCounter != game.bombNum) {
            int randRow = (int)(Math.random() * ((rowLength - 1) + 1));
            int randCol = (int)(Math.random() * ((colLength - 1) + 1));

            if (intMap[randRow][randCol] != -1 && isSafe(randRow, randCol, safeRow, safeCol)) {
                intMap[randRow][randCol] = -1;
                addNums(randRow, randCol);
            }
        }

        //outputs intmap for debuging purposes
        for (int[] ints : intMap) {
            System.out.println();
            for (int anInt : ints) {
                System.out.print(anInt + "  ");
            }
        }

        generateButtonMap(gridPane);

        Button clickedButton = b[safeRow][safeCol];
        int buttonNum = intMap[safeRow][safeCol];

        controller.showClickedButton(clickedButton, safeRow, safeCol, buttonNum);

        clickedButton.setFont(Font.font("Roboto", FontWeight.BOLD, 15));
        game.clickedMap[safeRow][safeCol] = true;
    }

    /**
     * adds the number that indicates how many bombs are around the button
     */
    public void addNums(int row, int col) {
        game.bombCounter++;

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

    /**
     * Generates the buttons and makes them interactive
     */
    public void generateButtonMap(GridPane gridPane) {
        for (int row = 0; row < intMap.length; row++) {
            for (int col = 0; col < intMap[row].length; col++) {
                b[row][col] = new Button();
                Button currBut = b[row][col];

                switch (game.difficultyString) {
                    case "Easy" -> currBut.setMinSize(Integer.parseInt(style.get("Button Size Easy")),
                            Integer.parseInt(style.get("Button Size Easy")));
                    case "Medium" -> currBut.setMinSize(Integer.parseInt(style.get("Button Size Medium")),
                            Integer.parseInt(style.get("Button Size Medium")));
                    case "Hard" -> currBut.setMinSize(Integer.parseInt(style.get("Button Size Hard")),
                            Integer.parseInt(style.get("Button Size Hard")));
                    default -> throw new IllegalArgumentException("Error at " + game.difficultyString + " illegal difficulty.");
                }

                if ((row+col) % 2 == 0) {
                    currBut.setStyle(style.get("Button 1"));
                } else {
                    currBut.setStyle(style.get("Button 2"));
                }

                int finalRow = row;
                int finalCol = col;
                currBut.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> controller.gameController(b[finalRow][finalCol], finalRow, finalCol, e));

                gridPane.add(currBut, col, row);
            }
        }
    }
}
