package Controller;

import Main.DonaldOliverInventorySystem;
import Model.InHouse;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import Model.Product;
import static java.lang.System.console;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Donald Oliver
 */
public class MainScreenController implements Initializable {

    Inventory inventory;
    @FXML
    private TextField searchProductsField;
    @FXML
    private TableView<Product> productsTable;
    @FXML
    private TableColumn<Product, Integer> ProductID;
    @FXML
    private TableColumn<Product, String> ProductName;
    @FXML
    private TableColumn<Product, Integer> ProductInventoryLevel;
    @FXML
    private TableColumn<Product, Double> ProductPrice;
    @FXML
    private TextField searchPartsField;
    @FXML
    private TableView<Part> partsTable;
    @FXML
    private TableColumn<Part, Integer> PartID;
    @FXML
    private TableColumn<Part, String> PartName;
    @FXML
    private TableColumn<Part, Integer> PartInventoryLevel;
    @FXML
    private TableColumn<Part, Double> PartPrice;

    private ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private ObservableList<Product> productInventory = FXCollections.observableArrayList();

    private ObservableList<Integer> partIdList;
    private ObservableList<Integer> productIdList;

    private static Inventory inventoryData = null;

    public static Inventory getInventory() {
        return inventoryData;
    }
    private static Product modProd = null;

    public static Product getModProd() {
        return modProd;
    }
    private static Part modPart = null;

    public static Part getModPart() {
        return modPart;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inventory = DonaldOliverInventorySystem.getInventory();
        partIdList = inventory.getPartIdList();
        productIdList = inventory.getProductIdList();
        initializePartsTable();
        initializeProductsTable();
        PartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        PartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        ProductID.setCellValueFactory(new PropertyValueFactory<>("id"));
        ProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ProductInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    private void initializePartsTable() {
        if (!partIdList.isEmpty()) {
            for (int i = 0; i < partIdList.size(); i++) {
                partInventory.add(inventory.lookupPart(partIdList.get(i)));
            }
        }
        partsTable.setItems(partInventory);
        partsTable.refresh();
    }

    private void initializeProductsTable() {
        if (!productIdList.isEmpty()) {
            for (int i = 0; i < productIdList.size(); i++) {
                productInventory.add(inventory.lookupProduct(productIdList.get(i)));
            }
        }
        productsTable.setItems(productInventory);
        productsTable.refresh();
    }

    @FXML
    private void partSearchHandler(ActionEvent event) {
        String searchTerm = searchPartsField.getText();
        ObservableList<Part> updateParts = FXCollections.observableArrayList();
        //will implement with a loop first, but let's come back to this and implement the most efficient way to do this
        for (Part p : partInventory) {
            if (Integer.toString(p.getId()).equals(searchTerm) || p.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                updateParts.add(p);
                partsTable.getSelectionModel().select(p);
            }
            partsTable.setItems(updateParts);
            partsTable.refresh();
        }
    }

    @FXML
    private void partKeyHandler(KeyEvent e) {
        if (e.getCode().toString().equals("ENTER")) {
            ActionEvent search = new ActionEvent();
            this.partSearchHandler(search);

        } else if (e.getCode().toString().equals("BACK_SPACE") && searchPartsField.getText().isEmpty()) {
            partsTable.setItems(partInventory);
            partsTable.refresh();
            partsTable.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void productKeyHandler(KeyEvent e) {
        if (e.getCode().toString().equals("ENTER")) {
            ActionEvent search = new ActionEvent();
            this.productSearchHandler(search);

        } else if (e.getCode().toString().equals("BACK_SPACE") && searchProductsField.getText().isEmpty()) {
            productsTable.setItems(productInventory);
            productsTable.refresh();
            productsTable.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void productSearchHandler(ActionEvent event) {
        String searchTerm = searchProductsField.getText();
        ObservableList updateProducts = FXCollections.observableArrayList();
        for (Product p : productInventory) {
            if (Integer.toString(p.getId()).equals(searchTerm) || p.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                productsTable.getSelectionModel().select(p);
                updateProducts.add(p);
            }
        }
        productsTable.setItems(updateProducts);
        productsTable.refresh();
    }

    @FXML
    private void addPartButtonClick(ActionEvent event) throws IOException {
        inventoryData = inventory;
        Parent addPartParent = FXMLLoader.load(getClass().getResource("/Views/AddPart.fxml"));
        Scene addPartScene = new Scene(addPartParent);
        Stage addPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();

    }

    @FXML
    private void addProductButtonClick(ActionEvent event) throws IOException {
        inventoryData = inventory;
        Parent addProductParent = FXMLLoader.load(getClass().getResource("/Views/AddProduct.fxml"));
        Scene addProductScene = new Scene(addProductParent);
        Stage addProductStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addProductStage.setScene(addProductScene);
        addProductStage.show();
    }

    @FXML
    private void modifyPartButtonClick(ActionEvent event) throws IOException {
        inventoryData = inventory;
        modPart = partsTable.getSelectionModel().getSelectedItem();
        if (modPart != null) {
            Parent modifyPartParent = FXMLLoader.load(getClass().getResource("/Views/ModifyPart.fxml"));
            Scene modifyPartScene = new Scene(modifyPartParent);
            Stage modifyPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            modifyPartStage.setScene(modifyPartScene);
            modifyPartStage.show();
        }
    }

    @FXML
    private void modifyProductButtonClick(ActionEvent event) throws IOException {
        inventoryData = inventory;
        modProd = productsTable.getSelectionModel().getSelectedItem();
        if (modProd != null) {
            Parent modifyProductParent = FXMLLoader.load(getClass().getResource("/Views/ModifyProduct.fxml"));
            Scene modifyProductScene = new Scene(modifyProductParent);
            Stage modifyProductStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            modifyProductStage.setScene(modifyProductScene);
            modifyProductStage.show();
        }
    }

    @FXML
    private void deleteProductButtonClick(ActionEvent event) throws IOException {
        if (dialogueHandler(1)) {
            Product remove = productsTable.getSelectionModel().getSelectedItem();
            if (remove != null) {
                for (Product p : productInventory) {
                    if (remove.getId() == p.getId()) {
                        productInventory.remove(p);
                        searchProductsField.clear();
                        productsTable.setItems(productInventory);
                        productsTable.refresh();
                        inventory.deleteProduct(p);
                        break;
                    }
                }
            }
        }
    }

    @FXML
    private void deletePartButtonClick(ActionEvent event) throws IOException {
        if (dialogueHandler(0)) {
            Part remove = partsTable.getSelectionModel().getSelectedItem();
            if (remove != null) {
                for (Part p : partInventory) {
                    if (remove.getId() == p.getId()) {
                        partInventory.remove(p);
                        searchPartsField.clear();
                        partsTable.setItems(partInventory);
                        partsTable.refresh();
                        inventory.deletePart(p);
                        break;
                    }
                }
            }
        }
    }

    @FXML
    private void exitButtonClick(ActionEvent event) throws IOException {
        if (dialogueHandler(2)) {
            Stage exitStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            exitStage.close();
        }
    }

    private boolean dialogueHandler(int code) {
        switch (code) {
            case 0:
                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert1.setTitle("Delete Inventory Part");
                alert1.setHeaderText("Part will be removed from the master inventory on confirmation");
                alert1.setContentText("Click Ok to confirm");
                Optional<ButtonType> result1 = alert1.showAndWait();
                return result1.get() == ButtonType.OK;
            case 1:
                Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
                alert2.setTitle("Delete Inventory Product");
                alert2.setHeaderText("Product will be removed from the master inventory on confirmation");
                alert2.setContentText("Click Ok to confirm");
                Optional<ButtonType> result2 = alert2.showAndWait();
                return result2.get() == ButtonType.OK;
            case 2:
                Alert alert3 = new Alert(Alert.AlertType.CONFIRMATION);
                alert3.setTitle("Exit the Program");
                alert3.setHeaderText("Confirm to Exit");
                alert3.setContentText("Click Ok to confirm");
                Optional<ButtonType> result3 = alert3.showAndWait();
                return result3.get() == ButtonType.OK;
        }
        return true;
    }

}
