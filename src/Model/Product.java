package Model;

import javafx.collections.ObservableList;
import java.util.ArrayList;
import javafx.collections.FXCollections;

/**
 *
 * @author Donald Oliver
 */
public class Product {
    private static int totProducts;
    private ObservableList associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    
    public Product () {}
    
    public Product(int id, String name, double price, int stock, int min, int max) {
        setId(++totProducts*1000);
        setName(name);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);
    }
    public int getTotProducts() {
        return this.totProducts;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setPrice(double price){
        this.price = price;
    }
    
    public void setStock (int stock){
        this.stock = stock;
    }
    
    public void setMin (int min){
        this.min = min;
    }
    
    public void setMax (int max){
        this.max = max;
    }
    
    public int getId(){
        return this.id;
    }
    
    public String getName(){
        return this.name;
    }
    
    public double getPrice(){
        return this.price;
    }
    
    public int getStock(){
        return this.stock;
    }
    
    public int getMin(){
        return this.min;
    }
    
    public int getMax(){
        return this.max;
    }
    
    public void addAssociatedPart(Part part) {
        if (part != null) {
            this.associatedParts.add(part);
        }
    }
    
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        if (selectedAssociatedPart != null) {
            this.associatedParts.remove(selectedAssociatedPart);
        }
        return false;
    }
    
    public ObservableList<Part> getAllAssociatedParts() {
        ObservableList allAssocParts = FXCollections.observableArrayList();
        allAssocParts = associatedParts;
        return allAssocParts;
    }
    
    public void decrementTotProducts() {
        totProducts = --totProducts;
    }
}
