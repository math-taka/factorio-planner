package factorio.ui;

import java.util.List;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProductionFacilitySettingRow extends VBox{
    private TextField productivityField;
    private ComboBox<String> kindComboBox;
    
    public ProductionFacilitySettingRow(String facilityType,List<String> facilityKinds){
        Label nameLabel = new Label(facilityType);
        
        HBox productivityRow = new HBox();
        productivityRow.setSpacing(10);
        Label productivityLabel = new Label(" 生産性ボーナス (%)");
        productivityField = new TextField();
        productivityRow.getChildren().addAll(
            productivityLabel,productivityField
        );

        HBox kindRow = new HBox();
        kindRow.setSpacing(10);
        Label kindLabel = new Label(" 生産設備");
        kindComboBox = new ComboBox<>();
        kindComboBox.getItems().addAll(facilityKinds);
        kindRow.getChildren().addAll(
            kindLabel,kindComboBox
        );

        getChildren().addAll(
            nameLabel,productivityRow,kindRow
        );
    }
}
