package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Donald Oliver
 */
public class ModifyProductController implements Initializable {
    
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
    private TableView<Part> allPartsTable;
    @FXML
    private TableColumn<Part, Integer> partID;
    @FXML
    private TableColumn<Part, String> partName;
    @FXML
    private TableColumn<Part, Integer> partInventoryLevel;
    @FXML
    private TableColumn<Part, Double> partPrice;
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
    private TextField assocPartSearch;
    
    private Product modProduct = null;
    private Product temp = new Product();
    private ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private ObservableList<Part> assocPartInventory = FXCollections.observableArrayList();
    private Inventory inventory = null;
   
    @FXML
    private void cancelButtonClick(ActionEvent event) throws IOException {
        if(dialogueHandler(2)) {
            Parent cancelParent = FXMLLoader.load(getClass().getResource("/Views/Main.fxml"));
            Scene cancelScene = new Scene(cancelParent);
            Stage cancelStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            cancelStage.setScene(cancelScene);
            cancelStage.show();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inventory = MainScreenController.getInventory();
        modProduct = MainScreenController.getModProd();
        initTemp();
        assocPartInventory = temp.getAllAssociatedParts();
        initializeTextFields();
        initializeAllPartsTable(inventory);
        initializeAssociatedPartsTable(temp);
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        assocID.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocName.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocInvLvl.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }    
    
    private void initTemp() {
        
        for (Part p : modProduct.getAllAssociatedParts()) {
            temp.addAssociatedPart(p);
        }
        temp.setId(modProduct.getId());
        temp.setName(modProduct.getName());
        temp.setStock(modProduct.getStock());
        temp.setPrice(modProduct.getPrice());
        temp.setMin(modProduct.getMin());
        temp.setMax(modProduct.getMax());
    }
    
    private void initializeTextFields() {
        productId.setText(Integer.toString(modProduct.getId()));
        productName.setText(modProduct.getName());
        productStock.setText(Integer.toString(modProduct.getStock()));
        productPrice.setText(Double.toString(modProduct.getPrice()));
        productMin.setText(Integer.toString(modProduct.getMin()));
        productMax.setText(Integer.toString(modProduct.getMax()));
    }
    
    private void initializeAllPartsTable(Inventory inventory) {
        partInventory = inventory.getAllParts();
        allPartsTable.setItems(partInventory);
        allPartsTable.refresh();
    }
    
    private void initializeAssociatedPartsTable (Product product) {
        associatedPartsTable.setItems(product.getAllAssociatedParts());
        associatedPartsTable.refresh();
    }
    
    private void getTextFieldValues(){
        temp.setName(productName.getText());
        temp.setPrice(Double.valueOf(productPrice.getText()));
        temp.setStock(Integer.valueOf(productStock.getText()));
        temp.setMin(Integer.valueOf(productMin.getText()));
        temp.setMax(Integer.valueOf(productMax.getText()));
        
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
        if(dialogueHandler(1)) {
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
    @FXML
    private void saveModifiedProduct(ActionEvent event) throws IOException {
        inventory.deleteProduct(modProduct);
        modProduct = new Product();
        getTextFieldValues();
        for (Part p : assocPartInventory) {
            modProduct.addAssociatedPart(p);
        }
        modProduct.setId(temp.getId());
        modProduct.setName(temp.getName());
        modProduct.setPrice(temp.getPrice());
        modProduct.setMin(temp.getMin());
        modProduct.setMax(temp.getMax());
        modProduct.setStock(temp.getStock());
        inventory.addProduct(modProduct);
        if(modProduct.getAllAssociatedParts().isEmpty()) {
            dialogueHandler(0);
            return;
        }
        Parent cancelParent = FXMLLoader.load(getClass().getResource("/Views/Main.fxml"));
        Scene cancelScene = new Scene(cancelParent);
        Stage cancelStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        cancelStage.setScene(cancelScene);
        cancelStage.show();
    }
        private boolean dialogueHandler(int code) {
        switch(code) {
            case 0:
                Alert alert0 = new Alert(Alert.AlertType.ERROR);
                alert0.setTitle("Error");
                alert0.setHeaderText("Invalid Entry:");
                alert0.setContentText("A Product must have at least one associated Part");
                alert0.showAndWait();
                return true;
            case 1:
                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert1.setTitle("Delete Associated Part");
                alert1.setHeaderText("Do you want to delete the selected associated part?");
                alert1.setContentText("Click Ok to confirm");
                Optional<ButtonType> result1 = alert1.showAndWait();
                return result1.get() == ButtonType.OK;
            case 2:
                Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
                alert2.setTitle("Cancel Product Modification");
                alert2.setHeaderText("Canceling will revert Product changes:");
                alert2.setContentText("Click Ok to confirm");
                Optional<ButtonType> result2 = alert2.showAndWait();
                return result2.get() == ButtonType.OK;
        }
        return true;
    }
}
