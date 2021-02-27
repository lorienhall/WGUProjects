package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** This is the controller for the main screen. */
public class MainScreenController implements Initializable
{
    Stage stage;
    Parent scene;

    /** Setting the fx id references for the part and product tables */
    // Products table
    @FXML
    private TableView<Product> ProductsTV;
    @FXML
    private TableColumn<Product, Integer> ProductsTVID;
    @FXML
    private TableColumn<Product, String> ProductsTVN;
    @FXML
    private TableColumn<Product, Integer> ProductsTVInv;
    @FXML
    private TableColumn<Product, Double> ProductsTVP;

    // Parts table
    @FXML
    private TableView<Part> PartsTV;
    @FXML
    private TableColumn<Part, Integer> PartsTVID;
    @FXML
    private TableColumn<Part, String> PartsTVN;
    @FXML
    private TableColumn<Part, Integer> PartsTVInv;
    @FXML
    private TableColumn<Part, Double> PartsTVP;

    /** Setting the fx id references for the textfields. */
    @FXML
    private TextField SearchProductsF;
    @FXML
    private TextField SearchPartsF;

    /** Setting the fx id references for the buttons. */
    public Button ExitB;
    public Button DeleteProductB;
    public Button ModifyProductsB;
    public Button AddProductsB;
    public Button SearchProductsB;
    public Button DeletePartB;
    public Button ModifyPartsB;
    public Button AddPartsB;
    public Button SearchPartsB;


    /** The next two methods create the error messages for all the part and product screens. */
    public static void errorPart(int code, TextField field)
    {
        fieldError(field);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error adding part");
        alert.setHeaderText("Cannot add part");

        switch (code) {
            case 1: {
                alert.setContentText("Field is empty!");

                break;
            }
            case 2: {
                alert.setContentText("Please select In House or OutSourced!");
                break;
            }
            case 3: {
                alert.setContentText("Invalid format!");
                break;
            }
            case 4: {
                alert.setContentText("Name is invalid!");
                break;
            }
            case 5: {
                alert.setContentText("Value cannot be negative!");
                break;
            }
            case 6: {
                alert.setContentText("Inventory cannot be lower than min!");
                break;
            }
            case 7: {
                alert.setContentText("Inventory cannot be greater than max!");
                break;
            }
            case 8: {
                alert.setContentText("Min cannot be higher than max!");
                break;
            }
            case 9: {
                alert.setContentText("Machine ID must be a number");
                break;
            }
            case 10: {
                alert.setContentText("You cannot delete a part that is associated with a product!");
                break;
            }
            default: {
                alert.setContentText("Unknown error!");
                break;
            }
        }
        alert.showAndWait();
    }

    public static void errorProduct(int code, TextField field)
    {
        fieldError(field);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error adding product");
        alert.setHeaderText("Cannot add product");

        switch (code) {
            case 1: {
                alert.setContentText("Product must have at least one associated part.");
                break;
            }
            case 2: {
                alert.setContentText("You cannot delete a product that has an associated part.");
                break;
            }
            case 3: {
                alert.setContentText("Field is empty!");
                break;
            }
            case 4: {
                alert.setContentText("Invalid format!");
                break;
            }
            case 5: {
                alert.setContentText("Name is invalid!");
                break;
            }
            case 6: {
                alert.setContentText("Value cannot be negative!");
                break;
            }
            case 7: {
                alert.setContentText("Inventory cannot be lower than min!");
                break;
            }
            case 8: {
                alert.setContentText("Inventory cannot be greater than max!");
                break;
            }
            case 9: {
                alert.setContentText("Min cannot be higher than max!");
                break;
            }
            case 10: {
                alert.setContentText("Product price must be greater than the price of all the parts.");
                break;
            }
            default: {
                alert.setContentText("Unknown Error");
            }
        }
        alert.showAndWait();
    }

    /** This method is used in the error messages to change the style of a textfield to indicate
     * that there is an issue with it. */
    public static void fieldError(TextField field)
    {
        if (field != null){
            field.setStyle("-fx-border-color: red");
        }
    }

    /** This is the initialize method. It sets up the parts and products table to be able to
     * display the necessary data. */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // Set data for the parts table
        PartsTV.setItems(Inventory.getAllParts());

        PartsTVID.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartsTVN.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartsTVInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        PartsTVP.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Set data for the products table
        ProductsTV.setItems(Inventory.getAllProduct());

        ProductsTVID.setCellValueFactory(new PropertyValueFactory<>("id"));
        ProductsTVN.setCellValueFactory(new PropertyValueFactory<>("name"));
        ProductsTVInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ProductsTVP.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    // Part Button Event Handlers
    /** This method sends you to the add part screen when the add button on the parts table is clicked. */
    public void addPartB(javafx.scene.input.MouseEvent mouseEvent) throws IOException
    {
        // Initialize the add part screen
        stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));

        // Change the current scene
        stage.setScene(new Scene(scene, 600, 444));
        stage.show();
    }

    /** This is a static method and attribute for passing data between screens. */
    private static Part modifyPart = null;
    public static Part getmodifyPart() { return modifyPart; }

    /** This method sends you to the modify part screen when the modify button on the parts table is clicked. */
    public void modifyPartB(javafx.event.ActionEvent actionEvent) throws IOException
    {
        Part ap = PartsTV.getSelectionModel().getSelectedItem();
        if(Inventory.getAllParts().isEmpty())
        {
            errorWindow(1);
            return;
        }
        if(!Inventory.getAllParts().isEmpty() && ap == null)
        {
            errorWindow(2);
        }
        else {
            // Set up for data passing
            modifyPart = ap;

            // Load the modify part screen
          stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
          scene = FXMLLoader.load(getClass().getResource("/view/ModifyPart.fxml"));
          stage.setScene(new Scene(scene, 600, 444));
          stage.show();
        }
    }

    /** This method searches all the parts to find a match to search input.
     * It then sets the matches it found to be displayed in the tableview. */
    public void searchParts()
    {
        String q = SearchPartsF.getText();
        ObservableList<Part> parts = Inventory.lookUpPart(q);

        if(parts.size() == 0)
        {
            try
            {
                int ID = Integer.parseInt(q);
                Part p = Inventory.lookupPart(ID);

                if (p != null) {parts.add(p);}
            }
            catch (NumberFormatException e)
            {
                // Ignore
            }
        }

        PartsTV.setItems(parts);
        SearchPartsF.setText("");
    }

    /** This method removes a part from the parts table. It also provides error messages
     * if there was no selected part and if the selected part is associated with
     * a product. And lastly it confirms the deletion of the part. */
    public void deletePartB()
    {
        Part AP = PartsTV.getSelectionModel().getSelectedItem();
        if (AP == null)
        {
            errorWindow(2);
            return;
        }
        // Check all the products to make sure your not deleting a part that is associated with a product
        for (Product product : Inventory.getAllProduct())
        {
            for(Part part : product.getAllAssociatedParts())
            if (AP == part)
            {
                errorPart(3, null);
                return;
            }

        }
    }

    // Product Button Event Handlers
    /** This method sends you to the add product screen when the add button on the products table is clicked. */
    public void addProductB(javafx.event.ActionEvent actionEvent) throws IOException
    {
        // Load the add part screen
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This is a static method and attribute for passing data between screens. */
    private static Product modifyProduct = null;
    public static Product getmodifyProduct() { return modifyProduct; }

    /** This method sends you to the modify product screen when the modify button on the products table is clicked. */
    public void modifyProductB(javafx.event.ActionEvent actionEvent) throws IOException
    {
        Product productData = ProductsTV.getSelectionModel().getSelectedItem();
        // Set up for data passing
        modifyProduct = productData;

        // Load the modify product screen
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ModifyProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /** This method searches all the products to find a match to search input.
     * It then sets the matches it found to be displayed in the tableview. */
    public void searchProducts()
    {
        String q = SearchProductsF.getText();
        ObservableList<Product> products = Inventory.lookUpProduct(q);

        if(products.size() == 0)
        {
            try
            {
                int ID = Integer.parseInt(q);
                Product p = Inventory.lookupProduct(ID);

                if (p != null) {products.add(p);}
            }
            catch (NumberFormatException e)
            {
                // Ignore
            }
        }

        ProductsTV.setItems(products);
        SearchProductsF.setText("");
    }

    /** This method removes a product from the products table. It also provides error messages
     * if there was no selected product and if the selected product has associated parts.
     * And lastly it confirms the deletion of the product. */
    public void deleteProductB()
    {
        Product AP = ProductsTV.getSelectionModel().getSelectedItem();

        if (AP == null)
        {
            errorWindow(2);
            return;
        }
        if (!AP.getAllAssociatedParts().isEmpty())
        {
            errorProduct(2, null);
        }
        // Confirm deletion
        if (confirmationWindowProduct(AP.getName()))
        {
            Inventory.deleteProduct(AP);
            AP.deleteAssociatedPart(AP);
        }

    }

    /** This method exits the program when the exit button is clicked */
    public void exitProgramButton()
    {System.exit(0); }


    /** This method creates some general error windows used throughout this file. */
    private void errorWindow(int code)
    {
        if (code == 1)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Empty Inventory!");
            alert.setContentText("There's nothing to select!");
            alert.showAndWait();
        }
        if (code == 2)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Selection");
            alert.setContentText("You must select an item!");
            alert.showAndWait();
        }
        if (code == 3)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Selection");
            alert.setContentText("You cannot delete a part that is associated with a product!");
            alert.showAndWait();
        }
    }

    /** This method creates the confirmation window that confirms the removal of a part. */
    private boolean confirmationWindowPart(String name)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Part");
        alert.setHeaderText("Are you sure you want to delete: " + name);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /** This method creates the confirmation window that confirms the removal of a product. */
    private boolean confirmationWindowProduct(String name)
    {
         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Product");
        alert.setHeaderText("Are you sure you want to delete: " + name);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}

