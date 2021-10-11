package Controller;

import Model.*;
import java.io.IOException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
/**
 * FXML Controller class
 *
 * @author Donald Oliver
 */
public class AddProductController implements Initializable {

    @FXML
    private TableView<Part> allPartsTable;    
    @FXML
    private TableColumn<Part, Integer> PartID;
    @FXML
    private TableColumn<Part, String> PartName;
    @FXML
    private TableColumn<Part, Integer> PartInventoryLevel;
    @FXML
    private TableColumn<Part, Double> PartPrice;
    @FXML
    private TableView<Part> associatedPartsTable;
    @FXML
    private TableColumn<Part, Integer> assocID;
    @FXML
    private TableColumn<Part, String> assocName;
    @FXML
    private TableColumn<Part, Integer> assocInvLvl;
    @FXML
    private TableColumn<Part, Double> assocPrice;
    @FXML
    private TextField productId;
    @FXML
    private TextField productName;
    @FXML
    private TextField productStock;
    @FXML
    private TextField productPrice;
    @FXML
    private TextField productMin;
    @FXML
    private TextField productMax;
    @FXML
    private TextField assocPartSearch;

    private Product newProduct = null;
    private Inventory inventory = null; 
    private ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private ObservableList<Part> assocPartInventory = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inventory = MainScreenController.getInventory();
        initializeAllPartsTable(inventory);
        PartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        PartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        assocID.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocName.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocInvLvl.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }   
    
    @FXML
    private void cancelButtonClick(ActionEvent event) throws IOException {
        if(dialogueHandler(2)) {
            Parent addProductParent = FXMLLoader.load(getClass().getResource("/Views/Main.fxml"));
            Scene addProductScene = new Scene(addProductParent);
            Stage addProductStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            addProductStage.setScene(addProductScene);
            addProductStage.show();
        }
    } 
    
    @FXML
    private void addAssociatedPart(ActionEvent event) throws IOException{
        Part partAdded = allPartsTable.getSelectionModel().getSelectedItem();
        if (partAdded != null) {
            assocPartInventory.add(partAdded);
            associatedPartsTable.setItems(assocPartInventory);
            associatedPartsTable.refresh();
        }
    }
    
    @FXML
    private void deleteAssociatedPart(ActionEvent event) throws IOException {
        if (dialogueHandler(1)) {
            Part partDeleted = associatedPartsTable.getSelectionModel().getSelectedItem();
            if (partDeleted != null) {
                assocPartInventory.remove(partDeleted);
                associatedPartsTable.setItems(assocPartInventory);
                associatedPartsTable.refresh();
            }
        }
    }
    
    @FXML
    private void partSearchHandler(ActionEvent event) {
        String searchTerm = assocPartSearch.getText();
        ObservableList<Part> updateParts = FXCollections.observableArrayList();
        //will implement with a loop first, but let's come back to this and implement the most efficient way to do this
        for(Part p : partInventory) {
            if (Integer.toString(p.getId()).equals(searchTerm) || p.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                updateParts.add(p);
                allPartsTable.getSelectionModel().select(p);
        }
            allPartsTable.setItems(updateParts);
            allPartsTable.refresh();
        }
    }
    
    @FXML
    private void partKeyHandler (KeyEvent e) {
        if (e.getCode().toString().equals("ENTER")) {
            ActionEvent search = new ActionEvent();
            this.partSearchHandler(search);
            
        }
        else if (e.getCode().toString().equals("BACK_SPACE") && assocPartSearch.getText().isEmpty()) {
            allPartsTable.setItems(partInventory);
            allPartsTable.refresh();
            allPartsTable.getSelectionModel().clearSelection();
        }
    }
    
    private void initializeAllPartsTable(Inventory inventory) {
        partInventory = inventory.getAllParts();
        allPartsTable.setItems(partInventory);
        allPartsTable.refresh();
    }
    
        private void getFieldValues() {
        int totProducts = newProduct.getTotProducts();
        newProduct.setId(1000*totProducts++);
        newProduct.setName(productName.getText());
        newProduct.setPrice(Double.valueOf(productPrice.getText()));
        newProduct.setStock(Integer.valueOf(productStock.getText()));
        newProduct.setMin(Integer.valueOf(productMin.getText()));
        newProduct.setMax(Integer.valueOf(productMax.getText()));
        
    } 
    
    @FXML
    private void saveProduct(ActionEvent event) throws IOException {
        newProduct = new Product(0,"",0.0,0,0,0);
        getFieldValues();
        for (Part p : assocPartInventory) {
            newProduct.addAssociatedPart(p);
        }
        if(newProduct.getAllAssociatedParts().isEmpty()) {
            dialogueHandler(0);
            newProduct.decrementTotProducts();
            return;
        }
        inventory.addProduct(newProduct);
        Parent cancelParent = FXMLLoader.load(getClass().getResource("/Views/Main.fxml"));
        Scene cancelScene = new Scene(cancelParent);
        Stage cancelStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        cancelStage.setScene(cancelScene);
        cancelStage.show();
    }
    
    private boolean dialogueHandler(int code) {
        switch(code) {
            case 0:
                Alert alert0 = new Alert(AlertType.ERROR);
                alert0.setTitle("Error");
                alert0.setHeaderText("Invalid Entry:");
                alert0.setContentText("A Product must have at least one associated Part");
                alert0.showAndWait();
                return true;
            case 1:
                Alert alert1 = new Alert(AlertType.CONFIRMATION);
                alert1.setTitle("Delete Associated Part");
                alert1.setHeaderText("Do you want to delete the selected associated part?");
                alert1.setContentText("Click Ok to confirm");
                Optional<ButtonType> result1 = alert1.showAndWait();
                return result1.get() == ButtonType.OK;
            case 2:
                Alert alert2 = new Alert(AlertType.CONFIRMATION);
                alert2.setTitle("Cancel Product Creation");
                alert2.setHeaderText("Canceling will delete new Product Information:");
                alert2.setContentText("Click Ok to confirm");
                Optional<ButtonType> result2 = alert2.showAndWait();
                return result2.get() == ButtonType.OK;
        }
        return true;
    }
}
