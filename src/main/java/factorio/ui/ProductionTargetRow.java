package factorio.ui;

import factorio.model.ItemStack;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class ProductionTargetRow extends HBox {
    
    private ComboBox<String> itemComboBox;
    private TextField amountField;
    private Label unitLabel;

    public ProductionTargetRow(
        ComboBox<String> timeUnitComboBox
    ){
        setSpacing(10);

        itemComboBox=new ComboBox<>();
        itemComboBox.getItems().addAll(
            "鉄板",
            "銅板",
            "歯車"
        );

        amountField = new TextField();
        unitLabel = new Label(timeUnitComboBox.getValue());

        timeUnitComboBox.valueProperty().addListener(
            (observable,oldValue,newValue) -> {
                unitLabel.setText(newValue);
            }
        );

        getChildren().addAll(
            itemComboBox,
            amountField,
            unitLabel
        );
    }

    public ItemStack toItemStack(double timeCoversionRate){
        String item = itemComboBox.getValue();
        double amount = Double.parseDouble(amountField.getText())*timeCoversionRate;
    
        return new ItemStack(item,amount);
    }
}
