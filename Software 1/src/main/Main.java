package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

/** This is the main class for the project.*/
public class Main extends Application {

    /** This method adds test data and loads the main screen. If I were going to
     * create a feature to release in the "next update",
     * it would be a see data button on the main screen. This button once
     * clicked would display the data from either the part or product table in
     * a separate window. And if you clicked it again, it would display the
     * data for that second object in the same window. That way you could look
     * at all the characteristics of an object without having to click on modify.
     * I think it would make updating and deleting things easier, especially if
     * your working with a lot of products.*/
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // Add data
        Part o1 = new Outsourced(1, "Bolt", .05, 70, 10, 100, "MachineCorp");
        Part o2 = new Outsourced(2, "Screw", .09, 50, 10, 100, "MachineCorp");
        Part o3 = new Outsourced(3,"Pole", 1.99, 5, 1, 10, "MachineCorp");
        Part i1 = new InHouse(4,"Wheel", 2.99, 10, 4, 26, 10);
        Part i2 = new InHouse(5, "Tube", 4.99, 20, 4, 40, 11);
        Inventory.addPart(o1);
        Inventory.addPart(o2);
        Inventory.addPart(o3);
        Inventory.addPart(i1);
        Inventory.addPart(i2);

        Product p1 = new Product(100, "Bike", 30.50, 8, 1, 15);
        p1.addAssociatedPart(o1);
        p1.addAssociatedPart(o2);
        p1.addAssociatedPart(i1);
        p1.addAssociatedPart(i2);
        Product p2 = new Product(101, "Scooter", 25.99, 7, 2, 15);
        p2.addAssociatedPart(o1);
        p2.addAssociatedPart(o3);
        Inventory.addProduct(p1);
        Inventory.addProduct(p2);

        // Load main screen
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main_Screen.fxml"));
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(root, 1000, 444));
        primaryStage.show();
    }


}
