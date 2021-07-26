package ue09_GUI;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Gui_1 extends Application {
    @Override
    public void start(Stage stage) {
        Group group = new Group(); // empty group
        Scene scene = new Scene(group); // assign group to scene
        stage.setScene(scene); // assign scene to stage
        stage.show(); // show the window

        stage.setTitle("GUI 1 - empty");
        stage.setMinWidth(200D); // double!
        stage.setMinHeight(200D);


        stage.setX(200D);
        stage.setY(200D);


        Label label1 = new Label("Hello"); // label mit Text
        label1.setLayoutX(10); // x position relative to window
        label1.setLayoutY(20);

        Label label2 = new Label(); // empty
        label2.setText("World!");
        label2.setLayoutX(10);
        label2.setLayoutY(40);

        group.getChildren().addAll(label1, label2); // add all labels
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}