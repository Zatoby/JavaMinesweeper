package gridPane;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Polygon;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GridPane_Sample extends Application {

	@Override
	public void start(Stage stage) {
		stage.setTitle("GridPane Sample");
		stage.setMinWidth(390);
		stage.setMinHeight(200);

		GridPane gridPane = new GridPane();
		Scene scene = new Scene(gridPane, 300, 200); // w, h
		stage.setScene(scene);

		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(10)); // top, right, bottom, left

		Button button1 = new Button("button1");
		Button button2 = new Button("button2");
		Button button3 = new Button("button3");
		Button button4 = new Button("button4");
		Button button5 = new Button("button5");
		Button button6 = new Button("button6");

		/*
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		button1.setPrefSize(screenBounds.getWidth(), screenBounds.getHeight());
		button2.setPrefSize(screenBounds.getWidth(), screenBounds.getHeight());
		button3.setPrefSize(screenBounds.getWidth(), screenBounds.getHeight());
		button4.setPrefSize(screenBounds.getWidth(), screenBounds.getHeight());
		button5.setPrefSize(screenBounds.getWidth(), screenBounds.getHeight());
		button6.setPrefSize(screenBounds.getWidth(), screenBounds.getHeight());
		
		 */
		
//		button1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // w, h
//		button2.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
//		button3.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
//		button4.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
//		button5.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
//		button6.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		Polygon triangle = new Polygon();
		triangle.getPoints().addAll(new Double[]{
				0.0, 0.0,
				20.0, 30.0,
				-20.0, 30.0,
		});

		gridPane.add(button1, 0, 0, 1, 2);  // col 1, row 1, colspan 1, rowspan 2
		gridPane.add(button2, 1, 0);  		// col 2, row 1
		gridPane.add(button3, 2, 0);  		// col 3, row 1
		gridPane.add(button4, 1, 1, 2, 1);  // col 2, row 2, colspan 2, rowspan 1
		gridPane.add(button5, 0, 2);   		// col 1, row 3
		gridPane.add(button6, 3, 3);   		// col 4, row 4
		gridPane.add(triangle, 3, 4);   		// col 4, row 4

		GridPane.setValignment(button1, VPos.CENTER);
		GridPane.setHalignment(button4, HPos.CENTER);

	  //gridPane.setGridLinesVisible(true); // uncomment the line to see the grid
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
