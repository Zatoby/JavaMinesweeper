package ue09_GUI_II;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import sample.PopUpFx;

import java.util.Locale;

public class HBoxLayout extends Application {
    @Override
    public void start(Stage stage) {
        String PopUpDist = PopUpFx.readLine("Abstand zwischen den GUI-Elementen");
        String PopUpBord = PopUpFx.readLine("Rahmen Abstände: oben, rechts, unten, links");
        String PopUpPos = PopUpFx.readLine("Position im Fenster: [oben | mitte | unten], [links| mitte | rechts]");

        String[] PopUpBordSplit = PopUpBord.split("\\,");
        String[] PopUpPosSplit = PopUpPos.split("\\,");

        int v0 = Integer.parseInt(PopUpBordSplit[0].trim());
        int v1 = Integer.parseInt(PopUpBordSplit[1].trim());
        int v2 = Integer.parseInt(PopUpBordSplit[2].trim());
        int v3 = Integer.parseInt(PopUpBordSplit[3].trim());

        stage.setTitle("HBox Layout");
        
        HBox hbox = new HBox();
        Scene scene = new Scene(hbox, 600, 100);
        stage.setScene(scene);

        hbox.setPadding(new Insets(v0, v1, v2, v3));    // Set all sides to 10
        hbox.setSpacing(Integer.parseInt(PopUpDist));                // Gap between nodes

        switch (PopUpPosSplit[0].toLowerCase().trim()) {
            case "oben":
                switch (PopUpPosSplit[1].toLowerCase().trim()) {
                    case "links":
                        hbox.setAlignment(Pos.TOP_LEFT);
                        break;
                    case "mitte":
                        hbox.setAlignment(Pos.TOP_CENTER);
                        break;
                    case "rechts":
                        hbox.setAlignment(Pos.TOP_RIGHT);
                        break;
                    default:
                        throw new IllegalArgumentException("Nur links, mitte, und rechts als Eingabe erlaubt.");
                }
                break;
            case "mitte":
                switch (PopUpPosSplit[1].toLowerCase().trim()) {
                    case "links":
                        hbox.setAlignment(Pos.CENTER_LEFT);
                        break;
                    case "mitte":
                        hbox.setAlignment(Pos.CENTER);
                        break;
                    case "rechts":
                        hbox.setAlignment(Pos.CENTER_RIGHT);
                        break;
                    default:
                        throw new IllegalArgumentException("Nur links, mitte, und rechts als Eingabe erlaubt.");
                }
                break;
            case "unten":
                switch (PopUpPosSplit[1].toLowerCase().trim()) {
                    case "links":
                        hbox.setAlignment(Pos.BOTTOM_LEFT);
                        break;
                    case "mitte":
                        hbox.setAlignment(Pos.BOTTOM_CENTER);
                        break;
                    case "rechts":
                        hbox.setAlignment(Pos.BOTTOM_RIGHT);
                        break;
                    default:
                        throw new IllegalArgumentException("Nur links, mitte, und rechts als Eingabe erlaubt.");
                }
                break;
            default:
                throw new IllegalArgumentException("Nur oben, mitte, und unten als Eingabe erlaubt.");
        }

        Label text1 = new Label("Label1");
        Label text2 = new Label("Label2");
        Button button1 = new Button("Button1");
        Button button2 = new Button("Button2");

//		button1.setMaxHeight(Double.MAX_VALUE); // to show stretching
//		button2.setMaxHeight(Double.MAX_VALUE);

        hbox.getChildren().addAll(text1, button1, text2, button2);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

