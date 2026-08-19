package factorio.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FactorioPlannerApp extends Application{
    private VBox targetRows;
    private ComboBox<String> timeUnitComboBox;

    @Override
    public void start(Stage stage){
        VBox productionTargetPane = createProductionTargetPane();

        VBox productionSettingPane = createProductionSettingPane();

        HBox root = new HBox(
            productionTargetPane,
            productionSettingPane
        );
        root.setPadding(new Insets(20));

        HBox.setHgrow(productionTargetPane,Priority.ALWAYS);
        HBox.setHgrow(productionSettingPane,Priority.ALWAYS);

        Scene scene = new Scene(root,800,600);

        stage.setTitle("Factorio Planner");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }

    private void addTargetRow(){
        targetRows.getChildren().add(
            new ProductionTargetRow(timeUnitComboBox)
        );
    }

    private VBox createProductionTargetPane(){
        VBox productionTargetPane = new VBox();
        productionTargetPane.setSpacing(10);

        HBox targetHeader = new HBox();
        targetHeader.setSpacing(10);
        
        Label targetLabel = new Label("生産目標");
        targetLabel.setStyle("-fx-font-size:18px");

        timeUnitComboBox = new ComboBox<>();
        timeUnitComboBox.getItems().addAll(
            "/sec","/min"
        );
        timeUnitComboBox.setValue("/sec");

        targetHeader.getChildren().addAll(
            targetLabel,
            timeUnitComboBox
        );

        targetRows = new VBox();
        targetRows.setSpacing(10);
        targetRows.getChildren().add(
            new ProductionTargetRow(timeUnitComboBox)
        );

        Button addTargetButton = new Button("+ 生産目標を追加");
        addTargetButton.setOnAction(
            event -> addTargetRow()
        );

        productionTargetPane.getChildren().addAll(
            targetHeader,
            targetRows,
            addTargetButton
        );

        return productionTargetPane;
    }

    private VBox createProductionSettingPane(){
        VBox productionSettingPane = new VBox();
        productionSettingPane.getChildren().add(
            new Label("生産設定")
        );

        return productionSettingPane;
    }
}
