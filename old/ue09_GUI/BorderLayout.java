package ue09_GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import sample.PopUpFx;

public class BorderLayout extends Application {
    public void start(Stage stage) {
        FlowPane flowPane;
        String PopUpAnswer = PopUpFx.readLine("Kombination von: TCBLR");

        stage.setTitle("BorderLayout");
        stage.setMinWidth(200);
        stage.setMinHeight(200);
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane, 400, 300);
        stage.setScene(scene);

        if (PopUpAnswer.contains("T")) {
            Button button_top = new Button("top");
            //button_top.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            borderPane.setTop(button_top);
        }
        if (PopUpAnswer.contains("C")) {
            Button button_center = new Button("center");
            button_center.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            borderPane.setCenter(button_center);
        }
        if (PopUpAnswer.contains("B")) {
            Button button_bottom = new Button("bottom");
            button_bottom.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            borderPane.setBottom(button_bottom);
        }
        if (PopUpAnswer.contains("L")) {
            Button button_left = new Button("left");
            button_left.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            borderPane.setLeft(button_left);
        }
        if (PopUpAnswer.contains("R")) {
            Button button_right = new Button("right");
            button_right.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            borderPane.setRight(button_right);
        }

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
