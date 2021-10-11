package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
public class ModifyPartController implements Initializable {
    @FXML
    private TextField partId;
    @FXML
    private TextField partName;
    @FXML
    private TextField partStock;
    @FXML
    private TextField partPrice;
    @FXML
    private TextField partMin;
    @FXML
    private TextField partMax;
    @FXML
    private TextField partMachineId;
    @FXML
    private TextField companyName;
    @FXML
    private Label machineIdLabel;
    @FXML
    private Label companyNameLabel;
    @FXML
    private RadioButton inHouse;
    @FXML 
    private RadioButton outsourced;
    
    private Part modPart;
    private ToggleGroup group;
    private Inventory inventory;
    private ObservableList<Part> partList = FXCollections.observableArrayList();
    private class RadioButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event){
            if(inHouse.isSelected()) {
                machineIdLabel.setVisible(true);
                partMachineId.setVisible(true);
                companyNameLabel.setVisible(false);
                companyName.setVisible(false);   
            }
            if(outsourced.isSelected()) {
                machineIdLabel.setVisible(false);
                partMachineId.setVisible(false);
                companyNameLabel.setVisible(true);
                companyName.setVisible(true);
            }
        }
    }

    @FXML
    private void cancelButtonClick(ActionEvent event) throws IOException {
        if(dialogueHandler()) {
            Parent cancelParent = FXMLLoader.load(getClass().getResource("/Views/Main.fxml"));
            Scene cancelScene = new Scene(cancelParent);
            Stage cancelStage = (Stage)((Node) event.getSource()).getScene().getWindow();
            cancelStage.setScene(cancelScene);
            cancelStage.show();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        group = new ToggleGroup();
        modPart = MainScreenController.getModPart();
        inventory = MainScreenController.getInventory();
        partList = inventory.getAllParts();
        inHouse.setToggleGroup(group);
        outsourced.setToggleGroup(group);
        inHouse.setOnAction(new RadioButtonHandler());
        outsourced.setOnAction(new RadioButtonHandler());
        initializeRadioButtons();
        initializeTextFields();
        
    }
    private void initializeRadioButtons() {
        if(modPart.getClass().toString().equals("class Model.InHouse")) {
            inHouse.setSelected(true);
        }
        
        if(modPart.getClass().toString().equals("class Model.Outsourced")) {
            outsourced.setSelected(true);
        }
    }
    
    private void initializeTextFields() {
        partId.setText(Integer.toString(modPart.getId()));
        partName.setText(modPart.getName());
        partStock.setText(Integer.toString(modPart.getStock()));
        partPrice.setText(Double.toString(modPart.getPrice()));
        partMin.setText(Integer.toString(modPart.getMin()));
        partMax.setText(Integer.toString(modPart.getMax()));
        
        if(inHouse.isSelected()){
            partMachineId.setText(Integer.toString(((InHouse)modPart).getMachineId()));
            machineIdLabel.setVisible(true);
            partMachineId.setVisible(true);
        }
        
        if(outsourced.isSelected()) {
           companyName.setText(((Outsourced)modPart).getCompanyName());
           companyNameLabel.setVisible(true);
           companyName.setVisible(true);
        }
    }
    
    private void getFieldValues() {
        int id = modPart.getId();
        if(inHouse.isSelected()){
            modPart = new InHouse(0,"",0.0,0,0,0,0);
            modPart.setId(id);
        }
        if(outsourced.isSelected()){
            modPart = new Outsourced(0,"",0,0,0,0,"");
            modPart.setId(id);
        }
        modPart.setName(partName.getText());
        modPart.setPrice(Double.valueOf(partPrice.getText()));
        modPart.setStock(Integer.valueOf(partStock.getText()));
        modPart.setMin(Integer.valueOf(partMin.getText()));
        modPart.setMax(Integer.valueOf(partMax.getText()));
        
        if(inHouse.isSelected()) {
            ((InHouse)modPart).setMachineId(Integer.valueOf(partMachineId.getText()));
        }
        
        if(outsourced.isSelected()) {
            ((Outsourced)modPart).setCompanyName(companyName.getText());
        }
    }
    
    private void updateInventory() {
        ObservableList<Part> newParts = FXCollections.observableArrayList();
        newParts = partList;
        int size = newParts.size();
        for(int i = 0; i < size; i++) {
            Part temp = newParts.get(i);
            if(modPart.getId() == temp.getId()) {
                newParts.set(i, modPart);
            }
        }
        inventory.setAllParts(newParts);
    }
    
    @FXML
    private void savePart(ActionEvent event) throws IOException {
        getFieldValues();
        updateInventory();
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
