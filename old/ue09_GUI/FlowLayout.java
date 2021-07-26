package ue09_GUI;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import sample.PopUpFx;

public class FlowLayout extends Application{
    public void start(Stage stage) {
        FlowPane flowPane;
        String PopUpAnswer = PopUpFx.readLine("FlowLayout Ausrichtung angeben: H oder V");

        if (PopUpAnswer.equals("H")) {
            stage.setTitle("FlowLayout horizontal");
            flowPane = new FlowPane(Orientation.HORIZONTAL);

        } else if (PopUpAnswer.equals("V")) {
            stage.setTitle("FlowLayout vertical");
            flowPane = new FlowPane(Orientation.VERTICAL);
        } else {
            throw new IllegalArgumentException("Illegal answer: " + PopUpAnswer + "! (legal answers: H or V)");
        }

        Scene scene = new Scene(flowPane);
        stage.setScene(scene);

        flowPane.setPadding(new Insets(20, 0, 20, 20)); // top, right, bottom, left
        flowPane.setVgap(20);
        flowPane.setHgap(30);
        flowPane.setRowValignment(VPos.TOP);
        flowPane.setColumnHalignment(HPos.RIGHT);


        Button play = new Button("play");
        Button rewind = new Button("rewind");
        Button forward = new Button("forward");
        Button pause = new Button("pause");
        Button stop = new Button("stop");

        flowPane.getChildren().addAll(play, rewind, forward, pause, stop);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
