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

/** This class is the controller for the modify product screen.*/
public class ModifyProductController implements Initializable {


    private ObservableList<Part> associatedPart = FXCollections.observableArrayList();
    /** These are for getting the product data from the main screen. */
    private Product product = null;
    Stage stage;
    Parent scene;

    /** This is the initialize method. It set the textfields and sets up the tables to be able to hold the necessary data.*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Get data from the main screen
        product = MainScreenController.getmodifyProduct();

        // Set associated parts table data
        for (int i = 0; i < 10000; i++)
        {
            Part part = product.lookupAssociatedPart(i);
            if (part != null){associatedPart.add(part);}
        }


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

        // Display product data
        MProductID.setText(String.valueOf(product.getId()));
        MProductN.setText(product.getName());
        MProductI.setText(String.valueOf(product.getStock()));
        MProductP.setText(String.valueOf(product.getPrice()));
        MProductMin.setText(String.valueOf(product.getMin()));
        MProductMax.setText(String.valueOf(product.getMax()));
        associatedPart = product.getAllAssociatedParts();

    }

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
    public Button AddAssociatedPartB;
    public Button removeAssociatedPartB;
    public Button SaveB;
    public Button CancelB;
    public Button SearchPartsB;
    public Button AddProductsB;

    /** Setting the fx id references for the textfields. */
    @FXML
    private TextField MProductID;

    @FXML
    private TextField MProductN;

    @FXML
    private TextField MProductMin;

    @FXML
    private TextField MProductI;

    @FXML
    private TextField MProductP;

    @FXML
    private TextField MProductMax;

    @FXML
    private TextField SearchPartsF;

    /** This method searches all the parts to find a match to search input.
     * It then sets the matches it found to be displayed in the tableview.*/
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
     *  It also generates a window asking you to confirm the removal.*/
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

    /** This method saves the information entered into the textfields as a new product, deletes the old product,
     * and then returns to the main screen. It also validates the data and generates error messages. I had a
     * lot of trouble getting my associated parts table to work, mainly in the modify product screen. You can
     * see the result below. I had to use a for loop to add all the associated parts to the associated parts list.
     * As well as make a new product and use that product to update the old product.*/
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
       int id = Integer.parseInt(MProductID.getText());
       String name = MProductN.getText();
       int stock = Integer.parseInt(MProductI.getText());
       double price = Double.parseDouble(MProductP.getText());
       int min = Integer.parseInt(MProductMin.getText());
       int max = Integer.parseInt(MProductMax.getText());

        // Update the product
        Product p4 = new Product(id, name, price, stock, min, max);
       for (Part part : associatedPart)
       {
           p4.addAssociatedPart(part);
       }
        Inventory.updateProduct(p4);


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
        if (MProductN.getText().trim().isEmpty())
        {
            MainScreenController.errorPart(5, MProductN);
            return false;
        }
        if (MProductI.getText().isEmpty())
        {
            MainScreenController.errorPart(3, MProductI);
            return false;
        }
        if (MProductP.getText().isEmpty())
        {
            MainScreenController.errorPart(3, MProductP);
            return false;
        }
        if (MProductMin.getText().isEmpty())
        {
            MainScreenController.errorPart(3, MProductMin);
            return false;
        }
        if (MProductMax.getText().isEmpty())
        {
            MainScreenController.errorPart(3, MProductMax);
            return false;
        }
        // Get the new data from the text fields
        int id = Integer.parseInt(MProductID.getText());
        String name = MProductN.getText();
        int stock = Integer.parseInt(MProductI.getText());
        double price = Double.parseDouble(MProductP.getText());
        int min = Integer.parseInt(MProductMin.getText());
        int max = Integer.parseInt(MProductMax.getText());
        // Check that there are no negatives
        if (min < 0)
        {
            MainScreenController.errorPart(5, MProductMin);
            return false;
        }
        if (max < 0)
        {
            MainScreenController.errorPart(5, MProductMax);
            return false;
        }
        if (price < 0)
        {
            MainScreenController.errorPart(5, MProductP);
            return false;
        }
        // Check data types
        if (!Pattern.matches("[0-9]+", String.valueOf(stock)))
        {
            MainScreenController.errorPart(3, MProductI);
            return false;
        }
        if (!Pattern.matches("^(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$", String.valueOf(price)))
        {
            MainScreenController.errorPart(3, MProductP);
            return false;
        }
        if (!Pattern.matches("[0-9]+", String.valueOf(min)))
        {
            MainScreenController.errorPart(3, MProductMin);
            return false;
        }
        if (!Pattern.matches("[0-9]+", String.valueOf(max)))
        {
            MainScreenController.errorPart(3, MProductMax);
            return false;
        }
        // Check data in relation to other data
        if (price < tprice)
        {
        MainScreenController.errorProduct(10, MProductP);
        return false;
        }
        if (associatedPart.isEmpty())
        {
            MainScreenController.errorProduct(1, null);
            return false;
        }
        if (stock < min)
        {
            MainScreenController.errorProduct(7, MProductI);
            return false;
        }
        if (stock > max)
        {
            MainScreenController.errorProduct(8, MProductI);
            return false;
        }
        return true;
    }

   /** This method resets the field styles back to normal in case they were changed by the error messages. */
   private void resetFieldStyles()
   {
        MProductID.setStyle("-fx-border-color: light gray");
        MProductN.setStyle("-fx-border-color: light gray");
        MProductI.setStyle("-fx-border-color: light gray");
        MProductP.setStyle("-fx-border-color: light gray");
        MProductMin.setStyle("-fx-border-color: light gray");
        MProductMax.setStyle("-fx-border-color: light gray");
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

   /** This method cancels the action of creating a new part by returning to the main screen. */
   public void cancelAction(javafx.event.ActionEvent actionEvent) throws IOException
   {
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Main_Screen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

}
