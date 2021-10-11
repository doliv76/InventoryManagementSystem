package Main;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import Model.Product;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 *
 * @author Donald Oliver
 */
public class DonaldOliverInventorySystem extends Application {
    private static Inventory inventory = new Inventory();
    public static Inventory getInventory() {
        return inventory;
    }
    @Override
    public void start(Stage stage) throws Exception { 
        initializeInventory(inventory);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Main.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);        
        stage.setScene(scene);
        stage.show();
    }
    
        private void initializeInventory(Inventory inventory) {
        Part partA1 = new InHouse(1,"Part A1",1.99,30,5,100,101);
        Part partA2 = new InHouse(2,"Part A2",2.50,76,5,100,102);
        Part partA3 = new InHouse(3,"Part A3",11.75,10,5,50,103);
        inventory.addPart(partA1);
        inventory.addPart(partA2);
        inventory.addPart(partA3);
        inventory.addPart(new InHouse(4,"Part A4",23.75,3,2,10,104));
        inventory.addPart(new InHouse(5,"Part A5",0.86,65,5,100,105));
        inventory.addPart(new InHouse(6,"Part A6",6.73,42,5,50,116));
        inventory.addPart(new InHouse(7,"Part A7",0.21,87,5,100,109));
        inventory.addPart(new InHouse(8,"Part A8",19.65,13,5,20,215));
        inventory.addPart(new InHouse(9,"Part A9",3.45,67,5,100,375));
        inventory.addPart(new InHouse(10,"Part A10",18.00,10,5,50,8));
        //Add Outsourced Parts
        Part partA11 = new Outsourced(11,"Part A11",21.26,15,5,50,"Bob's Tool and Die");
        Part partA12 = new Outsourced(12,"Part A12",16.83,23,5,50,"Blammo Co");
        Part partA13 = new Outsourced(13,"Part A13",33.12,14,2,20,"Test,Test, and Test LLC");
        inventory.addPart(partA11);
        inventory.addPart(partA12);
        inventory.addPart(partA13);
        inventory.addPart(new Outsourced(14,"Part A14",0.45,56,5,100,"United Industrial Suppliers"));
        inventory.addPart(new Outsourced(15,"Part A15",48.23,19,2,20,"Leave Co"));
        inventory.addPart(new Outsourced(16,"Part A16",5.12,38,5,100,"Ozark Mountain Machinery"));
        inventory.addPart(new Outsourced(17,"Part A17",32.01,16,2,20,"Consolidated Consolidators"));
        inventory.addPart(new Outsourced(18,"Part A18",17.00,49,5,50,"Food n Stuff"));
        inventory.addPart(new Outsourced(19,"Part A19",5.14,82,5,100,"Complete Food"));
        inventory.addPart(new Outsourced(20,"Part A20",20.13,11,2,20,"JJ's Diner"));
        inventory.addPart(new InHouse(21, "Legacy Part L1", 23.04, 2, 2, 5, 1));
        //Add Products and Associate Parts
        Product product1 = new Product(100, "Product 1", 50.00, 20, 5, 100);
        Product product2 = new Product(200, "Product 2", 25.99, 216, 5, 50);
        Product product3 = new Product(300, "Product 3", 99.99, 20, 10, 75);
        product1.addAssociatedPart(partA1);
        product1.addAssociatedPart(partA11);
        product2.addAssociatedPart(partA2);
        product2.addAssociatedPart(partA12);
        product3.addAssociatedPart(partA3);
        product3.addAssociatedPart(partA13);
        inventory.addProduct(product1);
        inventory.addProduct(product2);
        inventory.addProduct(product3);
        inventory.addProduct(new Product(400,"Product 4",15.99,5,5,100));
        inventory.addProduct(new Product(500,"Product 5",88.79,10,5,20));
    }
            
    public static void main(String[] args) {
        launch(args);
    }
}
