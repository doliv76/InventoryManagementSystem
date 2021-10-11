package Model;

import javafx.collections.ObservableList;
import java.util.ArrayList;
import javafx.collections.FXCollections;

/**
 *
 * @author Donald Oliver
 */
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    
    public static void addPart(Part partToAdd){
        if (partToAdd != null) {
            allParts.add(partToAdd);
        }
        return;
    }
   
    public static Part lookupPart(int partId){
        if(!allParts.isEmpty()) {
            for (int i = 0; i < allParts.size(); i++) {
                if(allParts.get(i).getId() == partId) {
                    return allParts.get(i);
                }
            }
        }
        return null;
    }
    
    public static ObservableList<Part> lookupPart (String partName) {
        ObservableList<Part> resultParts = FXCollections.observableArrayList();
        if (!allParts.isEmpty()){
            for (int i = 0; i < allParts.size(); i++) {
                if (allParts.get(i).getName() == partName) {
                    resultParts.add(allParts.get(i));
            }
        }
        return resultParts;
        }
        return null;
    }
    
    public static void updatePart(int index, Part selectedPart){
        for (int i = 0; i<allParts.size(); i++){
            if(allParts.get(i).getId() == selectedPart.getId()) {
                allParts.set(i, selectedPart);
                break;
            }
        }
    }
    
    public static boolean deletePart(Part selectedPart) {
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getId() == selectedPart.getId()) {
                allParts.remove(i);
                return true;
            }
        }
        return false;
    }
    
    public ObservableList<Integer> getPartIdList() {
        ObservableList<Integer> partIds = FXCollections.observableArrayList();
        for (int i = 0; i < allParts.size(); i++) {
            partIds.add(allParts.get(i).getId());
        }
        return partIds;
    }
    
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    
    public void setAllParts(ObservableList<Part> parts) {
        allParts = parts;
            
    }
    
    public static void addProduct(Product productToAdd){
        if (productToAdd != null) {
            allProducts.add(productToAdd);
        }
        return;
    }
    
    public static Product lookupProduct(int productId){
        if(!allProducts.isEmpty()){
            for (int i = 0; i < allProducts.size(); i++) {
                if(allProducts.get(i).getId() == productId){
                    return allProducts.get(i);
                }
            }
        }
        return null;
    }
    
    public static ObservableList<Product> lookupProduct (String productName) {
        ObservableList<Product> resultProducts = FXCollections.observableArrayList();
        if (!allProducts.isEmpty()){
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).getName() == productName) {
                    resultProducts.add(allProducts.get(i));
            }
        }
        return resultProducts;
        }
        return null;
    }
    
    public static void updateProduct(int index, Product selectedProduct) {
        for (int i = 0; i < allProducts.size(); i++) {
            if(allProducts.get(i).getId() == selectedProduct.getId()) {
                allProducts.set(i, selectedProduct);
                break;
            }
        }
    }
    
    public static boolean deleteProduct(Product selectedProduct){
        for (int i = 0; i < allProducts.size(); i++) {
            if(allProducts.get(i).getId() == selectedProduct.getId()) {
                allProducts.remove(i);
                return true;
            }
        }
        return false;
    }
    
    public ObservableList<Integer> getProductIdList() {
        ObservableList<Integer> productIds = FXCollections.observableArrayList();
        for (int i = 0; i < allProducts.size(); i++) {
            productIds.add(allProducts.get(i).getId());
        }
        return productIds;
    }
    
    public ObservableList<Product> getAllProducts() {
        return this.allProducts;
    } 
}
