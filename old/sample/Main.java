package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Hello World");
        StackPane pane = new StackPane();
        primaryStage.setScene(new Scene(pane, 300, 275));

        Button b1 = new Button("Klick mich");

        pane.getChildren().add(b1);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
