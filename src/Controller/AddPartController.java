package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Donald Oliver
 */
public class AddPartController implements Initializable {
    @FXML
    private TextField partId;
    @FXML
    private TextField partName;
    @FXML
    private TextField partPrice;
    @FXML
    private TextField partStock;
    @FXML
    private TextField partMin;
    @FXML
    private TextField partMax;
    @FXML
    private TextField partMachineId;
    @FXML
    private TextField companyName;
    @FXML
    private RadioButton inHouse;
    @FXML 
    private RadioButton outsourced;
    @FXML
    private Label machineIdLabel;
    @FXML
    private Label companyNameLabel;
    
    private ToggleGroup group;
    private Inventory inventory = null;
    private Part partToAdd = null;
    private class RadioButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            if(inHouse.isSelected()) {
                machineIdLabel.setVisible(true);
                partMachineId.setVisible(true);
                companyNameLabel.setVisible(false);
                companyName.setVisible(false);
                
                
            }
            else {
                machineIdLabel.setVisible(false);
                partMachineId.setVisible(false);
                companyNameLabel.setVisible(true);
                companyName.setVisible(true);
            }
        }
    }
    @FXML
    private void cancelButtonClick(ActionEvent event) throws IOException {
        if(dialogueHandler()){
            Parent cancelParent = FXMLLoader.load(getClass().getResource("/Views/Main.fxml"));
            Scene cancelScene = new Scene(cancelParent);
            Stage cancelStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            cancelStage.setScene(cancelScene);
            cancelStage.show();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        group = new ToggleGroup();
        inventory = MainScreenController.getInventory();
        inHouse.setToggleGroup(group);
        outsourced.setToggleGroup(group);
        outsourced.setOnAction(new RadioButtonHandler());
        inHouse.setOnAction(new RadioButtonHandler());
        inHouse.setSelected(true);
    }    
    
    private void getFieldValues() {
        if(inHouse.isSelected()){
            partToAdd = new InHouse(0,"",0.0,0,0,0,0);
        }
        if(outsourced.isSelected()){
            partToAdd = new Outsourced(0,"",0,0,0,0,"");
        }
        int totParts = partToAdd.getTotParts();
        partToAdd.setId(totParts++);
        partToAdd.setName(partName.getText());
        partToAdd.setPrice(Double.valueOf(partPrice.getText()));
        partToAdd.setStock(Integer.valueOf(partStock.getText()));
        partToAdd.setMin(Integer.valueOf(partMin.getText()));
        partToAdd.setMax(Integer.valueOf(partMax.getText()));
        
        if(inHouse.isSelected()) {
            ((InHouse)partToAdd).setMachineId(Integer.valueOf(partMachineId.getText()));
        }
        
        if(outsourced.isSelected()) {
            ((Outsourced)partToAdd).setCompanyName(companyName.getText());
        }
    } 
    
    @FXML
    private void savePart(ActionEvent event) throws IOException {
        getFieldValues();
        inventory.addPart(partToAdd);
        Parent cancelParent = FXMLLoader.load(getClass().getResource("/Views/Main.fxml"));
        Scene cancelScene = new Scene(cancelParent);
        Stage cancelStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        cancelStage.setScene(cancelScene);
        cancelStage.show();
        
    }
    private boolean dialogueHandler() {
        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
        alert2.setTitle("Cancel Product Creation");
        alert2.setHeaderText("Canceling will delete new Product Information:");
        alert2.setContentText("Click Ok to confirm");
        Optional<ButtonType> result2 = alert2.showAndWait();
        return result2.get() == ButtonType.OK;
    }
}