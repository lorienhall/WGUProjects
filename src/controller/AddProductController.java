package controller;

import javafx.collections.FXCollections;
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
import java.util.regex.Pattern;

/** This is the controller class for the add product screen. */
public class AddProductController implements Initializable {

    /** This is the initialize method. It sets up the parts and associated parts
     * table to be able to display the necessary data. */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Parts table setup
        PartsTV.setItems(Inventory.getAllParts());

        PartsTVID.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartsTVN.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartsTVInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        PartsTVP.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Associated parts table setup
        AssociatedPartT.setItems(associatedPart);

        APartTVID.setCellValueFactory(new PropertyValueFactory<>("id"));
        APartTVN.setCellValueFactory(new PropertyValueFactory<>("name"));
        APartTVInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        APartTVP.setCellValueFactory(new PropertyValueFactory<>("price"));


    }

    /** These are some various declarations */
    private static int ID = 102;
    private ObservableList<Part> associatedPart = FXCollections.observableArrayList();

    Stage stage;
    Parent scene;

    /** Setting the fx id references for the two tables */
    // Associated parts table
    @FXML
    private TableView<Part> AssociatedPartT;

    @FXML
    private TableColumn<Part, Integer> APartTVID;

    @FXML
    private TableColumn<Part, String> APartTVN;

    @FXML
    private TableColumn<Part, Integer> APartTVInv;

    @FXML
    private TableColumn<Part, Double> APartTVP;

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

    /** Setting the fx id references for the buttons. */
    public Button saveProductB;
    public Button AddAssociatedPartB;
    public Button removeAssociatePartB;
    public Button cancelB;
    public Button SearchPartsB;

    /** Setting the fx id references for the textfields. */
    @FXML
    private TextField SearchPartsF;

    @FXML
    private TextField AProductID;

    @FXML
    private TextField AProductName;

    @FXML
    private TextField AProductMin;

    @FXML
    private TextField AProductI;

    @FXML
    private TextField AProductP;

    @FXML
    private TextField AProductMax;

    /** This method saves the information entered into the textfields as a new product
     * and then returns to the main screen. It also validates the data and generates error messages. */
    public void onActionSaveProduct(javafx.event.ActionEvent actionEvent) throws IOException
    {
        // Reset field styles
        resetFieldStyles();

        // Validate data
        if (!validateData())
        {
            return;
        }

        // Get the new data from the text fields
        int id = ID;
        String name = AProductName.getText();
        int stock = Integer.parseInt(AProductI.getText());
        double price = Double.parseDouble(AProductP.getText());
        int min = Integer.parseInt(AProductMin.getText());
        int max = Integer.parseInt(AProductMax.getText());

        // Add new product
        Product p3 = new Product(id, name, price, stock, min, max);
        for (Part part : associatedPart) {
            p3.addAssociatedPart(part);
        }
        Inventory.addProduct(p3);
        ID++;

        // Load main screen
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Main_Screen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This method contains all the checks to ensure all the data is correctly entered. */
    private boolean validateData()
    {
        // Validate data and provide the alerts
        if (associatedPart.isEmpty())
        {
            MainScreenController.errorProduct(1, null);
            return false;
        }
        double tprice = 0;
        for (Part part : associatedPart)
        {
            tprice += part.getPrice();
        }
        // Ensure nothing is empty
        if (AProductName.getText().isEmpty())
        {
            MainScreenController.errorPart(3, AProductName);
            return false;
        }
        if (AProductI.getText().isEmpty())
        {
            MainScreenController.errorPart(3, AProductI);
            return false;
        }
        if (AProductP.getText().isEmpty())
        {
            MainScreenController.errorPart(3, AProductP);
            return false;
        }
        if (AProductMin.getText().isEmpty())
        {
            MainScreenController.errorPart(3, AProductMin);
            return false;
        }
        if (AProductMax.getText().isEmpty())
        {
            MainScreenController.errorPart(3, AProductMax);
            return false;
        }
        // Get the new data from the text fields
        int stock = Integer.parseInt(AProductI.getText());
        double price = Double.parseDouble(AProductP.getText());
        int min = Integer.parseInt(AProductMin.getText());
        int max = Integer.parseInt(AProductMax.getText());
        // Check that there are no negatives
        if (min < 0)
        {
            MainScreenController.errorPart(5, AProductMin);
            return false;
        }
        if (max < 0)
        {
            MainScreenController.errorPart(5, AProductMax);
            return false;
        }
        if (price < 0)
        {
            MainScreenController.errorPart(5, AProductP);
            return false;
        }
        if (stock < 0)
        {
            MainScreenController.errorPart(5, AProductI);
            return false;
        }
        // Check data types
        if (!Pattern.matches("[0-9]+", String.valueOf(stock)))
        {
            MainScreenController.errorPart(3, AProductI);
            return false;
        }
        if (!Pattern.matches("^(\\d{1,3}(,\\d{3})*|(\\d+))(\\.\\d{2})?$", String.valueOf(price)))
        {
            MainScreenController.errorPart(3, AProductP);
            return false;
        }
        if (!Pattern.matches("[0-9]+", String.valueOf(min)))
        {
            MainScreenController.errorPart(3, AProductMin);
            return false;
        }
        if (!Pattern.matches("[0-9]+", String.valueOf(max)))
        {
            MainScreenController.errorPart(3, AProductMax);
            return false;
        }
        // Check data in relation to other data
        if (price < tprice)
        {
            MainScreenController.errorProduct(10, AProductP);
            return false;
        }
        if (stock < min)
        {
            MainScreenController.errorProduct(7, AProductI);
            return false;
        }
        if (stock > max)
        {
            MainScreenController.errorProduct(8, AProductI);
            return false;
        }
        if (min > max)
        {
            MainScreenController.errorPart(9, AProductI);
            return false;
        }
        return true;
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

    /** This method adds a part to the associated parts list for the product. */
    @FXML
    void addAssociatedPart()
    {
        Part ap = PartsTV.getSelectionModel().getSelectedItem();
        if(ap == null)
        {
            return;
        }
        for (Part part : associatedPart)
        {
            if(ap.getId() == part.getId())
            {
                errorWindow(1);
                return;
            }
        }
        associatedPart.add(ap);
        AssociatedPartT.setItems(associatedPart);
    }

    /** This method removes a part from the associated parts list for the product.
     *  It also generates a window asking you to confirm the removal. */
    public void removeAssociatedPart()
    {
        Part AP = AssociatedPartT.getSelectionModel().getSelectedItem();
        if(AP == null)
        {
            return;
        }
        if (confirmationWindowAssociatedPart(AP.getName().trim()))
        {
            associatedPart.remove(AP);
            AssociatedPartT.setItems(associatedPart);
        }
    }

    /** This method cancels the action of creating a new part by returning to the main screen*/
    public void cancelAction(javafx.event.ActionEvent actionEvent) throws IOException
    {
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Main_Screen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This method resets the field styles back to normal in case they were changed by the error messages.*/
    private void resetFieldStyles()
    {
        AProductID.setStyle("-fx-border-color: light gray");
        AProductName.setStyle("-fx-border-color: light gray");
        AProductI.setStyle("-fx-border-color: light gray");
        AProductP.setStyle("-fx-border-color: light gray");
        AProductMin.setStyle("-fx-border-color: light gray");
        AProductMax.setStyle("-fx-border-color: light gray");
    }

    /** This method creates is for errors regarding the associated parts table. */
    private void errorWindow(int code)
    {
        if (code == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Selection");
            alert.setContentText("You cannot add a part to the associated parts list if it is already associated with that product.");
            alert.showAndWait();
        }
    }
    
    /** This method creates the confirmation window that confirms the removal of an associated part. */
    private boolean confirmationWindowAssociatedPart(String name)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Associated Part");
        alert.setHeaderText("Are you sure you want to remove " + name + " from the associated parts list");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

}
