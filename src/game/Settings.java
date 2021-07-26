package game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Settings class used to change colors and other values
 * @author Tobias Hernandez Perez
 */
public class Settings {
    /**
     * defines the game
     */
    private final Game game;
    /**
     * defines the controller
     */
    private final Controller controller;


    /**
     * settings stage
     */
    public Stage settings;
    /**
     * root pane
     */
    private BorderPane border;
    /**
     * pane for selection of tabs
     */
    private final HBox menuPane = new HBox();
    /**
     * button style tab
     */
    private final VBox buttonPane = new VBox();
    /**
     * top style tab
     */
    private final VBox generalPane = new VBox();

    /**
     * values of sliders, checkboxes etc.
     */
    private final Map<String, String> settingMap = new TreeMap<>();
    /**
     * style map
     */
    private final Map<String, String> style;

    /**
     * rgb sliders color 1 non-clicked buttons
     */
    private final Slider slidR1 = new Slider();
    private final Slider slidG1 = new Slider();
    private final Slider slidB1 = new Slider();
    /**
     * rgb sliders color 2 non-clicked buttons
     */
    private final Slider slidR2 = new Slider();
    private final Slider slidG2 = new Slider();
    private final Slider slidB2 = new Slider();

    /**
     * rgb sliders color 1 clicked buttons
     */
    private final Slider slidR1C = new Slider();
    private final Slider slidG1C = new Slider();
    private final Slider slidB1C = new Slider();
    /**
     * rgb sliders color 2 clicked buttons
     */
    private final Slider slidR2C = new Slider();
    private final Slider slidG2C = new Slider();
    private final Slider slidB2C = new Slider();

    /**
     * rgb slider top color
     */
    private final Slider slidRT = new Slider();
    private final Slider slidGT = new Slider();
    private final Slider slidBT = new Slider();

    /**
     * adjusts volume of game sounds
     */
    public Slider volumeSlider = new Slider();

    /**
     * map of all sliders
     */
    private final Map<String, Slider> sliderMap = new TreeMap<>();

    /**
     * inverts background color of top
     */
    private final int[] invertedColor;
    /**
     * Buttons to switch between tabs
     */
    Button buttons = new Button("Buttons"), general = new Button("General");


    /**
     * puts all sliders in a map
     */
    private void fillSliderMap() {
        sliderMap.put("slidR1", slidR1);
        sliderMap.put("slidG1", slidG1);
        sliderMap.put("slidB1", slidB1);

        sliderMap.put("slidR2", slidR2);
        sliderMap.put("slidG2", slidG2);
        sliderMap.put("slidB2", slidB2);


        sliderMap.put("slidR1C", slidR1C);
        sliderMap.put("slidG1C", slidG1C);
        sliderMap.put("slidB1C", slidB1C);

        sliderMap.put("slidR2C", slidR2C);
        sliderMap.put("slidG2C", slidG2C);
        sliderMap.put("slidB2C", slidB2C);

        sliderMap.put("slidRT", slidRT);
        sliderMap.put("slidGT", slidGT);
        sliderMap.put("slidBT", slidBT);
    }


    /**
     * Constructor for settings
     */
    public Settings(Game game, Controller controller) {
        this.game = game;
        this.style = game.style;
        this.controller = controller;
        this.invertedColor = game.getInvertedBackground(style.get("Control Pane Background"));


        Button resetButtonColors = new Button("Reset Style"), resetGeneralStyle = new Button("Reset Style");
        Button saveButtons = new Button("Save"), saveGeneral = new Button("Save");

        setButtonStyles(resetButtonColors, 12);
        setButtonStyles(resetGeneralStyle, 12);

        fillSliderMap();
        readSettings();
        setPanes();

        setSliders(slidR1, slidG1, slidB1);
        setSliders(slidR2, slidG2, slidB2);
        setSliders(slidR1C, slidG1C, slidB1C);
        setSliders(slidR2C, slidG2C, slidB2C);


        addToMenuPane();
        addToButtonPane(buttonPane, resetButtonColors, saveButtons);
        addToGeneral(generalPane, resetGeneralStyle, saveGeneral);

        setSave(saveButtons, "Buttons");
        setSave(saveGeneral, "Top");


        int sceneWidth = 350;
        int sceneHeight = 650;
        Scene settingScene = new Scene(border, sceneWidth, sceneHeight);


        settings.setOnCloseRequest(e -> game.isInSettings = false);
        settings.setScene(settingScene);


        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("pics/settings.png")));
        settings.getIcons().add(icon);

        resetButtonColors.setOnMouseClicked(e -> setMainStyleButtons());
        resetGeneralStyle.setOnMouseClicked(e -> setMainStyleTop());
        buttons.setOnMouseClicked(e -> border.setCenter(buttonPane));
        general.setOnMouseClicked(e -> border.setCenter(generalPane));

        setSliderValues();
    }

    /**
     * Defines the style and font of a button
     * @param button button to style
     * @param i font size
     */
    private void setButtonStyles(Button button, int i) {
        button.setStyle("-fx-border-radius: 0;" + style.get("Control Pane Background"));
        button.setFont(Font.font("Roboto", FontWeight.BOLD, i));
        button.setTextFill(Color.rgb(invertedColor[0], invertedColor[1], invertedColor[2]));
    }

    /**
     * adds elements to the menu pane
     */
    private void addToMenuPane() {
        menuPane.getChildren().addAll(general, buttons);
        menuPane.setPadding(new Insets(5));
        general.setTextFill(Color.rgb(invertedColor[0], invertedColor[1], invertedColor[2]));
        buttons.setTextFill(Color.rgb(invertedColor[0], invertedColor[1], invertedColor[2]));
        general.setFont(Font.font("Roboto", FontWeight.BOLD, 15));
        buttons.setFont(Font.font("Roboto", FontWeight.BOLD, 15));
        general.setStyle("-fx-background-color: transparent;");
        buttons.setStyle("-fx-background-color: transparent;");
    }

    /**
     * adds elements to the pane for the general settings
     * @param generalPane pane for general settings
     * @param reset button to reset style
     * @param save button to save style
     */
    private void addToGeneral(VBox generalPane, Button reset, Button save) {
        Button colorPreview = new Button();
        CheckBox hideTimer = new CheckBox("Hide Timer");
        CheckBox hideBombsLeft = new CheckBox("Hide Bombs Left");
        colorPreview.setMinSize(30, 30);


        hideTimer.setSelected(!Boolean.parseBoolean(settingMap.get("hideTimer")));
        hideBombsLeft.setSelected(!Boolean.parseBoolean(settingMap.get("hideBombsLeft")));

        setVisibility(!hideTimer.isSelected(), game.timer, game.timerImgView);
        setVisibility(!hideBombsLeft.isSelected(), game.bombsLeftText, controller.flagImgView);



        generalPane.getChildren().add(new Label());
        generalPane.getChildren().add(reset);

        generalPane.getChildren().add(new Label());
        generalPane.getChildren().addAll(sliderBox("Red", slidRT, false, colorPreview),
                sliderBox("Green", slidGT, true, colorPreview),
                sliderBox("Blue", slidBT, false, colorPreview));


        generalPane.getChildren().add(new Label());
        generalPane.getChildren().addAll(hideTimer, hideBombsLeft);


        generalPane.getChildren().add(new Label());
        generalPane.getChildren().add(sliderBox("Volume", volumeSlider, false, new Button()));
        volumeSlider.setMax(3);
        volumeSlider.setValue(Double.parseDouble(settingMap.get("volumeSlider")));


        generalPane.getChildren().add(new Label());
        generalPane.getChildren().add(save);

        hideTimer.selectedProperty().addListener((ov, old_val, new_val) -> {
            settingMap.put("hideTimer", old_val+"");
            setVisibility(old_val, game.timer, game.timerImgView);
        });
        hideBombsLeft.selectedProperty().addListener((ov, old_val, new_val) -> {
            settingMap.put("hideBombsLeft", old_val+"");
            setVisibility(old_val, game.bombsLeftText, controller.flagImgView);
        });


        setSliders(slidRT, slidGT, slidBT);
        previewColor(colorPreview, slidRT, "top");
        previewColor(colorPreview, slidGT, "top");
        previewColor(colorPreview, slidBT, "top");

        generalPane.setSpacing(3);
    }

    /**
     * adds elements to the pane for the button settings
     * @param buttonPane pane for button settings
     * @param reset reset style button
     * @param save save style button
     */
    private void addToButtonPane(VBox buttonPane, Button reset, Button save) {
        Label unclicked = new Label("Color 1"), clicked = new Label("Color 2");

        unclicked.setFont(Font.font("Roboto", FontWeight.BOLD, 15));
        clicked.setFont(Font.font("Roboto", FontWeight.BOLD, 15));

        Button colorPreview1 = new Button(), colorPreview2 = new Button();
        colorPreview1.setMinSize(30, 30);
        colorPreview2.setMinSize(30, 30);


        buttonPane.getChildren().add(new Label());
        buttonPane.getChildren().add(reset);

        buttonPane.getChildren().addAll(new Label(), new Label());

        buttonPane.getChildren().add(unclicked);
        buttonPane.getChildren().addAll(sliderBox("Red", slidR1, false, colorPreview1),
                sliderBox("Green", slidG1, true, colorPreview1),
                sliderBox("Blue", slidB1, false, colorPreview1));

        buttonPane.getChildren().add(new Label());

        buttonPane.getChildren().addAll(sliderBox("Red", slidR2, false, colorPreview2),
                sliderBox("Green", slidG2, true, colorPreview2),
                sliderBox("Blue", slidB2, false, colorPreview2));

        buttonPane.getChildren().add(new Label());
        buttonPane.getChildren().add(save);


        buttonPane.getChildren().addAll(new Label(), new Label());


        Button saveClicked = new Button("Save");
        Button colorPreview1Clicked = new Button(), colorPreview2Clicked = new Button();
        colorPreview2Clicked.setMinSize(30, 30);
        colorPreview1Clicked.setMinSize(30, 30);

        buttonPane.getChildren().add(new Label());

        buttonPane.getChildren().add(clicked);
        buttonPane.getChildren().addAll(sliderBox("Red", slidR1C, false, colorPreview1Clicked),
                sliderBox("Green", slidG1C, true, colorPreview1Clicked),
                sliderBox("Blue", slidB1C, false, colorPreview1Clicked));

        buttonPane.getChildren().add(new Label());
        buttonPane.getChildren().addAll(sliderBox("Red", slidR2C, false, colorPreview2Clicked),
                sliderBox("Green", slidG2C, true, colorPreview2Clicked),
                sliderBox("Blue", slidB2C, false, colorPreview2Clicked));

        buttonPane.getChildren().add(new Label());
        buttonPane.getChildren().add(saveClicked);


        previewColor(colorPreview1, slidR1, "buttons1");
        previewColor(colorPreview1, slidG1, "buttons1");
        previewColor(colorPreview1, slidB1, "buttons1");
        previewColor(colorPreview2, slidR2, "buttons2");
        previewColor(colorPreview2, slidG2, "buttons2");
        previewColor(colorPreview2, slidB2, "buttons2");
        previewColor(colorPreview1Clicked, slidR1C, "buttons1Clicked");
        previewColor(colorPreview1Clicked, slidG1C, "buttons1Clicked");
        previewColor(colorPreview1Clicked, slidB1C, "buttons1Clicked");
        previewColor(colorPreview2Clicked, slidR2C, "buttons2Clicked");
        previewColor(colorPreview2Clicked, slidG2C, "buttons2Clicked");
        previewColor(colorPreview2Clicked, slidB2C, "buttons2Clicked");

        setSave(save, "Buttons");
        setSave(saveClicked, "ButtonsClicked");
    }

    /**
     * sets the visibility of the timer or the bombs-left text
     * @param value whether its visible or not
     * @param text text that has to be edited
     * @param pic image that has to be edited
     */
    public void setVisibility(Boolean value, Node text, Node pic) {
        try {
            text.setVisible(value);
            pic.setVisible(value);
        } catch (NullPointerException ignored) {}
    }

    /**
     * creates a new hbox in which one of the rgb sliders (and the color preview) are contained
     * @param color string of the label next to the slider
     * @param slider the slider being adds
     * @param isPreview whether there should be a color preview next to it
     * @param preview the color preview
     * @return hbox with sliders
     */
    private HBox sliderBox(String color, Slider slider, boolean isPreview, Button preview) {
        HBox sliderBox = new HBox();

        if (isPreview) {
            sliderBox.getChildren().addAll(new Label(color), slider, preview);
        } else {
            sliderBox.getChildren().addAll(new Label(color), slider);
        }

        sliderBox.setSpacing(20);
        sliderBox.setAlignment(Pos.CENTER_LEFT);

        return sliderBox;
    }

    /**
     * sets the value of all sliders at the beginning of the program
     */
    private void setSliderValues() {
        Set<Map.Entry<String, Slider>> sliderSet = sliderMap.entrySet();
        for (Map.Entry<String, Slider> stringSliderEntry : sliderSet) {
            String key = stringSliderEntry.getKey();
            Slider value = stringSliderEntry.getValue();

            value.setValue(Double.parseDouble(settingMap.get(key)));
        }
    }

    /**
     * defines what the save button should save
     * @param save save button
     * @param type what should be saved
     */
    private void setSave(Button save, String type) {
        setButtonStyles(save, 15);

        switch (type) {
            case "Buttons" -> save.setOnMouseClicked(e -> setButtonStyle(
                    slidR1.getValue() + "," + slidG1.getValue() + "," + slidB1.getValue(),
                    slidR2.getValue() + "," + slidG2.getValue() + "," + slidB2.getValue(),
                    false
            ));
            case "ButtonsClicked" -> save.setOnMouseClicked(e -> setButtonStyle(
                    slidR1C.getValue() + "," + slidG1C.getValue() + "," + slidB1C.getValue(),
                    slidR2C.getValue() + "," + slidG2C.getValue() + "," + slidB2C.getValue(),
                    true
            ));
            case "Top" -> save.setOnMouseClicked(e -> {
                controller.clickPlayer.setVolume(volumeSlider.getValue());
                settingMap.put("volumeSlider", volumeSlider.getValue() + "");
                setTopStyle(slidRT.getValue() + "," + slidGT.getValue() + "," + slidBT.getValue());

                game.bombsLeftText.setStyle("-fx-text-fill: " + "rgb(" + Arrays.toString(invertedColor).substring(1, Arrays.toString(invertedColor).length() - 1) + ")");
                game.timer.setFill(Color.rgb(invertedColor[0], invertedColor[1], invertedColor[2]));
                general.setTextFill(Color.rgb(invertedColor[0], invertedColor[1], invertedColor[2]));
            });
        }
    }

    /**
     * defines the basic layout of the panes
     */
    private void setPanes() {
        settings = new Stage();
        border = new BorderPane();
        border.setTop(menuPane);
        border.setCenter(generalPane);
        generalPane.setPadding(new Insets(10,  10, 10, 10));
        buttonPane.setPadding(new Insets(10,  10, 10, 10));

        menuPane.setStyle(style.get("Control Pane Background"));
    }

    /**
     * sets the start, the min and the max value of the sliders
     * @param red red slider
     * @param green green slider
     * @param blue blue slider
     */
    private void setSliders(Slider red, Slider green, Slider blue) {
        red.setMin(0);
        red.setMax(255);
        red.setValue(0);

        green.setMin(0);
        green.setMax(255);
        green.setValue(0);

        blue.setMin(0);
        blue.setMax(255);
        blue.setValue(0);
    }

    /**
     * puts the new styles and values in the corresponding maps
     * and changes their style
     * @param color1 first color
     * @param color2 second color
     * @param isClicked whether the style of the clicked or unclicked buttons should be changed
     */
    private void setButtonStyle(String color1, String color2, boolean isClicked) {
        if (isClicked) {
            style.put("Button 1 Clicked", "-fx-background-radius: 0; -fx-background-color: rgb(" + color1 + ")");
            style.put("Button 2 Clicked", "-fx-background-radius: 0; -fx-background-color: rgb(" + color2 + ")");

            settingMap.put("slidR1C", ""+ slidR1C.getValue());
            settingMap.put("slidG1C", ""+ slidG1C.getValue());
            settingMap.put("slidB1C", ""+ slidB1C.getValue());
            settingMap.put("slidR2C", ""+ slidR2C.getValue());
            settingMap.put("slidG2C", ""+ slidG2C.getValue());
        } else {
            style.put("Button 1", "-fx-background-radius: 0; -fx-background-color: rgb(" + color1 + ")");
            style.put("Button 2", "-fx-background-radius: 0; -fx-background-color: rgb(" + color2 + ")");

            settingMap.put("slidR1", ""+ slidR1.getValue());
            settingMap.put("slidG1", ""+ slidG1.getValue());
            settingMap.put("slidB1", ""+ slidB1.getValue());
            settingMap.put("slidR2", ""+ slidR2.getValue());
            settingMap.put("slidG2", ""+ slidG2.getValue());
            settingMap.put("slidB2", ""+ slidB2.getValue());
        }


        for (int r = 0; r < game.rowLength; r++) {
            for (int c = 0; c < game.colLength; c++) {
                Button currBut = game.b[r][c];
                if (game.clickedMap[r][c]) {
                    if ((r+c) % 2 == 0) {
                        currBut.setStyle(style.get("Button 1 Clicked"));
                    } else {
                        currBut.setStyle(style.get("Button 2 Clicked"));
                    }
                } else {
                    if ((r+c) % 2 == 0) {
                        currBut.setStyle(style.get("Button 1"));
                    } else {
                        currBut.setStyle(style.get("Button 2"));
                    }
                }

            }
        }

        game.writeNewStyle();
        writeSettings();
    }

    /**
     * puts then new styles and values in the corresponding maps
     * and changes the style of the top panes
     * @param color new color
     */
    private void setTopStyle(String color) {
        String newColor = "-fx-background-color: rgb(" + color + ")";

        style.put("Control Pane Background", newColor);

        settingMap.put("Slider Red Control", ""+ slidRT.getValue());
        settingMap.put("Slider Green Control", ""+ slidGT.getValue());
        settingMap.put("Slider Blue Control", ""+ slidBT.getValue());

        game.controlPane.setStyle(newColor);

        game.writeNewStyle();
        writeSettings();
    }

    /**
     * resets the button styles to their original values
     */
    private void setMainStyleButtons() {
        style.put("Button 1", "-fx-background-radius: 0; -fx-background-color: #aad751");
        style.put("Button 2", "-fx-background-radius: 0; -fx-background-color: #a2d149");
        style.put("Button 1 Clicked", "-fx-background-radius: 0; -fx-background-color: #e5c29f");
        style.put("Button 2 Clicked", "-fx-background-radius: 0; -fx-background-color: #d7b899");

        for (int r = 0; r < game.rowLength; r++) {
            for (int c = 0; c < game.colLength; c++) {
                Button currBut = game.b[r][c];
                if (game.clickedMap[r][c]) {
                    if ((r+c) % 2 == 0) {
                        currBut.setStyle(style.get("Button 1 Clicked"));
                    } else {
                        currBut.setStyle(style.get("Button 2 Clicked"));
                    }
                } else {
                    if ((r+c) % 2 == 0) {
                        currBut.setStyle(style.get("Button 1"));
                    } else {
                        currBut.setStyle(style.get("Button 2"));
                    }
                }

            }
        }

        game.writeNewStyle();
    }

    /**
     * resets the top style to its original value
     */
    private void setMainStyleTop() {
        style.put("Control Pane Background", "-fx-background-color: #4a752c");

        game.controlPane.setStyle("-fx-background-color: #4a752c");

        game.writeNewStyle();
    }

    /**
     * sets the color previews and adds listeners to their respective sliders
     * @param colorPreview the color preview to be set
     * @param slider the slider to which the listener is added
     * @param type which color preview is being changed
     */
    private void previewColor(Button colorPreview, Slider slider, String type) {
        switch (type) {
            case "buttons1" -> {
                colorPreview.setStyle("-fx-background-color: rgb(" +
                        slidR1.getValue() + "," + slidG1.getValue() + "," + slidB1.getValue() + ")");
                slider.valueProperty().addListener((observable, oldValue, newValue) ->
                        colorPreview.setStyle("-fx-background-color: rgb(" +
                                slidR1.getValue() + "," + slidG1.getValue() + "," + slidB1.getValue() + ")"));
            }
            case "buttons2" -> {
                colorPreview.setStyle("-fx-background-color: rgb(" +
                        slidR2.getValue() + "," + slidG2.getValue() + "," + slidB2.getValue() + ")");
                slider.valueProperty().addListener((observable, oldValue, newValue) ->
                        colorPreview.setStyle("-fx-background-color: rgb(" +
                                slidR2.getValue() + "," + slidG2.getValue() + "," + slidB2.getValue() + ")"));
            }
            case "buttons1Clicked" -> {
                colorPreview.setStyle("-fx-background-color: rgb(" +
                        slidR1C.getValue() + "," + slidG1C.getValue() + "," + slidB1C.getValue() + ")");
                slider.valueProperty().addListener((observable, oldValue, newValue) ->
                        colorPreview.setStyle("-fx-background-color: rgb(" +
                                slidR1C.getValue() + "," + slidG1C.getValue() + "," + slidB1C.getValue() + ")"));
            }
            case "buttons2Clicked" -> {
                colorPreview.setStyle("-fx-background-color: rgb(" +
                        slidR2C.getValue() + "," + slidG2C.getValue() + "," + slidB2C.getValue() + ")");
                slider.valueProperty().addListener((observable, oldValue, newValue) ->
                        colorPreview.setStyle("-fx-background-color: rgb(" +
                                slidR2C.getValue() + "," + slidG2C.getValue() + "," + slidB2C.getValue() + ")"));
            }
            case "top" -> {
                colorPreview.setStyle("-fx-background-color: rgb(" +
                        slidRT.getValue() + "," + slidGT.getValue() + "," + slidBT.getValue() + ")");
                slider.valueProperty().addListener((observable, oldValue, newValue) ->
                        colorPreview.setStyle("-fx-background-color: rgb(" +
                                slidRT.getValue() + "," + slidGT.getValue() + "," + slidBT.getValue() + ")"));
            }
        }
    }


    /**
     * reads the settings file
     * and puts the values into the map
     */
    private void readSettings() {
        try {
            try (BufferedReader settingsIn = Files.newBufferedReader(Paths.get("resources/settings.txt"), StandardCharsets.UTF_8)) {
                String line = settingsIn.readLine();

                while (line != null) {
                    if (line.trim().startsWith("#")) {
                        String key = line.substring(1).trim();
                        line = settingsIn.readLine();

                        settingMap.put(key, line);
                    }
                    line = settingsIn.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * writes the settings file
     */
    public void writeSettings() {
        try {
            BufferedWriter settingsOut = Files.newBufferedWriter(Paths.get("resources/settings.txt"));
            StringBuilder data = new StringBuilder();

            Set<Map.Entry<String, String>> styleSet = settingMap.entrySet();
            for (Map.Entry<String, String> stringStringEntry : styleSet) {
                String key = (String) ((Map.Entry) stringStringEntry).getKey();
                String value = (String) ((Map.Entry) stringStringEntry).getValue();

                data.append('#').append(key).append('\n').append(value).append('\n');
            }

            settingsOut.write(data.toString());
            settingsOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
