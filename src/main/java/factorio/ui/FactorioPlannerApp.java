package factorio.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class FactorioPlannerApp extends Application{
    @Override
    public void start(Stage stage){
        Label label = new Label("Factorio Planner");

        Scene scene = new Scene(label,800,600);

        stage.setTitle("Factorio Planner");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
