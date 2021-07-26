package game;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Settings {
    Game game;
    int sceneWidth = 350;
    int sceneHeight = 475;

    Stage settings;
    BorderPane border;
    GridPane menuPane = new GridPane();
    GridPane buttonPane = new GridPane();
    GridPane topPane = new GridPane();

    private BufferedReader settingsIn;
    public BufferedWriter settingsOut;

    private Map<String, String> settingMap = new TreeMap<>();
    private Map<String, String> style;

    Slider sliderRedC1 = new Slider();
    Slider sliderGreenC1 = new Slider();
    Slider sliderBlueC1 = new Slider();
    Slider sliderRedC2 = new Slider();
    Slider sliderGreenC2 = new Slider();
    Slider sliderBlueC2 = new Slider();

    Slider sliderRedC1Clicked = new Slider();
    Slider sliderGreenC1Clicked = new Slider();
    Slider sliderBlueC1Clicked = new Slider();
    Slider sliderRedC2Clicked = new Slider();
    Slider sliderGreenC2Clicked = new Slider();
    Slider sliderBlueC2Clicked = new Slider();

    Slider sliderRedControl = new Slider();
    Slider sliderGreenControl = new Slider();
    Slider sliderBlueControl = new Slider();

    Map<String, Slider> sliderMap = new TreeMap<>();


    public Settings(KeyEvent event, Game game) {
        this.game = game;
        this.style = game.style;

        Button mainStyleButtons = new Button("Original");
        Button mainStyleTop = new Button("Original");
        Button saveButtons = new Button("Save");
        Button saveTop = new Button("Save");
        Button buttonStyle = new Button("Button Style");
        Button topStyle = new Button("Top Style");

        readSettings();

        setPanes();


        setSliders(sliderRedC1, sliderGreenC1, sliderBlueC1);
        setSliders(sliderRedC2, sliderGreenC2, sliderBlueC2);
        setSliders(sliderRedC1Clicked, sliderGreenC1Clicked, sliderBlueC1Clicked);
        setSliders(sliderRedC2Clicked, sliderGreenC2Clicked, sliderBlueC2Clicked);


        menuPane.add(buttonStyle, 1, 0);
        menuPane.add(topStyle, 2, 0);
        addToButtonPane(buttonPane, mainStyleButtons, saveButtons);
        addToTopPane(topPane, mainStyleTop, saveTop);

        setSave(saveButtons, "Buttons");
        setSave(saveTop, "Top");

        Scene settingScene = new Scene(border, sceneWidth, sceneHeight);

        settings.setOnCloseRequest(e -> game.isInSettings = false);
        settings.setScene(settingScene);

        mainStyleButtons.setOnMouseClicked(e -> setMainStyleButtons());
        mainStyleTop.setOnMouseClicked(e -> setMainStyleTop());
        buttonStyle.setOnMouseClicked(e -> border.setCenter(buttonPane));
        topStyle.setOnMouseClicked(e -> border.setCenter(topPane));


        if (KeyCode.ESCAPE == event.getCode() && !game.isInSettings) {
            game.isInSettings = true;
            settings.show();

            setSliderValues();
        }
    }

    private void addToTopPane(GridPane topPane, Button mainStyle, Button save) {
        Button colorPreview = new Button();

        colorPreview.setMinSize(30, 30);

        topPane.add(mainStyle, 0, 0);
        topPane.add(sliderRedControl, 5, 10);
        topPane.add(sliderGreenControl, 5, 20);
        topPane.add(sliderBlueControl, 5, 30);
        topPane.add(save, 5, 99);
        topPane.add(colorPreview, 10, 20);

        setSliders(sliderRedControl, sliderGreenControl, sliderBlueControl);
        previewColor(colorPreview, sliderRedControl, "top");
        previewColor(colorPreview, sliderGreenControl, "top");
        previewColor(colorPreview, sliderBlueControl, "top");
    }

    private void addToButtonPane(GridPane buttonPane, Button mainStyle, Button save) {
        Label color1 = new Label("\nColor 1");
        Label color2 = new Label("\nColor 2");
        Label color1Clicked = new Label("\nColor 1 Clicked");
        Label color2Clicked = new Label("\nColor 2 Clicked");

        Label R = new Label("Red");
        Label G = new Label("Green");
        Label B = new Label("Blue");

        Label R2 = new Label("Red");
        Label G2 = new Label("Green");
        Label B2 = new Label("Blue");

        Label RClicked = new Label("Red");
        Label GClicked = new Label("Green");
        Label BClicked = new Label("Blue");

        Label R2Clicked = new Label("Red");
        Label G2Clicked = new Label("Green");
        Label B2Clicked = new Label("Blue");

        Button colorPreview1 = new Button();
        Button colorPreview2 = new Button();

        Button saveClicked = new Button("Save");

        Button colorPreview1Clicked = new Button();
        Button colorPreview2Clicked = new Button();

        colorPreview1.setMinSize(30, 30);
        colorPreview2.setMinSize(30, 30);
        colorPreview1Clicked.setMinSize(30, 30);
        colorPreview2Clicked.setMinSize(30, 30);

        buttonPane.add(mainStyle, 0, 0);
        //settingPane.add(buttonStyle, 0, 1);
        buttonPane.add(color1, 0, 1);
        buttonPane.add(color2, 0, 5);
        buttonPane.add(R, 0, 2);
        buttonPane.add(G, 0, 3);
        buttonPane.add(B, 0, 4);
        buttonPane.add(R2, 0, 6);
        buttonPane.add(G2, 0, 7);
        buttonPane.add(B2, 0, 8);
        buttonPane.add(colorPreview1, 2, 3);
        buttonPane.add(colorPreview2, 2, 7);
        buttonPane.add(save, 6, 10);

        buttonPane.add(sliderRedC1, 1, 2);
        buttonPane.add(sliderGreenC1, 1, 3);
        buttonPane.add(sliderBlueC1, 1, 4);

        buttonPane.add(sliderRedC2, 1, 6);
        buttonPane.add(sliderGreenC2, 1, 7);
        buttonPane.add(sliderBlueC2, 1, 8);
//--------------------------------------------------------
        buttonPane.add(color1Clicked, 0, 9);
        buttonPane.add(color2Clicked, 0, 13);
        buttonPane.add(RClicked, 0, 10);
        buttonPane.add(GClicked, 0, 11);
        buttonPane.add(BClicked, 0, 12);
        buttonPane.add(R2Clicked, 0, 14);
        buttonPane.add(G2Clicked, 0, 15);
        buttonPane.add(B2Clicked, 0, 16);
        buttonPane.add(colorPreview1Clicked, 2, 11);
        buttonPane.add(colorPreview2Clicked, 2, 15);
        buttonPane.add(saveClicked, 0, 99);

        buttonPane.add(sliderRedC1Clicked, 1, 10);
        buttonPane.add(sliderGreenC1Clicked, 1, 11);
        buttonPane.add(sliderBlueC1Clicked, 1, 12);

        buttonPane.add(sliderRedC2Clicked, 1, 14);
        buttonPane.add(sliderGreenC2Clicked, 1, 15);
        buttonPane.add(sliderBlueC2Clicked, 1, 16);


        previewColor(colorPreview1, sliderRedC1, "buttons1");
        previewColor(colorPreview1, sliderGreenC1, "buttons1");
        previewColor(colorPreview1, sliderBlueC1, "buttons1");
        previewColor(colorPreview2, sliderRedC2, "buttons2");
        previewColor(colorPreview2, sliderGreenC2, "buttons2");
        previewColor(colorPreview2, sliderBlueC2, "buttons2");
        previewColor(colorPreview1Clicked, sliderRedC1Clicked, "buttons1Clicked");
        previewColor(colorPreview1Clicked, sliderGreenC1Clicked, "buttons1Clicked");
        previewColor(colorPreview1Clicked, sliderBlueC1Clicked, "buttons1Clicked");
        previewColor(colorPreview2Clicked, sliderRedC2Clicked, "buttons2Clicked");
        previewColor(colorPreview2Clicked, sliderGreenC2Clicked, "buttons2Clicked");
        previewColor(colorPreview2Clicked, sliderBlueC2Clicked, "buttons2Clicked");

        setSave(saveClicked, "ButtonsClicked");
    }

    private void setSliderValues() {
        sliderRedC1.setValue(Double.parseDouble(settingMap.get("Slider Red Color 1")));
        sliderGreenC1.setValue(Double.parseDouble(settingMap.get("Slider Green Color 1")));
        sliderBlueC1.setValue(Double.parseDouble(settingMap.get("Slider Blue Color 1")));

        sliderRedC2.setValue(Double.parseDouble(settingMap.get("Slider Red Color 2")));
        sliderGreenC2.setValue(Double.parseDouble(settingMap.get("Slider Green Color 2")));
        sliderBlueC2.setValue(Double.parseDouble(settingMap.get("Slider Blue Color 2")));

        sliderRedC1Clicked.setValue(Double.parseDouble(settingMap.get("Slider Red Color 1 Clicked")));
        sliderGreenC1Clicked.setValue(Double.parseDouble(settingMap.get("Slider Green Color 1 Clicked")));
        sliderBlueC1Clicked.setValue(Double.parseDouble(settingMap.get("Slider Blue Color 1 Clicked")));

        sliderRedC2Clicked.setValue(Double.parseDouble(settingMap.get("Slider Red Color 2 Clicked")));
        sliderGreenC2Clicked.setValue(Double.parseDouble(settingMap.get("Slider Green Color 2 Clicked")));
        sliderBlueC2Clicked.setValue(Double.parseDouble(settingMap.get("Slider Blue Color 2 Clicked")));

        sliderRedControl.setValue(Double.parseDouble(settingMap.get("Slider Red Control")));
        sliderGreenControl.setValue(Double.parseDouble(settingMap.get("Slider Green Control")));
        sliderBlueControl.setValue(Double.parseDouble(settingMap.get("Slider Blue Control")));
    }

    private void setSave(Button save, String type) {
        if (type.equals("Buttons")) {
            save.setOnMouseClicked(e -> setButtonStyle(
                    sliderRedC1.getValue() + "," + sliderGreenC1.getValue() + "," + sliderBlueC1.getValue(),
                    sliderRedC2.getValue() + "," + sliderGreenC2.getValue() + "," + sliderBlueC2.getValue(),
                    false
            ));
        } else if (type.equals("ButtonsClicked")) {
            save.setOnMouseClicked(e -> setButtonStyle(
                    sliderRedC1Clicked.getValue() + "," + sliderGreenC1Clicked.getValue() + "," + sliderBlueC1Clicked.getValue(),
                    sliderRedC2Clicked.getValue() + "," + sliderGreenC2Clicked.getValue() + "," + sliderBlueC2Clicked.getValue(),
                    true
            ));
        } else if (type.equals("Top")) {
            save.setOnMouseClicked(e -> setTopStyle(
                    sliderRedControl.getValue() + "," + sliderGreenControl.getValue() + "," + sliderBlueControl.getValue()
            ));
        }
    }

    private void setPanes() {
        settings = new Stage();
        border = new BorderPane();
        border.setTop(menuPane);
        border.setCenter(buttonPane);
        buttonPane.setPadding(new Insets(10,  10, 10, 10));
        buttonPane.setHgap(25);
    }

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

    private void setButtonStyle(String color1, String color2, boolean isClicked) {
        if (isClicked) {
            style.put("Button 1 Clicked", "-fx-background-radius: 0; -fx-background-color: rgb(" + color1 + ")");
            style.put("Button 2 Clicked", "-fx-background-radius: 0; -fx-background-color: rgb(" + color2 + ")");

            settingMap.put("Slider Red Color 1 Clicked", ""+sliderRedC1Clicked.getValue());
            settingMap.put("Slider Green Color 1 Clicked", ""+sliderGreenC1Clicked.getValue());
            settingMap.put("Slider Blue Color 1 Clicked", ""+sliderBlueC1Clicked.getValue());
            settingMap.put("Slider Red Color 2 Clicked", ""+sliderRedC2Clicked.getValue());
            settingMap.put("Slider Green Color 2 Clicked", ""+sliderGreenC2Clicked.getValue());
        } else {
            style.put("Button 1", "-fx-background-radius: 0; -fx-background-color: rgb(" + color1 + ")");
            style.put("Button 2", "-fx-background-radius: 0; -fx-background-color: rgb(" + color2 + ")");

            settingMap.put("Slider Red Color 1", ""+sliderRedC1.getValue());
            settingMap.put("Slider Green Color 1", ""+sliderGreenC1.getValue());
            settingMap.put("Slider Blue Color 1", ""+sliderBlueC1.getValue());
            settingMap.put("Slider Red Color 2", ""+sliderRedC2.getValue());
            settingMap.put("Slider Green Color 2", ""+sliderGreenC2.getValue());
            settingMap.put("Slider Blue Color 2", ""+sliderBlueC2.getValue());
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

    private void setTopStyle(String color) {
        String newColor = "-fx-background-color: rgb(" + color + ")";

        style.put("Control Pane Background", newColor);

        settingMap.put("Slider Red Control", ""+sliderRedControl.getValue());
        settingMap.put("Slider Green Control", ""+sliderGreenControl.getValue());
        settingMap.put("Slider Blue Control", ""+sliderBlueControl.getValue());

        game.controlPane.setStyle(newColor);

        game.writeNewStyle();
        writeSettings();
    }

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

    private void setMainStyleTop() {
        style.put("Control Pane Background", "-fx-background-color: #4a752c");

        game.controlPane.setStyle("-fx-background-color: #4a752c");

        game.writeNewStyle();
    }

    private void previewColor(Button colorPreview, Slider slider, String type) {
        switch (type) {
            case "buttons1":
                colorPreview.setStyle("-fx-background-color: rgb(" +
                        sliderRedC1.getValue() + "," + sliderGreenC1.getValue() + "," + sliderBlueC1.getValue() + ")");

                slider.valueProperty().addListener((observable, oldValue, newValue) ->
                        colorPreview.setStyle("-fx-background-color: rgb(" +
                                sliderRedC1.getValue() + "," + sliderGreenC1.getValue() + "," + sliderBlueC1.getValue() + ")"));
                break;
            case "buttons2":
                colorPreview.setStyle("-fx-background-color: rgb(" +
                        sliderRedC2.getValue() + "," + sliderGreenC2.getValue() + "," + sliderBlueC2.getValue() + ")");

                slider.valueProperty().addListener((observable, oldValue, newValue) ->
                        colorPreview.setStyle("-fx-background-color: rgb(" +
                                sliderRedC2.getValue() + "," + sliderGreenC2.getValue() + "," + sliderBlueC2.getValue() + ")"));
                break;
            case "buttons1Clicked":
                colorPreview.setStyle("-fx-background-color: rgb(" +
                        sliderRedC1Clicked.getValue() + "," + sliderGreenC1Clicked.getValue() + "," + sliderBlueC1Clicked.getValue() + ")");

                slider.valueProperty().addListener((observable, oldValue, newValue) ->
                        colorPreview.setStyle("-fx-background-color: rgb(" +
                                sliderRedC1Clicked.getValue() + "," + sliderGreenC1Clicked.getValue() + "," + sliderBlueC1Clicked.getValue() + ")"));
                break;
            case "buttons2Clicked":
                colorPreview.setStyle("-fx-background-color: rgb(" +
                        sliderRedC2Clicked.getValue() + "," + sliderGreenC2Clicked.getValue() + "," + sliderBlueC2Clicked.getValue() + ")");

                slider.valueProperty().addListener((observable, oldValue, newValue) ->
                        colorPreview.setStyle("-fx-background-color: rgb(" +
                                sliderRedC2Clicked.getValue() + "," + sliderGreenC2Clicked.getValue() + "," + sliderBlueC2Clicked.getValue() + ")"));
                break;
            case "top":
                colorPreview.setStyle("-fx-background-color: rgb(" +
                        sliderRedControl.getValue() + "," + sliderGreenControl.getValue() + "," + sliderBlueControl.getValue() + ")");

                slider.valueProperty().addListener((observable, oldValue, newValue) ->
                        colorPreview.setStyle("-fx-background-color: rgb(" +
                                sliderRedControl.getValue() + "," + sliderGreenControl.getValue() + "," + sliderBlueControl.getValue() + ")"));
                break;
        }
    }

    private void readSettings() {
        try {
            settingsIn = Files.newBufferedReader(Paths.get("resources/settings.txt"), Charset.forName("UTF-8"));
            String line = settingsIn.readLine();

            while (line != null) {
                if (line.trim().startsWith("#")) {
                    String key = line.substring(1).trim();
                    line = settingsIn.readLine();

                    settingMap.put(key, line);
                }
                line = settingsIn.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeSettings() {
        try {
            settingsOut = Files.newBufferedWriter(Paths.get("resources/settings.txt"));
            String data = "";

            Set styleSet = settingMap.entrySet();
            Iterator itr = styleSet.iterator();
            while(itr.hasNext()) {
                Map.Entry entry = (Map.Entry) itr.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();

                data += '#' + key + '\n' + value + '\n';
            }

            settingsOut.write(data);
            settingsOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
